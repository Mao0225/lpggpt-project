package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.sjzu.edu.common.model.GasFile;
import com.sjzu.edu.common.model.GasStation;

import java.util.List;

public class GGasFileController extends Controller {
    @Inject
    GGasFileService service;
    private GasStation gasstation= new GasStation().dao();

    public void gasFilelist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        String gasNumber = getPara("gasNumber");
        String valve_body_code = getPara("valve_body_code");
        String filling_specification = getPara("filling_specification");
        setAttr("gasNumber",gasNumber);
        setAttr("valve_body_code",valve_body_code);
        setAttr("filling_specification",filling_specification);
        setAttr("gasFiles", service.search(pageNumber, 10,gasNumber,valve_body_code,filling_specification,getSessionAttr("stationid")));
        // 渲染搜索结果页面
        render("GGasFiles.html");
    }

    public void edit() {
        List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station WHERE id="+getSessionAttr("stationid") +"");
        setAttr("gasstations", gasstations);

        setAttr("gas_file", service.findById(getParaToInt()));
    }

    public void add() {
        List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station WHERE id="+getSessionAttr("stationid") +"");
        setAttr("gasstations", gasstations);
        System.out.println("gasstations.size()"+gasstations.size());
        render("add.html");
    }

    public void save() {
        try {
        GasFile gasfile = getModel(GasFile.class, "gas_file");
        String gasNumber = gasfile.getGasNumber();
        int stationid = gasfile.getStationid();
        System.out.println("接收新增气瓶档案：气瓶编号=" + gasNumber);

            // 2. 校验气瓶编号是否重复
            boolean isDuplicate = service.isGasNumberDuplicate(gasNumber,stationid);
            if (isDuplicate) {
                // 2.1 重复：返回错误提示（跳转回新增页面并携带错误信息）
                System.err.println("新增失败：气瓶编号[" + gasNumber + "]已存在！");
                // 回显新增页面的加气站列表（保持页面数据）
                List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
                setAttr("gasstations", gasstations);
                // 携带错误信息和已填写的表单数据（回显）
                setAttr("errorMsg", "新增失败：气瓶编号「" + gasNumber + "」已存在，请更换！");
                setAttr("gas_file", gasfile); // 回显用户已填写的内容
                render("add.html"); // 渲染新增页面
                return;
            }

            // 3. 不重复：正常保存并跳转列表页
            System.out.println("气瓶编号[" + gasNumber + "]未重复，执行保存");
            gasfile.save();
            redirect("/gasFile/gasFilelist"); // 保存成功跳转到列表页
        } catch (Exception e) {
            // 异常处理：打印日志并返回错误提示
            System.err.println("新增气瓶档案异常：" + e.getMessage());
            e.printStackTrace();
            // 回显新增页面
            List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
            setAttr("gasstations", gasstations);
            setAttr("errorMsg", "新增失败：系统异常，请稍后重试！");
            redirect("/GGasFile/gasFilelist");

        }
    }

    public void update() {
        GasFile gasFile = getModel(GasFile.class, "gas_file");
        gasFile.update();
        redirect("/GGasFile/gasFilelist");
    }

    public void delete() {
        service.delete(getParaToInt());
        redirect("/GGasFile/gasFilelist");
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
        setAttr("gasFiles", service.search(pageNumber, 10,gasNumber,valve_body_code,filling_specification,getSessionAttr("stationid")));
        // 渲染搜索结果页面
        render("GGasFiles.html");


    }

}
