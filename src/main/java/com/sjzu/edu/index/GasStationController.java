package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.GasStation;

@Path(value = "/", viewPath = "/GasStation")
public class GasStationController extends Controller {

    @Inject
    GasStationService service;

    public void GasStationList() {
        String searchKey = getPara("searchKey");
        if ("null".equalsIgnoreCase(searchKey)){
            searchKey=null;
        }
        String pageNum1 = getPara("pageNum");
        Integer pageNum =1;
        if (pageNum1!=null&&pageNum1.length()!=0){
            pageNum = Integer.parseInt(pageNum1);
        }

        setAttr("searchKey",searchKey);
        setAttr("GasStation", service.paginate(getParaToInt(0, 1), 10,searchKey));
        render("GasStation.html");
    }

    public void edit() {
        setAttr("GasStation", service.findById(getParaToInt()));
    }

    public void update() {
        getBean(GasStation.class).update();
        redirect("/GasStation/GasStationList");
    }

    public void add() {
        redirect("add.html");

    }

    public void save() {
        getBean(GasStation.class).save();
        redirect("/GasStation/GasStationList");
    }

    public void delete() {
        service.deleteById(getParaToInt());
        redirect("/GasStation/GasStationList");
    }
}
