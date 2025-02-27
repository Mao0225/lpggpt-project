package com.sjzu.edu.index;


import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;


import com.sjzu.edu.common.model.GasFile;
import com.sjzu.edu.common.model.GasStation;
import com.sjzu.edu.common.model.Restaurant;

import java.util.List;


@Path(value = "/", viewPath = "/gasFile")
public class GasfileController extends Controller {

    @Inject
    GasfileService service;
    private GasStation gasstation= new GasStation().dao();

    public void gasFilelist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        String gasNumber = getPara("gasNumber");
        String valve_body_code = getPara("valve_body_code");
        String filling_specification = getPara("filling_specification");
        setAttr("gasNumber",gasNumber);
        setAttr("valve_body_code",valve_body_code);
        setAttr("filling_specification",filling_specification);
        setAttr("gasFiles", service.search(pageNumber, 10,gasNumber,valve_body_code,filling_specification));
        // 渲染搜索结果页面
        render("gasFiles.html");
    }

    public void edit() {
        List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
        setAttr("gasstations", gasstations);

        setAttr("gas_file", service.findById(getParaToInt()));
    }

    public void add() {
        List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
        setAttr("gasstations", gasstations);
        System.out.println("gasstations.size()"+gasstations.size());
        render("add.html");
    }

    public void save() {
        GasFile gasfile = getModel(GasFile.class, "gas_file");
        gasfile.save();
        redirect("/gasFile/gasFilelist");
    }

    public void update() {
        GasFile gasFile = getModel(GasFile.class, "gas_file");
        gasFile.update();
        redirect("/gasFile/gasFilelist");
    }

    public void delete() {
        service.delete(getParaToInt());
        redirect("/gasFile/gasFilelist");
    }

    public void search() {
       // String gasNumber = getPara("gasNumber");
        // 获取分页参数，如果不存在则使用默认值
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        String gasNumber = getPara("gasNumber");
        String valve_body_code = getPara("valve_body_code");
        String filling_specification = getPara("filling_specification");
        setAttr("gasNumber",gasNumber);
        setAttr("valve_body_code",valve_body_code);
        setAttr("filling_specification",filling_specification);
        setAttr("gasFiles", service.search(pageNumber, 10,gasNumber,valve_body_code,filling_specification));
        // 渲染搜索结果页面
        render("gasFiles.html");


    }

}





