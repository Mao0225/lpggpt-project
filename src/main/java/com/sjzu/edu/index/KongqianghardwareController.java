package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.GasStation;
import com.sjzu.edu.common.model.Kongqianghardware;

import java.util.List;

public class KongqianghardwareController extends Controller {
    KongqianghardwareService kongqianghardwareService = new KongqianghardwareService();
    private Kongqianghardware kongqianghardware = new Kongqianghardware().dao();
    private GasStation gastation = new GasStation().dao();

    public void list(){
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);
        // 将stationId改为stationName，接收名称关键字
        String stationName = getPara("stationName");
        Page<Record> kongqianghardwarePage = kongqianghardwareService.paginate(pageNumber, pageSize, stationName);
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("hardwareList", kongqianghardwarePage);
        setAttr("gastations", gastations);
        // 回显搜索条件
        setAttr("stationName", stationName);
        render("kongqianghardwarelist.html");
    }

    public void add(){
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("gastations", gastations);
        render("add.html");
    }

    public void save() {
        Kongqianghardware hardware = getModel(Kongqianghardware.class, "hardware");
        // 设置关联的加气站名称
        if (hardware.getStationid() != null) {
            GasStation station = gastation.findById(hardware.getStationid());
            if (station != null) {
                hardware.setStationname(station.getStationName());
            }
        }
        if (hardware.save()) {
            redirect("/kongqianghardware/list");
        }
    }

    public void edit() {
        Integer id = getParaToInt("id");
        Record record = kongqianghardwareService.findById(id);
        Record stationRecord = kongqianghardwareService.getStationNameByHardwareId(id);
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("gastations", gastations);
        setAttr("record", record);
        setAttr("stationRecord", stationRecord);
        render("edit.html");
    }

    public void update() {
        Kongqianghardware hardware = getModel(Kongqianghardware.class, "hardware");
        // 更新关联的加气站名称
        if (hardware.getStationid() != null) {
            GasStation station = gastation.findById(hardware.getStationid());
            if (station != null) {
                hardware.setStationname(station.getStationName());
            }
        }
        if (hardware.update()) {
            redirect("/kongqianghardware/list");
        }
    }

    public void delete() {
        Integer id = getParaToInt("id");
        if (id != null) {
            kongqianghardwareService.deleteById(id);
            redirect("/kongqianghardware/list");
        } else {
            renderError(400, "缺少ID参数，无法执行删除操作");
        }
    }
}