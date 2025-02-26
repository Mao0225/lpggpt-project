package com.sjzu.edu.index;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Custromer;
import com.sjzu.edu.common.model.Station;
import com.sjzu.edu.common.model.BseEquipOperation;

import java.util.List;
@Path(value = "/", viewPath = "/Eequipment")
public class EquipmentoperateController extends  Controller{
    @Inject
    EquipmentoperateService service;
    public void Eequipmentlist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String factory = getPara("factory");
        String model = getPara("model");
        String name = getPara("name");
        String type = getPara("type");



        setAttr("factory", factory);
        setAttr("model", model);
        setAttr("name", name);
        setAttr("type", type);



        setAttr("eequipment",service.search(pageNumber, pageSize,factory,model,name,type,getSessionAttr("stationid")));
        render("Eequipment.html");
    }



}
