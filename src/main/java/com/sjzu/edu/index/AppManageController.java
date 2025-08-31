package com.sjzu.edu.index;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.AppManage;
import com.sjzu.edu.common.model.Bangdingren;
import com.sjzu.edu.index.AppManageService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class AppManageController extends Controller {

    @Inject
    AppManageService service;

    private final AppManage dao = new AppManage().dao();

    /**
     * 获取应用列表数据
     */
    public void list() {
        String name = getPara("name");
        String version = getPara("version");
        Integer pageNumber = getParaToInt("page",1);
        Page<AppManage> appList = service.searchPaginate(pageNumber,10,name, version); // 假设服务层支持过滤
        setAttr("appList", appList);
        setAttr("name", name);
        setAttr("version", version);
        render("app_manage_list.html");
    }

    /**
     * 获取单个应用信息
     */
    public void toEdit() {
        Integer id = getParaToInt();
        if (id == null) {
            renderJson(Ret.ok("data", new AppManage()));
            return;
        }
        AppManage app = service.findById(id);
        if (app != null) {
            setAttr("app", app);
            render("edit.html");
        } else {
            renderError(404); // 例如返回404页面
        }
//        renderJson(Ret.ok("data", app));
    }


    public void toAdd() {
        Integer id = getParaToInt();
        AppManage app;
        if (id != null) {
            // 加载现有记录
            app = service.findById(id);
            if (app == null) {
                // 如果记录不存在，返回 404
                renderError(404);
                return;
            }
            // 递增版本号
            String version = app.getVersion();
            if (version != null && version.matches("\\d+\\.\\d+\\.\\d+")) {
                String[] parts = version.split("\\.");
                int patch = Integer.parseInt(parts[2]) + 1; // 递增最后一位
                String newVersion = parts[0] + "." + parts[1] + "." + patch;
                app.setVersion(newVersion);
            } else {
                // 如果版本号格式不正确，设置默认值或抛出错误
                app.setVersion("1.0.0");
            }
        } else {
            // 新建记录，设置空的 AppManage 对象
            app = new AppManage();
            // 可选：设置默认版本号
            // app.setVersion("1.0.0");
        }
        setAttr("app", app);
        render("add.html");
    }

    /**
     * 保存或更新应用信息
     */
    public void save() {
        try {
            System.out.println("开始保存操作...");
            UploadFile uploadFile = getFile();

            AppManage app = getModel(AppManage.class, "app");
            System.out.println("应用详情：名称=" + app.getName() + "， версии=" + app.getVersion() + "，appcode=" + app.getAppcode());

            // 验证必填字段
            if (StrKit.isBlank(app.getName()) || StrKit.isBlank(app.getVersion()) || StrKit.isBlank(app.getAppcode())) {
                System.out.println("验证失败：应用名称或版本号或android包名为空");
                renderJson(Ret.fail("msg", "应用名称、版本号、android包名为必填项"));
                return;
            }

            // 处理文件上传
            if (uploadFile != null) {
                System.out.println("处理文件上传：原始文件名=" + uploadFile.getOriginalFileName());
                String uploadPath = getRequest().getServletContext().getRealPath("/upload") + File.separator + "app";
                System.out.println("上传路径：" + uploadPath);
                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    System.out.println("上传目录不存在，正在创建：" + uploadPath);
                    dir.mkdirs();
                }

                // 获取原始文件扩展名
                String originalFileName = uploadFile.getOriginalFileName();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String timestamp = sdf.format(new Date());
                String fileName = app.getName() + "_" + app.getVersion() + "_" + timestamp + fileExtension;
                System.out.println("生成文件名：" + fileName);

                File targetFile = new File(uploadPath, fileName);
                File tempFile = uploadFile.getFile(); // 获取临时文件
                try {
                    Files.copy(tempFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("文件已复制并保存至：" + targetFile.getAbsolutePath());
                    app.setFile("/upload/app/" + fileName);
                    System.out.println("应用文件路径已设置：" + app.getFile());

                    // 删除临时文件
                    if (tempFile.delete()) {
                        System.out.println("临时文件已删除：" + tempFile.getAbsolutePath());
                    } else {
                        System.out.println("无法删除临时文件：" + tempFile.getAbsolutePath());
                    }
                } catch (IOException e) {
                    System.out.println("文件复制失败：" + e.getMessage());
                    renderJson(Ret.fail("msg", "文件上传失败：" + e.getMessage()));
                    return;
                }
            } else {
                System.out.println("未上传文件");
            }

            app.setUpdatetime(new Date());
            System.out.println("更新时间已设置：" + app.getUpdatetime());
            if (app.getId() == null) {
                System.out.println("保存新应用记录");
                app.save();
            } else {
                app.update();
                System.out.println("更新现有应用记录，ID：" + app.getId());
            }
            System.out.println("保存操作完成，重定向至 /appmanage/list");
            redirect("/appmanage/list");
        } catch (Exception e) {
            System.out.println("保存过程中发生错误：" + e.getMessage());
            e.printStackTrace();
            renderJson(Ret.fail("msg", "保存失败：" + e.getMessage()));
            redirect("/appmanage/list");
        }
    }


    /**
     * 删除应用版本
     */
    public void deleteVersion() {
        service.deleteById(getParaToInt());
        redirect("/appmanage/list");
    }

    /**
     * 删除应用
     */
    public void deleteApp() {
        service.deleteByCode(getPara("appcode"));
        redirect("/appmanage/list");
    }

    /**
     * 获取应用历史版本列表数据
     */
    public void getVersionList() {
        String appcode = getPara("appcode");
        List<AppManage> historyVersions = dao.find("SELECT * FROM app_manage WHERE appcode = ? ORDER BY updatetime DESC", appcode);
        setAttr("list", historyVersions);
        render("versionlist.html");
    }




}