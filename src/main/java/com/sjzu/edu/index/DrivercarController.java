package com.sjzu.edu.index;



import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.Drivercar;
import com.sjzu.edu.common.model.Uploaddianzibiaoqian;

import java.io.File;
import java.util.List;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/drivercar")
@Before(UserInterceptor.class)
public class DrivercarController extends Controller {
    @Inject
    DrivercarService service;
    private Uploaddianzibiaoqian dianzibiaoqian = new Uploaddianzibiaoqian().dao();
    //图片上传功能
    public void localUpload() {
        List<UploadFile> uploadFiles = getFiles();
        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            StringBuilder fileUrls = new StringBuilder();
            int count = 1;

            // 获取配置文件中的根路径
            for (UploadFile uploadFile : uploadFiles) {
                String newFileName = uploadFile.getFileName(); // 获取原始文件名，而不是生成新的文件名

                String savePath = "drivercar"; // 新的保存路径
                File saveDir = new File(uploadFile.getUploadPath(), savePath);
                if (!saveDir.exists()) {
                    saveDir.mkdirs(); // 如果目录不存在，创建目录
                }
                File newFile = new File(saveDir, newFileName);
                uploadFile.getFile().renameTo(newFile); // 将文件移动到新的保存路径

                // 使用配置文件中的根路径构建文件的 URL 路径
                String fileUrl = "/upload/temp/" + savePath + "/" + newFileName;
                System.out.println("File URL " + fileUrl);

                fileUrls.append(fileUrl);
                if (count < uploadFiles.size()) {
                    fileUrls.append(","); // 如果还有更多的文件，添加一个逗号分隔符
                }
                count++;
            }

            // 返回所有文件路径的字符串
            System.out.println("File URLs: " + fileUrls.toString());
            renderJson("url", fileUrls.toString());
        } else {
            renderError(400);
        }
    }

    public void drivercarlist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String drivername = getPara("drivername");
        String driverphone = getPara("driverphone");
        String drivercarno = getPara("drivercarno");
        String carname = getPara("carname");
        String carshengchanriqi = getPara("carshengchanriqi");

        setAttr("drivername", drivername);
        setAttr("driverphone", driverphone);
        setAttr("drivercarno", drivercarno);
        setAttr("carname", carname);
        setAttr("carshengchanriqi", carshengchanriqi);
        System.out.println(service.search(pageNumber, pageSize,drivername,driverphone,drivercarno,carname,carshengchanriqi).getList());
        setAttr("drivercar",service.search(pageNumber, pageSize,drivername,driverphone,drivercarno,carname,carshengchanriqi));
        render("drivercar.html");
//        setAttr("drivercar", service.paginate(getParaToInt(0, 1),10));
//        render("drivercar.html");
    }




    public void save(){
        Drivercar drivercar = getModel(Drivercar.class, "drivercar");
        System.out.println("drivercar/save");
        if (drivercar.save()) {
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/drivercar/drivercarlist");
        } else {
            redirect("/user/userlist");
        }
    }





    public void searchdata(){
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String drivername = getPara("drivername");
        String driverphone = getPara("driverphone");
        String drivercarno = getPara("drivercarno");
        String carname = getPara("carname");
        String carshengchanriqi = getPara("carshengchanriqi");

        setAttr("drivername", drivername);
        setAttr("driverphone", driverphone);
        setAttr("drivercarno", drivercarno);
        setAttr("carname", carname);
        setAttr("carshengchanriqi", carshengchanriqi);
        setAttr("drivercar",service.search(pageNumber, pageSize,drivername,driverphone,drivercarno,carname,carshengchanriqi));
        render("drivercar.html");
    }
    public void edit() {
        setAttr("drivercar", service.findById(getParaToInt()));
    }
    public void update() {
        Drivercar drivercar = getModel(Drivercar.class, "drivercar");

        if (drivercar.update()) {
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/drivercar/drivercarlist");
        } else {
            redirect("/user/userlist");
        }
    }
    public void add() {
        System.out.println("add");
        render("add.html");

    }
    public void delete() {
        System.out.println("delete function");
        service.deleteById(getParaToInt());
        redirect("/drivercar/drivercarlist");
    }
}



