package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseXiaohezi;

public class InstallController  extends Controller {
    InstallService service = new InstallService();
    public void installlist(){
        int pageNumber = getParaToInt("page",1);
        int pageSize = getParaToInt("pageSzie",10);
        String xiaohezibianma= getPara("xiaohezibianma");
        String time =  getPara("time");
        String restaurantname = getPara("restaurantname");
        // 修改后
        Page<Record> xiaoheziRecord = service.paginate(pageNumber, pageSize, xiaohezibianma, time,restaurantname);
        setAttr("time",time);
        setAttr("xiaohezibianma",xiaohezibianma);
        setAttr("restaurantname",restaurantname);
        render("install.html");
        setAttr("xiaohezi",xiaoheziRecord);

    }
    public void edit(){
        int id = getParaToInt("id");
        BseXiaohezi xiaohezi=service.findById(id);
        System.out.println(xiaohezi);
        setAttr("xiaohezidata",xiaohezi);
    }

    public void update(){
        BseXiaohezi xiaohezi = getModel(BseXiaohezi.class,"xiaohezidata");
        if(xiaohezi.update())
        { System.out.println("333366");
            redirect("/install/installlist");
        }else{
            System.out.println("333367");
        }
    }

    public void delete(){
        int id = getParaToInt("id");
        service.deleteById(id);
        redirect("/install/installlist");
    }

}
