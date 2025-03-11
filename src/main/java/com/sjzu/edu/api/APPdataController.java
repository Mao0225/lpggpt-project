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

@Path(value = "/", viewPath = "/appdata")
public class APPdataController extends Controller {

    private static final String WEBAPP_ROOT = PathKit.getWebRootPath();
    private static final String UPLOAD_DIR = WEBAPP_ROOT + "/upload/temp/data";
    private static final String TABLE_NAME = "bse_data";

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

            List<UploadFile> allFiles = getFiles();

            // 第二步：此时可以获取普通参数
            String worker = getPara("worker");
            System.out.println("Worker参数值：" + worker);  // 现在应该能获取到值

            // 参数验证
            if (worker == null || worker.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "操作人信息不能为空");
                renderJson(result);
                return;
            }

            // 第三步：创建记录对象（必须在文件处理之后）
            Record record = new Record();
            record.set("worker", worker);

            // 3. 按类型分组处理文件
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

            // 4. 将路径拼接为字符串存入Record
            filePaths.forEach((dbField, paths) -> {
                record.set(dbField, String.join(",", paths));
            });
            String worker1 = getPara("worker");
            System.out.println("Worker参数值：" + worker1);  // 应该显示"猫"
            // 5. 保存到数据库
            boolean success = Db.save(TABLE_NAME, record);

            if (success) {
                result.put("code", 200);
                result.put("msg", "数据保存成功");
            } else {
                result.put("code", 500);
                result.put("msg", "数据库保存失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器异常: " + e.getMessage());
            e.printStackTrace();
        }
        renderJson(result);
    }

    private String saveUploadFile(UploadFile uf) throws IOException {
        // 生成唯一文件名
        String newName = UUID.randomUUID() + getFileExt(uf.getFileName());

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

    private String getFileExt(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }
}
