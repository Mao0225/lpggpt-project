package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.Drivercar;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Path(value = "/", viewPath = "/driver")
public class DriverController extends Controller {
    @Inject
    DriverService service;

    public void driverlist(){
        System.out.println("driver_list function");
        System.out.println(service.paginate(getParaToInt(0, 1), 10).toString());
        setAttr("drivers",service.paginate(getParaToInt(0,1),10));
        render("drivers.html");
    }
    public void edit(){
        setAttr("driver",service.findByid(getParaToInt()));
//        render("edit.html");
    }


    public void add(){
        redirect("add.html");
    }

    public void save(){
        Drivercar d = getModel(Drivercar.class,"driver");
        boolean save = d.save();
        if(save){
            redirect("/driver/driverlist");
        }else{
        }
    }

    public void update(){
        Drivercar driver = getModel(Drivercar.class, "driver");
        if(driver.update()) {
            redirect("/driver/driverlist");
        } else {

        }
    }

    public void upload(){
        UploadFile uploadfile = getFile("file");
        if (uploadfile != null) {
            File file = uploadfile.getFile();
            String localFileName = file.getName();
            System.out.println("Local file name: " + localFileName);
            renderJson("url", localFileName);
        } else {
            renderError(400);
        }
    }

    public void searchdata(){
        String drivername = getPara("drivername");
        String driverphone = getPara("driverphone");
        String drivercarno = getPara("drivercarno");
        System.out.println(service.search(1, 10,drivername,driverphone,drivercarno));
        setAttr("drivers",service.search(1, 10,drivername,driverphone,drivercarno));
        render("drivers.html");
    }

        public void delete () {
            service.deleteByid(getParaToInt());
            redirect("/driver/driverlist");
        }
    }
