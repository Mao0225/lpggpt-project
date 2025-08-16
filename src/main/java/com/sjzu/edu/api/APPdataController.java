package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path(value = "/", viewPath = "/appdata")
public class APPdataController extends Controller {

    // 使用相对路径而非绝对路径，避免路径拼接问题
    private static final String UPLOAD_IMAGE_RELATIVE_DIR = "/upload/temp/data/images";
    private static final String UPLOAD_VIDEO_RELATIVE_DIR = "/upload/temp/data/videos";
    private static final String TABLE_NAME = "bse_data";

    // 使用AtomicInteger确保并发计数安全
    private static final Map<String, AtomicInteger> fileCountMap = new ConcurrentHashMap<>();
    private static final Map<String, Record> tempDataMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> expectedFileCountMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> lastUpdateTimeMap = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    // 新增：记录已上传的文件ID，用于断点续传
    private static final Map<String, Set<String>> uploadedFileIdsMap = new ConcurrentHashMap<>();

    private static final Map<String, String> FIELD_MAPPING = new HashMap<String, String>() {{
        put("doorplate", "door_video");
        put("gaspipe", "qiguan_video");
        put("gascylinder", "louguan_video");
        put("conduit", "daoguan_video");
    }};

    public void upload() {
        JSONObject result = new JSONObject();
        System.out.println("[" + new Date() + "] 接收到文件上传请求，参数：" + getParaMap());

        try {
            // 基础上传目录（相对路径）
            String baseUploadDir = "/upload/temp";
            // 构建完整的基础上传目录路径
            String webRootPath = PathKit.getWebRootPath();
            File baseDir = new File(webRootPath + baseUploadDir);

            // 确保基础上传目录存在
            if (!baseDir.exists()) {
                if (!baseDir.mkdirs()) {
                    String errorMsg = "无法创建上传根目录: " + baseDir.getAbsolutePath();
                    System.err.println("[" + new Date() + "] " + errorMsg);
                    result.put("code", 500);
                    result.put("msg", errorMsg);
                    renderJson(result);
                    return;
                }
            }

            // 使用相对路径获取上传文件，设置单个文件最大100MB
            List<UploadFile> allFiles = getFiles(baseUploadDir, 100 * 1024 * 1024);

            String worker = getPara("worker");
            String uuid = getPara("uuid");
            String telephone = getPara("telephone");
            int totalFiles = getParaToInt("totalFiles", 0);
            String fileType = getPara("fileType"); // 获取文件类型（image或video）
            String type = getPara("type"); // 获取媒体类型（doorplate等）
            String fileId = getPara("fileId"); // 新增：文件唯一标识，用于断点续传

            // 验证文件ID
            if (fileId == null || fileId.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "文件ID不能为空");
                renderJson(result);
                return;
            }

            System.out.println("[" + new Date() + "] 处理上传 - UUID:" + uuid +
                    ", 文件ID:" + fileId +
                    ", 工作者:" + worker +
                    ", 文件类型:" + fileType +
                    ", 媒体类型:" + type +
                    ", 总文件数:" + totalFiles +
                    ", 当前文件数:" + (allFiles != null ? allFiles.size() : 0));

            // 参数验证
            if (worker == null || worker.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "操作人信息不能为空");
                renderJson(result);
                return;
            }

