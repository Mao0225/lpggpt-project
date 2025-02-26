package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseEquipOperation;
import com.sjzu.edu.common.model.BseEquipment;
import com.sjzu.edu.common.model.BseGun;
import com.sjzu.edu.common.model.GasStation;

import java.util.List;

public class MgGunController extends Controller {
    MgGunService MgGunService = new MgGunService();
    private BseGun bsegun = new BseGun().dao();
    private GasStation gastation = new GasStation().dao();
    public void gunlist(){
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10);
        Page<Record> BseGunPage=MgGunService.paginate(pageNumber,pageSize);
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("gunlist",BseGunPage);
        setAttr("gastations", gastations);
        render("gunlist.html");
    }
    public void searchgun(){
        Integer companyId=getParaToInt("companyId");
        System.out.println("companyId； "+companyId);
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10);
        Page<Record> BseGunPage=MgGunService.search(pageNumber,pageSize,companyId);
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("gastations", gastations);
        setAttr("gunlist", BseGunPage);
        render("maequipmentlist.html");
    }
    public void add(){
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("gastations", gastations);
        render("add.html");
    }
    public void save() {
        BseGun gun = getModel(BseGun.class, "gun");
        if (gun.save()) {
            // 保存成功，可以重定向到设备列表页面或者显示成功消息
            redirect("/managegun/gunlist");
        }
    }
    public void edit() {
        Integer id = getParaToInt("id");
        Record record = MgGunService.findgun(id);
        System.out.println("record: "+record);
        Record stationRecord = MgGunService.getStationNameByGunId(id);
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("gastations", gastations);
        setAttr("record", record);
        setAttr("stationRecord", stationRecord);
        render("edit.html");

    }
    public void update() {
        BseGun gun = getModel(BseGun.class, "gun");
        if (gun.update()) {
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/managegun/gunlist");
        }
    }
    public void delete() {
        Integer gunId = getParaToInt("id"); // 明确指定获取名为id的参数转换为整型，获取可能为null
        if (gunId!= null) {
            MgGunService.deleteById(gunId);
            redirect("/managegun/gunlist");
        } else {
            // 处理参数为空的情况，比如返回错误提示信息给前端，告知缺少必要的设备id参数
            renderError(400, "缺少设备id参数，无法执行删除操作");
        }
    }
    }
