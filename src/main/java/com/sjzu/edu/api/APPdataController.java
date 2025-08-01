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

    private static final String WEBAPP_ROOT = PathKit.getWebRootPath();
    private static final String UPLOAD_DIR = WEBAPP_ROOT + "/upload/temp/data";
    private static final String TABLE_NAME = "bse_data";

    // 使用AtomicInteger确保并发计数安全
    private static final Map<String, AtomicInteger> fileCountMap = new ConcurrentHashMap<>();
    private static final Map<String, Record> tempDataMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> expectedFileCountMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> lastUpdateTimeMap = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

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
            List<UploadFile> allFiles = getFiles();
            String worker = getPara("worker");
            String uuid = getPara("uuid");
            String telephone = getPara("telephone");
            int totalFiles = getParaToInt("totalFiles", 0);

            System.out.println("worker:" + worker);
            System.out.println("uuid:" + uuid);
            System.out.println("telephone:" + telephone);
            System.out.println("totalFiles:" + totalFiles);
            System.out.println("当前请求文件数:" + allFiles.size()); // 新增日志

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

            // 初始化记录（添加主键ID）
            Record record = tempDataMap.computeIfAbsent(uuid, k -> {
                Record newRecord = new Record();
                //newRecord.set("id", UUID.randomUUID().toString()); // 关键：添加主键
                newRecord.set("worker", worker);
                newRecord.set("uuid", uuid);
                newRecord.set("telephone", telephone);
                return newRecord;
            });

            // 初始化预期文件数和原子计数器
            expectedFileCountMap.putIfAbsent(uuid, totalFiles);
            fileCountMap.putIfAbsent(uuid, new AtomicInteger(0));
            lastUpdateTimeMap.put(uuid, System.currentTimeMillis());

            // 处理上传文件
            Map<String, List<String>> filePaths = new HashMap<>();
            for (UploadFile uf : allFiles) {
                String paramName = uf.getParameterName();
                String[] parts = paramName.split("_");
                if (parts.length < 2) continue;

                String type = parts[0];
                String dbField = FIELD_MAPPING.get(type);
                if (dbField == null) continue;

                String filePath = saveUploadFile(uf);
                filePaths.computeIfAbsent(dbField, k -> new ArrayList<>()).add(filePath);
            }

            // 更新记录中的文件路径
            filePaths.forEach((dbField, paths) -> {
                String existingPaths = record.getStr(dbField);
                record.set(dbField, existingPaths == null ?
                        String.join(",", paths) :
                        existingPaths + "," + String.join(",", paths));
            });

            // 原子方式更新计数（关键修复）
            AtomicInteger counter = fileCountMap.get(uuid);
            int currentFileCount = counter.addAndGet(allFiles.size());
            System.out.println("UUID:" + uuid + " 累计上传:" + currentFileCount + "/" + totalFiles);

            // 检查是否所有文件都已上传
            boolean allFilesUploaded = currentFileCount >= totalFiles;
            System.out.println("UUID:" + uuid + " 是否完成上传:" + allFilesUploaded);

            if (allFilesUploaded) {
                // 表存在性测试（现在会执行）
                try {
                    Db.query("SELECT 1 FROM bse_data LIMIT 1");
                    System.out.println("表 bse_data 存在");
                } catch (Exception e) {
                    System.out.println("表 bse_data 访问失败: " + e.getMessage());
                    result.put("code", 500);
                    result.put("msg", "数据库表访问失败");
                    renderJson(result);
                    return;
                }

                // 保存到数据库
                boolean success = Db.save(TABLE_NAME, record);
                System.out.println("UUID:" + uuid + " 保存结果:" + success);

                // 清理临时数据
                tempDataMap.remove(uuid);
                fileCountMap.remove(uuid);
                expectedFileCountMap.remove(uuid);
                lastUpdateTimeMap.remove(uuid);

                if (success) {
                    result.put("code", 200);
                    result.put("msg", "数据保存成功");
                } else {
                    result.put("code", 500);
                    result.put("msg", "数据库保存失败");
                }
            } else {
                result.put("code", 201);
                result.put("msg", "部分文件上传成功");
                result.put("uploadedCount", currentFileCount);
                result.put("expectedCount", totalFiles);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器异常: " + e.getMessage());
            e.printStackTrace();
        }
        renderJson(result);
    }

    // 检查上传状态接口（同步修改计数方式）
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
                int currentCount = fileCountMap.get(uuid).get(); // 使用原子计数器
                int expectedCount = expectedFileCountMap.getOrDefault(uuid, 0);
                System.out.println("checkUploadStatus - UUID:" + uuid + " 进度:" + currentCount + "/" + expectedCount);

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
                        System.out.println("表 bse_data 存在");
                    } catch (Exception e) {
                        System.out.println("表 bse_data 访问失败: " + e.getMessage());
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

    // 清理过期数据方法保持不变
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
            System.out.println("清理过期数据，UUID: " + uuid);
        }

        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "清理完成，共清理 " + expiredUuids.size() + " 条过期数据");
        renderJson(result);
    }

    // 文件保存相关方法保持不变
    private String saveUploadFile(UploadFile uf) throws IOException {
        String ext = getFileExt(uf);
        String newName = UUID.randomUUID() + ext;

        File targetDir = new File(UPLOAD_DIR);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            throw new IOException("目录创建失败: " + targetDir.getAbsolutePath());
        }

        File destFile = new File(targetDir, newName);
        Files.move(uf.getFile().toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return "upload/temp/data/" + newName;
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
        switch (mimeType) {
            case "image/jpeg": return ".jpg";
            case "image/png": return ".png";
            case "video/mp4": return ".mp4";
            case "video/webm": return ".webm";
            default: return "";
        }
    }
}