            if (uuid == null || uuid.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "UUID不能为空");
                renderJson(result);
                return;
            }

            if (totalFiles <= 0) {
                result.put("code", 400);
                result.put("msg", "总文件数必须大于0");
                renderJson(result);
                return;
            }

            if (type == null || type.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "媒体类型不能为空");
                renderJson(result);
                return;
            }

            // 验证是否有文件需要上传
            if (allFiles == null || allFiles.isEmpty()) {
                result.put("code", 400);
                result.put("msg", "未检测到上传的文件");
                renderJson(result);
                return;
            }

            // 初始化记录
            Record record = tempDataMap.computeIfAbsent(uuid, k -> {
                Record newRecord = new Record();
                newRecord.set("worker", worker);
                newRecord.set("uuid", uuid);
                newRecord.set("telephone", telephone);
                newRecord.set("create_time", new Date()); // 添加创建时间
                return newRecord;
            });

            // 初始化预期文件数、原子计数器和已上传文件ID集合
            expectedFileCountMap.putIfAbsent(uuid, totalFiles);
            fileCountMap.putIfAbsent(uuid, new AtomicInteger(0));
            uploadedFileIdsMap.putIfAbsent(uuid, ConcurrentHashMap.newKeySet());
            lastUpdateTimeMap.put(uuid, System.currentTimeMillis());

            // 检查文件是否已上传，实现断点续传
            Set<String> uploadedFileIds = uploadedFileIdsMap.get(uuid);
            if (uploadedFileIds.contains(fileId)) {
                System.out.println("[" + new Date() + "] 文件已上传，跳过 - UUID:" + uuid + ", 文件ID:" + fileId);
                result.put("code", 202);
                result.put("msg", "文件已上传");
                result.put("uploadedCount", fileCountMap.get(uuid).get());
                result.put("expectedCount", totalFiles);
                renderJson(result);
                return;
            }

            // 处理上传文件
            Map<String, List<String>> filePaths = new HashMap<>();
            for (UploadFile uf : allFiles) {
                String dbField = FIELD_MAPPING.get(type);
                if (dbField == null) {
                    String errorMsg = "未知的媒体类型: " + type;
                    System.err.println("[" + new Date() + "] " + errorMsg);
                    result.put("code", 400);
                    result.put("msg", errorMsg);
                    renderJson(result);
                    return;
                }

                // 根据文件类型保存到不同目录
                String filePath = saveUploadFile(uf, "video".equals(fileType), webRootPath);
                filePaths.computeIfAbsent(dbField, k -> new ArrayList<>()).add(filePath);
            }

            // 更新记录中的文件路径
            filePaths.forEach((dbField, paths) -> {
                String existingPaths = record.getStr(dbField);
                String newPaths = existingPaths == null ?
                        String.join(",", paths) :
                        existingPaths + "," + String.join(",", paths);
                record.set(dbField, newPaths);
            });

            // 标记文件已上传
            uploadedFileIds.add(fileId);

            // 原子方式更新计数
            AtomicInteger counter = fileCountMap.get(uuid);
            int currentFileCount = counter.addAndGet(allFiles.size());
            System.out.println("[" + new Date() + "] UUID:" + uuid + " 累计上传:" + currentFileCount + "/" + totalFiles);

            // 检查是否所有文件都已上传
            boolean allFilesUploaded = currentFileCount >= totalFiles;
            System.out.println("[" + new Date() + "] UUID:" + uuid + " 是否完成上传:" + allFilesUploaded);

            if (allFilesUploaded) {
                // 表存在性测试
                try {
                    Db.query("SELECT 1 FROM bse_data LIMIT 1");
                    System.out.println("[" + new Date() + "] 表 bse_data 存在");
                } catch (Exception e) {
                    String errorMsg = "表 bse_data 访问失败: " + e.getMessage();
                    System.err.println("[" + new Date() + "] " + errorMsg);
                    result.put("code", 500);
                    result.put("msg", "数据库表访问失败");
                    renderJson(result);
                    return;
                }

                // 保存到数据库
                boolean success = Db.save(TABLE_NAME, record);
                System.out.println("[" + new Date() + "] UUID:" + uuid + " 保存结果:" + success);

                // 清理临时数据
                tempDataMap.remove(uuid);
                fileCountMap.remove(uuid);
                expectedFileCountMap.remove(uuid);
                lastUpdateTimeMap.remove(uuid);
                uploadedFileIdsMap.remove(uuid);

                if (success) {
                    result.put("code", 200);
                    result.put("msg", "所有数据保存成功");
                    result.put("uploadedCount", currentFileCount);
                    result.put("expectedCount", totalFiles);
                } else {
                    result.put("code", 500);
                    result.put("msg", "数据库保存失败");
                }
            } else {
                result.put("code", 201);
                result.put("msg", "部分文件上传成功");
                result.put("uploadedCount", currentFileCount);
                result.put("expectedCount", totalFiles);
                // 新增：返回已上传的文件ID列表
                result.put("uploadedFileIds", new ArrayList<>(uploadedFileIds));
            }
        } catch (Exception e) {
            String errorMsg = "服务器异常: " + e.getMessage();
            System.err.println("[" + new Date() + "] " + errorMsg);
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", errorMsg);
        }
        renderJson(result);
    }

    // 检查上传状态接口
    public void checkUploadStatus() {
        JSONObject result = new JSONObject();
        try {
            String uuid = getPara("uuid");
            if (uuid == null || uuid.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "UUID不能为空");
                renderJson(result);
                return;
            }

            if (tempDataMap.containsKey(uuid)) {
                int currentCount = fileCountMap.get(uuid).get();
                int expectedCount = expectedFileCountMap.getOrDefault(uuid, 0);
                Set<String> uploadedFileIds = uploadedFileIdsMap.getOrDefault(uuid, Collections.emptySet());

                System.out.println("[" + new Date() + "] checkUploadStatus - UUID:" + uuid + " 进度:" + currentCount + "/" + expectedCount);

                if (expectedCount <= 0) {
                    result.put("code", 400);
                    result.put("msg", "无法获取预期文件数量");
                    renderJson(result);
                    return;
                }

                boolean allFilesUploaded = currentCount >= expectedCount;
                if (allFilesUploaded) {
                    Record record = tempDataMap.get(uuid);

                    // 表存在性测试
                    try {
                        Db.query("SELECT 1 FROM bse_data LIMIT 1");
                        System.out.println("[" + new Date() + "] 表 bse_data 存在");
                    } catch (Exception e) {
                        System.out.println("[" + new Date() + "] 表 bse_data 访问失败: " + e.getMessage());
                        result.put("code", 500);
                        result.put("msg", "数据库表访问失败");
                        renderJson(result);
                        return;
                    }

                    boolean success = Db.save(TABLE_NAME, record);
                    if (success) {
                        tempDataMap.remove(uuid);
                        fileCountMap.remove(uuid);
                        expectedFileCountMap.remove(uuid);
                        lastUpdateTimeMap.remove(uuid);
                        uploadedFileIdsMap.remove(uuid);
                        result.put("code", 200);
                        result.put("msg", "所有文件已上传，数据已保存");
                    } else {
                        result.put("code", 500);
                        result.put("msg", "数据库保存失败");
                    }
                } else {
                    result.put("code", 206);
                    result.put("msg", "部分文件已上传");
                    result.put("uploadedCount", currentCount);
                    result.put("expectedCount", expectedCount);
                    result.put("uploadedFileIds", new ArrayList<>(uploadedFileIds));
                }
            } else {
                result.put("code", 404);
                result.put("msg", "未找到上传记录或已过期");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器异常: " + e.getMessage());
            e.printStackTrace();
        }
        renderJson(result);
    }

    // 清理过期数据
    public void cleanExpiredData() {
        long currentTime = System.currentTimeMillis();
        List<String> expiredUuids = new ArrayList<>();

        for (Map.Entry<String, Long> entry : lastUpdateTimeMap.entrySet()) {
            if (currentTime - entry.getValue() > EXPIRATION_TIME) {
                expiredUuids.add(entry.getKey());
            }
        }

        for (String uuid : expiredUuids) {
            tempDataMap.remove(uuid);
            fileCountMap.remove(uuid);
            expectedFileCountMap.remove(uuid);
            lastUpdateTimeMap.remove(uuid);
            uploadedFileIdsMap.remove(uuid);
            System.out.println("[" + new Date() + "] 清理过期数据，UUID: " + uuid);
        }

        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "清理完成，共清理 " + expiredUuids.size() + " 条过期数据");
        renderJson(result);
    }

    // 文件保存方法，增加详细日志
    private String saveUploadFile(UploadFile uf, boolean isVideo, String webRootPath) throws IOException {
        String ext = getFileExt(uf);
        String newName = UUID.randomUUID() + ext;

        // 构建目标目录的绝对路径
        String targetDirRelative = isVideo ? UPLOAD_VIDEO_RELATIVE_DIR : UPLOAD_IMAGE_RELATIVE_DIR;
        String targetDirPath = webRootPath + targetDirRelative;
        File targetDir = new File(targetDirPath);

        // 确保目录存在
        if (!targetDir.exists()) {
            // 使用mkdirs()而不是mkdir()，确保能创建多级目录
            if (!targetDir.mkdirs()) {
                throw new IOException("目录创建失败: " + targetDir.getAbsolutePath());
            }
            System.out.println("[" + new Date() + "] 创建目录成功: " + targetDir.getAbsolutePath());
        }

        File destFile = new File(targetDir, newName);
        // 移动文件
        Files.move(uf.getFile().toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("[" + new Date() + "] 文件移动成功: " + destFile.getAbsolutePath());

        // 返回相对于web根目录的路径
        String relativePath = targetDirRelative + "/" + newName;

        // 确保路径使用统一的斜杠
        return relativePath.replace("\\", "/");
    }

    private String getFileExt(UploadFile uf) {
        String fileName = uf.getFileName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            return fileName.substring(dotIndex);
        }

        String mimeType = uf.getContentType();
        return getExtensionByMimeType(mimeType);
    }

    private String getExtensionByMimeType(String mimeType) {
        if (mimeType == null) return "";

        switch (mimeType) {
            case "image/jpeg": return ".jpg";
            case "image/png": return ".png";
            case "image/gif": return ".gif";
            case "video/mp4": return ".mp4";
            case "video/quicktime": return ".mov";
            case "video/x-msvideo": return ".avi";
            case "video/webm": return ".webm";
            default: return "";
        }
    }
}
