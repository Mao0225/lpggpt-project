package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.Bangdingren;

import java.io.File;
import java.util.Arrays;
import java.util.List;


@Path(value = "/", viewPath = "/bangdingren")

public class BangdingrenController extends Controller {
    //private Bangdingren dao = new Bangdingren().dao();
    @Inject
    BangdingrenService service;
    public void localUpload() {
        List<UploadFile> uploadFiles = getFiles();
        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            StringBuilder fileUrls = new StringBuilder();
            int count = 1;
            for (UploadFile uploadFile : uploadFiles) {
                String newFileName = uploadFile.getFileName(); // 获取原始文件名，而不是生成新的文件名

                String savePath = "bangdingren"; // 新的保存路径
                File saveDir = new File(uploadFile.getUploadPath(), savePath);
                if (!saveDir.exists()) {
                    saveDir.mkdirs(); // 如果目录不存在，创建目录
                }

                File newFile = new File(saveDir, newFileName);
                uploadFile.getFile().renameTo(newFile); // 将文件移动到新的保存路径

                String fileUrl = "/upload/temp/" + savePath + "/" + newFileName; // 构建文件的 URL 路径
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


    public void bangdingrenlist() {
        Integer pageNumber = getParaToInt("page",1);
        String name = getPara("name");
        String telephone = getPara("telephone");
        setAttr("name",name);
        setAttr("telephone",telephone);
        setAttr("bangdingrens", service.searchdata(pageNumber,10,name,telephone));
        render("bangdingrens.html");
    }
    public List<String> getDriverLicenseImageUrls(String Bangdingren_license_image_url) {
        if (Bangdingren_license_image_url == null || Bangdingren_license_image_url.isEmpty()) {
            return Arrays.asList("top-bg1.jpg");  // 返回默认图片URL
        }
        return Arrays.asList(Bangdingren_license_image_url.split(","));
    }



    public void edit() {
        Integer bangdingrenId = getParaToInt();
        Bangdingren bangdingren = service.findById(bangdingrenId);
        if (bangdingren != null) {
            setAttr("bangdingren", bangdingren);
        } else {
            // 处理对象为null的情况，可以进行日志记录或其他处理
            renderError(404); // 例如返回404页面
        }
    }
    public void update() {
        Bangdingren bangdingren = getModel(Bangdingren.class, "bangdingren");
        bangdingren.update();
        redirect("/bangdingren/bangdingrenlist");
    }
    public void add() {
        redirect("/bangdingren/add.html");

    }
    public void save() {
        Bangdingren bangdingren = getModel(Bangdingren.class,"bangdingren");
        bangdingren.save();
        redirect("/bangdingren/bangdingrenlist");
    }
    public class UploadController extends Controller {
        public void index() {
            UploadFile uploadFile = getFile("photo", "/img/top-bg1.jpg"); // 指定上传文件保存的路径
            File file = uploadFile.getFile();
            // 在这里你可以获取上传的文件名，将其保存到数据库中，并返回给前端
            String filename = file.getName();
            renderJson("filename", filename);
        }
    }
    public void delete() {
        System.out.println("bangdingrendelete function");
        service.deleteById(getParaToInt());
        redirect("/bangdingren/bangdingrenlist");
    }
}