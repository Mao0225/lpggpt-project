package com.sjzu.edu.index;

import java.io.File;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Jiebangding;
import com.sjzu.edu.common.model.User;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/jiebangding")
//@Before(UserInterceptor.class) 没用上的解释器
public class JiebangdingController extends Controller {
    //private User dao = new User().dao();
    @Inject
    JiebangdingService service;


    public void jielist() {
        Integer pageNumber = getParaToInt("pageNum",1);
        String qipingbianma = getPara("qipingbianma");
        String unbinder = getPara("unbinder");
        String chengbianma = getPara("chengbianma");
        String startDate = getPara("startDate");
        String endDate = getPara("endDate");
        setAttr("qipingbianma", qipingbianma);
        setAttr("unbinder", unbinder);
        setAttr("chengbianma", chengbianma);
        setAttr("startDate", startDate);
        setAttr("endDate", endDate);
        System.out.println("asdklasdjasjdasodjasjdasdasidjasoidjas"+service.searchJiebangding(pageNumber, 10, qipingbianma,unbinder, chengbianma, startDate, endDate));
        setAttr("jielist", service.searchJiebangding(pageNumber, 10, qipingbianma,unbinder, chengbianma, startDate, endDate));
        render("jiebangdings.html");
    }



}









//
//
//    public void index() {
//        UploadFile file = getFile("fileUpload"); // get uploaded file
//        String fileName = file.getFileName();
//        // put uploaded file to the place you want
//        boolean isMoved = file.getFile().renameTo(new File("D:/lpggpt/src/main/webapp/img/ft.logo.png" + fileName));
//        if (isMoved) {
//            renderText("Upload successfully");
//        } else {
//            renderText("Upload failed");
//        }
//    }
//
//
//
//
//    public void uploadFile() {
//        UploadFile file = getFile("fileUpload"); // get uploaded file
//        String fileName = file.getFileName();
//        // put uploaded file to the place you want
//        boolean isMoved = file.getFile().renameTo(new File("D:/lpggpt/src/main/webapp/img/ft.logo.png" + fileName));
//        if (isMoved) {
//            // store the file path to session
//            getSession().setAttribute("uploadedFile", "/img/ft.logo.png" + fileName);
//            redirect("/jiebangding/displayImage");
//        } else {
//            renderText("Upload failed");
//        }
//    }
//

//    public void displayImage() {
//        // get the file path from session
//        Object filePathObj = getSession().getAttribute("uploadedFile");
//        if (filePathObj != null) {
//            String filePath = filePathObj.toString();
//            render(filePath);
//        } else {
//            renderText("No image to display");
//        }
//    }
//
//    public void add() {
//        render("add.html");
//    }
















