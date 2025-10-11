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

    // 记录已上传的文件ID，用于断点续传
    private static final Map<String, Set<String>> uploadedFileIdsMap = new ConcurrentHashMap<>();

    // 修正字段映射关系，确保每个类型对应正确的数据库字段
    private static final Map<String, String> FIELD_MAPPING = new HashMap<String, String>() {{
        put("doorplate", "door_video");         // 门牌照对应door_video
        put("gaspipe", "qiguan_video");         // 气管对应qiguan_video
        put("gascylinder", "daoguan_video");    // 导管(连接管)对应daoguan_video
        put("conduit", "louguan_video");        // 气瓶(供气合同)对应louguan_video
    }};



    public void upload() {
        JSONObject result = new JSONObject();
        long requestStartTime = System.currentTimeMillis();
        System.out.println("[" + new Date() + "] === 开始处理文件上传请求 ===");
        System.out.println("[" + new Date() + "] 请求参数列表: " + getParaMap());

        try {
            // 基础上传目录（相对路径）
            String baseUploadDir = "/upload/temp";
            // 构建完整的基础上传目录路径
            String webRootPath = PathKit.getWebRootPath();
            System.out.println("[" + new Date() + "] Web根目录路径: " + webRootPath);
            File baseDir = new File(webRootPath + baseUploadDir);
            System.out.println("[" + new Date() + "] 基础上传目录: " + baseDir.getAbsolutePath() + "，是否存在: " + baseDir.exists());

            // 确保基础上传目录存在
            if (!baseDir.exists()) {
                System.out.println("[" + new Date() + "] 基础上传目录不存在，尝试创建...");
                if (!baseDir.mkdirs()) {
                    String errorMsg = "无法创建上传根目录: " + baseDir.getAbsolutePath();
                    System.err.println("[" + new Date() + "] " + errorMsg);
                    result.put("code", 500);
                    result.put("msg", errorMsg);
                    renderJson(result);
                    return;
                }
                System.out.println("[" + new Date() + "] 基础上传目录创建成功");
            }

            // 使用相对路径获取上传文件，设置单个文件最大100MB
            System.out.println("[" + new Date() + "] 开始接收上传文件...");
            List<UploadFile> allFiles = getFiles(baseUploadDir, 100 * 1024 * 1024);
            System.out.println("[" + new Date() + "] 文件接收完成，共接收文件数: " + (allFiles != null ? allFiles.size() : 0));

            // 解析请求参数（移除totalFiles，因为单文件上传）
            String worker = getPara("worker");
            String uuid = getPara("uuid");
            String telephone = getPara("telephone");
            String fileType = getPara("fileType"); // 获取文件类型（image或video）
            String type = getPara("type"); // 获取媒体类型（doorplate等）

            // 打印参数详情
            System.out.println("[" + new Date() + "] 解析请求参数:");
            System.out.println("  worker: " + worker);
            System.out.println("  uuid: " + uuid);
            System.out.println("  telephone: " + telephone);
            System.out.println("  fileType: " + fileType);
            System.out.println("  type: " + type);

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

            if (type == null || type.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "媒体类型不能为空");
                renderJson(result);
                return;
            }

            // 验证是否有文件需要上传（假设单文件，检查size==1）
            if (allFiles == null || allFiles.isEmpty()) {
                result.put("code", 400);
                result.put("msg", "未检测到上传的文件");
                renderJson(result);
                return;
            }
            if (allFiles.size() > 1) {
                result.put("code", 400);
                result.put("msg", "本次上传仅支持单个文件");
                renderJson(result);
                return;
            }

            // 打印上传文件详情（单文件）
            UploadFile uf = allFiles.get(0);
            System.out.println("[" + new Date() + "] 上传文件:");
            System.out.println("  原始文件名: " + uf.getFileName());
            System.out.println("  文件大小: " + uf.getFile().length() + " bytes");
            System.out.println("  临时文件路径: " + uf.getFile().getAbsolutePath());
            System.out.println("  内容类型: " + uf.getContentType());

            // 处理上传文件（单文件）
            String dbField = FIELD_MAPPING.get(type);
            if (dbField == null) {
                String errorMsg = "未知的媒体类型: " + type + "，可用类型: " + FIELD_MAPPING.keySet();
                System.err.println("[" + new Date() + "] " + errorMsg);
                result.put("code", 400);
                result.put("msg", errorMsg);
                renderJson(result);
                return;
            }
            System.out.println("[" + new Date() + "] 媒体类型映射 - type:" + type + " -> dbField:" + dbField);

            // 根据文件类型保存到不同目录
            String filePath = saveUploadFile(uf, "video".equals(fileType), webRootPath);
            System.out.println("[" + new Date() + "] 文件保存路径: " + filePath);

            // 查询数据库中是否已有该UUID的记录
            Record existingRecord = Db.findFirst("SELECT * FROM " + TABLE_NAME + " WHERE uuid = ?", uuid);
            Record recordToSave;
            if (existingRecord != null) {
                // 记录存在，加载并追加文件路径
                System.out.println("[" + new Date() + "] UUID:" + uuid + " 记录已存在，开始更新文件路径");
                recordToSave = existingRecord;
            } else {
                // 记录不存在，创建新记录
                System.out.println("[" + new Date() + "] UUID:" + uuid + " 记录不存在，创建新记录");
                recordToSave = new Record();
                recordToSave.set("worker", worker);
                recordToSave.set("uuid", uuid);
                recordToSave.set("telephone", telephone);
                recordToSave.set("create_time", new Date());
            }

            // 更新记录中的文件路径（追加本次路径）
            String existingPaths = recordToSave.getStr(dbField);
            String newPaths = existingPaths == null ?
                    filePath :
                    existingPaths + "," + filePath;
            recordToSave.set(dbField, newPaths);
            System.out.println("[" + new Date() + "] 更新字段 " + dbField + " 的文件路径: " + newPaths);

            // 保存或更新到数据库
            boolean success;
            if (existingRecord != null) {
                // 更新（假设id是主键）
                success = Db.update(TABLE_NAME, "id", recordToSave);
                System.out.println("[" + new Date() + "] UUID:" + uuid + " 记录更新操作返回结果: " + success);
            } else {
                // 插入
                success = Db.save(TABLE_NAME, recordToSave);
                System.out.println("[" + new Date() + "] UUID:" + uuid + " 记录插入操作返回结果: " + success);
            }

            // 保存/更新后查询验证并打印详细信息
            Record savedRecord = null;
            if (success) {
                savedRecord = Db.findFirst("SELECT * FROM " + TABLE_NAME + " WHERE uuid = ?", uuid);
                if (savedRecord != null) {
                    System.out.println("[" + new Date() + "] === 保存/更新到 bse_data 表的数据是： ===");
                    System.out.println("ID: " + savedRecord.get("id"));
                    System.out.println("UUID: " + savedRecord.getStr("uuid"));
                    System.out.println("操作人: " + savedRecord.getStr("worker"));
                    System.out.println("联系电话: " + savedRecord.getStr("telephone"));
                    System.out.println("门牌照路径: " + savedRecord.getStr("door_video"));
                    System.out.println("气管路径: " + savedRecord.getStr("qiguan_video"));
                    System.out.println("连接管路径: " + savedRecord.getStr("daoguan_video"));
                    System.out.println("供气合同路径: " + savedRecord.getStr("louguan_video"));
                    System.out.println("创建时间: " + savedRecord.getDate("create_time"));
                    System.out.println("=================================================");
                } else {
                    System.err.println("[" + new Date() + "] 保存/更新返回成功，但查询不到记录 - UUID: " + uuid);
                    success = false; // 强制标记为失败
                }
            }

            if (success && savedRecord != null) {
                result.put("code", 200);
                result.put("msg", existingRecord != null ? "文件上传并更新记录成功" : "文件上传并新增记录成功");
                result.put("savedRecordId", savedRecord.get("id")); // 返回保存/更新的记录ID
            } else {
                result.put("code", 500);
                result.put("msg", "数据库保存/更新失败或验证未通过");
            }

        } catch (Exception e) {
            String errorMsg = "服务器处理异常: " + e.getMessage();
            System.err.println("[" + new Date() + "] " + errorMsg);
            e.printStackTrace(); // 打印完整堆栈信息
            result.put("code", 500);
            result.put("msg", errorMsg);
        }

        long requestEndTime = System.currentTimeMillis();
        System.out.println("[" + new Date() + "] === 文件上传请求处理完成，耗时: " + (requestEndTime - requestStartTime) + "ms ===");
        renderJson(result);
    }




    // 检查上传状态接口（也增加详细日志）
    public void checkUploadStatus() {
        JSONObject result = new JSONObject();
        System.out.println("[" + new Date() + "] === 开始处理上传状态查询请求 ===");
        try {
            String uuid = getPara("uuid");
            System.out.println("[" + new Date() + "] 状态查询 - UUID: " + uuid);

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

                System.out.println("[" + new Date() + "] 状态查询 - UUID:" + uuid + " 进度:" + currentCount + "/" + expectedCount);
                System.out.println("[" + new Date() + "] 已上传文件ID列表: " + uploadedFileIds);

                if (expectedCount <= 0) {
                    result.put("code", 400);
                    result.put("msg", "无法获取预期文件数量");
                    renderJson(result);
                    return;
                }

                boolean allFilesUploaded = currentCount >= expectedCount;
                if (allFilesUploaded) {
                    Record record = tempDataMap.get(uuid);
                    System.out.println("[" + new Date() + "] 状态查询 - 所有文件已上传，准备保存到数据库");

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
                    System.out.println("[" + new Date() + "] 状态查询 - 数据保存结果: " + success);

                    // 保存后查询验证
                    Record savedRecord = null;
                    if (success) {
                        savedRecord = Db.findFirst("SELECT * FROM " + TABLE_NAME + " WHERE uuid = ?", uuid);
                        if (savedRecord != null) {
                            System.out.println("[" + new Date() + "] === 状态查询：保存到 bse_data 表的数据是： ===");
                            System.out.println("ID: " + savedRecord.get("id"));
                            System.out.println("UUID: " + savedRecord.getStr("uuid"));
                            System.out.println("操作人: " + savedRecord.getStr("worker"));
                            System.out.println("门牌照路径: " + savedRecord.getStr("door_video"));
                            System.out.println("=================================================");
                        } else {
                            System.err.println("[" + new Date() + "] 状态查询 - 保存成功但查询不到记录 - UUID: " + uuid);
                            success = false;
                        }
                    }

                    if (success && savedRecord != null) {
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
        System.out.println("[" + new Date() + "] === 上传状态查询请求处理完成 ===");
        renderJson(result);
    }

    // 其他方法保持不变，以下为关键方法的日志增强
    private String saveUploadFile(UploadFile uf, boolean isVideo, String webRootPath) throws IOException {
        System.out.println("[" + new Date() + "] 开始处理文件保存 - 文件名: " + uf.getFileName() + ", 是否视频: " + isVideo);

        String ext = getFileExt(uf);
        String newName = UUID.randomUUID() + ext;
        System.out.println("[" + new Date() + "] 文件重命名 - 原始名: " + uf.getFileName() + ", 新文件名: " + newName);

        // 构建目标目录的绝对路径
        String targetDirRelative = isVideo ? UPLOAD_VIDEO_RELATIVE_DIR : UPLOAD_IMAGE_RELATIVE_DIR;
        String targetDirPath = webRootPath + targetDirRelative;
        File targetDir = new File(targetDirPath);
        System.out.println("[" + new Date() + "] 目标保存目录: " + targetDir.getAbsolutePath() + ", 是否存在: " + targetDir.exists());

        // 确保目录存在
        if (!targetDir.exists()) {
            System.out.println("[" + new Date() + "] 目标目录不存在，尝试创建...");
            if (!targetDir.mkdirs()) {
                throw new IOException("目录创建失败: " + targetDir.getAbsolutePath());
            }
            System.out.println("[" + new Date() + "] 目标目录创建成功");
        }

        File destFile = new File(targetDir, newName);
        // 移动文件
        System.out.println("[" + new Date() + "] 开始移动文件 - 源: " + uf.getFile().getAbsolutePath() + " -> 目标: " + destFile.getAbsolutePath());
        Files.move(uf.getFile().toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("[" + new Date() + "] 文件移动成功，目标文件大小: " + destFile.length() + " bytes");

        // 返回相对于web根目录的路径
        String relativePath = targetDirRelative + "/" + newName;
        String normalizedPath = relativePath.replace("\\", "/");
        System.out.println("[" + new Date() + "] 文件保存完成，相对路径: " + normalizedPath);

        return normalizedPath;
    }

    private String getFileExt(UploadFile uf) {
        String fileName = uf.getFileName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            String ext = fileName.substring(dotIndex);
            System.out.println("[" + new Date() + "] 从文件名获取扩展名: " + ext);
            return ext;
        }

        String mimeType = uf.getContentType();
        String ext = getExtensionByMimeType(mimeType);
        System.out.println("[" + new Date() + "] 从MIME类型获取扩展名 - MIME: " + mimeType + ", 扩展名: " + ext);
        return ext;
    }

    // 清理过期数据方法保持不变
    public void cleanExpiredData() {
        // ... 原有代码 ...
    }

    private String getExtensionByMimeType(String mimeType) {
        // ... 原有代码 ...
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