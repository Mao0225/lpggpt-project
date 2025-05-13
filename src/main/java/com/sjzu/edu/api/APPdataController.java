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

@Path(value = "/", viewPath = "/appdata")
public class APPdataController extends Controller {

    private static final String WEBAPP_ROOT = PathKit.getWebRootPath();
    private static final String UPLOAD_DIR = WEBAPP_ROOT + "/upload/temp/data";
    private static final String TABLE_NAME = "bse_data";
    // 移除固定文件数量限制

    // 临时存储上传中的数据
    private static final Map<String, Record> tempDataMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> fileCountMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> expectedFileCountMap = new ConcurrentHashMap<>(); // 存储每个UUID预期的文件数量
    private static final Map<String, Long> lastUpdateTimeMap = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30分钟过期时间

    // 字段映射关系（前端类型 -> 数据库字段）
    private static final Map<String, String> FIELD_MAPPING = new HashMap<String, String>() {{
        put("doorplate", "door_video");
        put("gaspipe", "qiguan_video");
        put("gascylinder", "louguan_video");
        put("conduit", "daoguan_video");
    }};

    public void upload() {
        JSONObject result = new JSONObject();
        System.out.println("所有接收到的参数：" + getParaMap());

        try {
            // 必须先调用 getFiles() 处理文件上传
            List<UploadFile> allFiles = getFiles();

            // 再获取普通参数
            String worker = getPara("worker");
            String uuid = getPara("uuid");
            String telephone = getPara("telephone");
            int totalFiles = getParaToInt("totalFiles", 0); // 新增：从前端获取总文件数

            System.out.println("worker:" + worker);
            System.out.println("uuid:" + uuid);
            System.out.println("telephone:" + telephone);
            System.out.println("totalFiles:" + totalFiles);

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

            // 从临时存储中获取或创建记录
            Record record = tempDataMap.computeIfAbsent(uuid, k -> {
                Record newRecord = new Record();
                newRecord.set("worker", worker);
                newRecord.set("uuid", uuid);
                newRecord.set("telephone", telephone);
                return newRecord;
            });

            // 存储预期的文件总数
            expectedFileCountMap.putIfAbsent(uuid, totalFiles);

            // 更新最后更新时间
            lastUpdateTimeMap.put(uuid, System.currentTimeMillis());

            // 处理上传的文件
            Map<String, List<String>> filePaths = new HashMap<>();

            for (UploadFile uf : allFiles) {
                String paramName = uf.getParameterName();
                String[] parts = paramName.split("_");
                if (parts.length < 2) continue;

                String type = parts[0];
                String dbField = FIELD_MAPPING.get(type);
                if (dbField == null) continue;

                // 处理文件保存
                String filePath = saveUploadFile(uf);

                // 添加路径到对应类型
                filePaths.computeIfAbsent(dbField, k -> new ArrayList<>()).add(filePath);
            }

            // 将路径添加到记录中
            filePaths.forEach((dbField, paths) -> {
                // 获取现有路径
                String existingPaths = record.getStr(dbField);
                if (existingPaths == null) {
                    record.set(dbField, String.join(",", paths));
                } else {
                    record.set(dbField, existingPaths + "," + String.join(",", paths));
                }
            });

            // 更新文件计数
            int currentFileCount = fileCountMap.getOrDefault(uuid, 0) + allFiles.size();
            fileCountMap.put(uuid, currentFileCount);

            // 获取预期文件总数
            int expectedFiles = expectedFileCountMap.get(uuid);

            // 检查是否所有文件都已上传
            boolean allFilesUploaded = currentFileCount >= expectedFiles;

            if (allFilesUploaded) {
                // 保存到数据库
                boolean success = Db.save(TABLE_NAME, record);

                // 清理临时数据
                tempDataMap.remove(uuid);
                fileCountMap.remove(uuid);
                expectedFileCountMap.remove(uuid);
                lastUpdateTimeMap.remove(uuid);

                if (success) {
                    System.out.println("保存成功，uuid: " + uuid);
                    result.put("code", 200);
                    result.put("msg", "数据保存成功");
                } else {
                    result.put("code", 500);
                    result.put("msg", "数据库保存失败");
                }
            } else {
                // 返回部分成功，等待其他文件
                result.put("code", 201);
                result.put("msg", "部分文件上传成功，等待剩余文件");
                result.put("uploadedCount", currentFileCount);
                result.put("expectedCount", expectedFiles);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器异常: " + e.getMessage());
            e.printStackTrace();
        }
        renderJson(result);
    }

    // 检查上传状态
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

            // 检查是否存在该uuid的记录
            if (tempDataMap.containsKey(uuid)) {
                // 检查是否所有必要的文件都已上传
                int currentCount = fileCountMap.getOrDefault(uuid, 0);
                int expectedCount = expectedFileCountMap.getOrDefault(uuid, 0);

                if (expectedCount <= 0) {
                    result.put("code", 400);
                    result.put("msg", "无法获取预期文件数量");
                    renderJson(result);
                    return;
                }

                boolean allFilesUploaded = currentCount >= expectedCount;

                if (allFilesUploaded) {
                    // 所有文件已上传，可以保存到数据库
                    Record record = tempDataMap.get(uuid);
                    boolean success = Db.save(TABLE_NAME, record);

                    if (success) {
                        // 清理临时数据
                        tempDataMap.remove(uuid);
                        fileCountMap.remove(uuid);
                        expectedFileCountMap.remove(uuid);
                        lastUpdateTimeMap.remove(uuid);

                        result.put("code", 200);
                        result.put("msg", "所有文件已上传，数据已保存");
                    } else {
                        result.put("code", 500);
                        result.put("msg", "数据库保存失败");
                    }
                } else {
                    result.put("code", 206);
                    result.put("msg", "部分文件已上传，等待剩余文件");
                    result.put("uploadedCount", currentCount);
                    result.put("expectedCount", expectedCount);
                }
            } else {
                result.put("code", 404);
                result.put("msg", "未找到上传记录或记录已过期");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器异常: " + e.getMessage());
            e.printStackTrace();
        }
        renderJson(result);
    }

    // 清理过期的临时数据
    public void cleanExpiredData() {
        long currentTime = System.currentTimeMillis();
        List<String> expiredUuids = new ArrayList<>();

        // 找出所有过期的uuid
        for (Map.Entry<String, Long> entry : lastUpdateTimeMap.entrySet()) {
            if (currentTime - entry.getValue() > EXPIRATION_TIME) {
                expiredUuids.add(entry.getKey());
            }
        }

        // 清理过期数据
        for (String uuid : expiredUuids) {
            tempDataMap.remove(uuid);
            fileCountMap.remove(uuid);
            expectedFileCountMap.remove(uuid);
            lastUpdateTimeMap.remove(uuid);
        }

        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "清理完成，共清理 " + expiredUuids.size() + " 条过期数据");
        renderJson(result);
    }

    private String saveUploadFile(UploadFile uf) throws IOException {
        // 生成唯一文件名
        String ext = getFileExt(uf);
        String newName = UUID.randomUUID() + ext;

        // 创建目标目录
        File targetDir = new File(UPLOAD_DIR);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            throw new IOException("目录创建失败: " + targetDir.getAbsolutePath());
        }

        // 移动文件
        File destFile = new File(targetDir, newName);
        Files.move(
                uf.getFile().toPath(),
                destFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        // 返回相对路径
        return "upload/temp/data/" + newName;
    }

    private String getFileExt(UploadFile uf) {
        // 优先从文件名获取扩展名
        String fileName = uf.getFileName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            return fileName.substring(dotIndex);
        }

        // 根据MIME类型补充扩展名
        String mimeType = uf.getContentType();
        return getExtensionByMimeType(mimeType);
    }

    private String getExtensionByMimeType(String mimeType) {
        switch (mimeType) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "video/mp4":
                return ".mp4";
            case "video/webm":
                return ".webm";
            default:
                return ""; // 未知类型留空
        }
    }
}