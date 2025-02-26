package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseFittOperate;
import com.sjzu.edu.common.model.BseFitting;
import com.sjzu.edu.common.model.BseInterval;
import com.sjzu.edu.common.model.BseMaintenance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaintenanceController extends Controller {
    private MaintenanceService maintenanceService = new MaintenanceService();
    private BseFitting bsefitting = new BseFitting().dao();

    public void maintenancelist() {
        // 从请求中获取参数
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10);
        String step = getPara("step");
        System.out.println("step: " + step);
        String equipmentname = getPara("equipmentname");
        String maintenancename = getPara("maintenancename");
        String kind = getPara("kind");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        System.out.println("equipmentname: " + equipmentname);
        System.out.println("maintenancename: " + maintenancename);
        System.out.println("kind: " + kind);
        System.out.println(" startTime: " + startTime);
        System.out.println(" endTime: " + endTime);
        setAttr("equipmentname", equipmentname);
        setAttr("maintenancename", maintenancename);
        setAttr("kind", kind);
        setAttr("startTime", startTime);
        setAttr("endTime", endTime);
        // 调用服务层的方法进行搜索
        Page<BseMaintenance> BseMaintenancePage = maintenanceService.paginate(pageNumber, pageSize, step, equipmentname, maintenancename, kind, startTime, endTime);

        // 将结果设置为属性
        setAttr("maintenancelist", BseMaintenancePage);

        // 渲染页面（这里假设对应的页面是equipment.html，你需要根据实际情况修改）
        render("maintenance.html");
    }

    public void equipmentinfo() {
        Integer equipmentId = getParaToInt("id");
        System.out.println("equipmentId: " + equipmentId);
        if (equipmentId != null) {

            Record equipmentRecord = maintenanceService.findByIdWithStationName(equipmentId);
            if (equipmentRecord != null) {
                // 将查询到的数据记录设置为属性，以便传递给前端页面，这里属性名假设为maintenanceInfo，你可以按需修改
                setAttr("equipmentInfo", equipmentRecord);
            } else {
                // 如果没有查询到对应的数据，可以返回相应的提示信息给前端，比如数据不存在等提示
                renderText("未查询到对应的数据记录，请检查参数是否正确");
            }
        } else {
            // 如果参数有空值，返回错误提示信息给前端，告知缺少必要参数
            renderError(400, "缺少必要的参数（equipmentid或companyid），无法执行查询操作");
        }
    }

    public void complete() {
        Integer maintenanceId = getParaToInt("id");
        System.out.println("equipmentId: " + maintenanceId);
        setAttr("key", maintenanceId);
        if (maintenanceId != null) {

            Record maintenceRecord = maintenanceService.findRelatedInfoById(maintenanceId);
            Record equipmentRecord = maintenanceService. getEquipmentRecordById(maintenanceId);
            if (maintenceRecord != null) {
                // 将查询到的数据记录设置为属性，以便传递给前端页面，这里属性名假设为maintenanceInfo，你可以按需修改
                setAttr("maintenceRecordInfo", maintenceRecord);
                System.out.println("maintenceRecord: "+maintenceRecord);
                // 将maintenanceId也添加到maintenceRecordInfo对象中，属性名可自定义，比如'maintenanceId'，这里假设前端通过这个属性名获取该值
                setAttr("maintenanceId", maintenanceId);
                System.out.println("maintenanceId: "+maintenanceId);
                setAttr("equipmentRecordInfo", equipmentRecord);
                render("complete.html");
            } else {
                // 如果没有查询到对应的数据，可以返回相应的提示信息给前端，比如数据不存在等提示
                renderText("未查询到对应的数据记录，请检查参数是否正确");
            }
        } else {
            // 如果参数有空值，返回错误提示信息给前端，告知缺少必要参数
            renderError(400, "缺少必要的参数（equipmentid或companyid），无法执行查询操作");
        }
    }
    public void completes(Integer mid) {
        Integer maintenanceId = mid;
        System.out.println("equipmentId: " + maintenanceId);
        if (maintenanceId != null) {

            Record maintenceRecord = maintenanceService.findRelatedInfoById(maintenanceId);
            Record equipmentRecord = maintenanceService.getEquipmentRecordById(maintenanceId);
            if (maintenceRecord != null) {
                // 将查询到的数据记录设置为属性，以便传递给前端页面，这里属性名假设为maintenanceInfo，你可以按需修改
                setAttr("maintenceRecordInfo", maintenceRecord);
                setAttr("equipmentRecordInfo", equipmentRecord);
                System.out.println("maintenceRecord: "+maintenceRecord);
                // 将maintenanceId也添加到maintenceRecordInfo对象中，属性名可自定义，比如'maintenanceId'，这里假设前端通过这个属性名获取该值
                setAttr("maintenanceId", maintenanceId);
                setAttr("key", mid);
                System.out.println("maintenanceId: "+maintenanceId);
                render("complete.html");
            } else {
                // 如果没有查询到对应的数据，可以返回相应的提示信息给前端，比如数据不存在等提示
                renderText("未查询到对应的数据记录，请检查参数是否正确");
            }
        } else {
            // 如果参数有空值，返回错误提示信息给前端，告知缺少必要参数
            renderError(400, "缺少必要的参数（equipmentid或companyid），无法执行查询操作");
        }
    }

    public void save() {

        getBean(BseMaintenance.class).update();
       redirect("/maintenance/maintenancelist");
    }

    public void add() {
        Integer companyId = getSessionAttr("stationid");
        String writer = getSessionAttr("username");
        Integer equipmentId = getParaToInt("equipmentid");
        System.out.println("equipmentId: " + equipmentId);
        List<BseFitting> bsefittings = bsefitting.find("SELECT * FROM bse_fitting WHERE companyid =?", companyId);
        System.out.println("bsefittings: " + bsefittings);
        setAttr("bsefittings", bsefittings);
        setAttr("companyId", companyId);
        setAttr("writer", writer);
        setAttr("key", getPara("key"));
        setAttr("equipmentId", equipmentId);
        render("add.html");
    }
    public void  repairinfo(){
        Integer maintenanceId = getParaToInt("id");
        System.out.println("maintenanceId: " + maintenanceId);
        Integer equipmentId= getParaToInt("eid");
        System.out.println("equipmentId: " + equipmentId);
        Record record =maintenanceService.repairinfo(maintenanceId,equipmentId);
        if (record != null) {
            setAttr("Info",record);
            render("repairinfo.html");
        } else {
            renderError(404, "未找到相关维护信息");
        }

    }
    public void saveFitting() {
        // 从请求参数中获取并封装BseFitting对象的数据
        BseFittOperate fitting = getModel(BseFittOperate.class, "fitting");
        System.out.println(fitting);

        // 获取后端传递过来的key（对应bse_maintenance表的Id）
        Integer key = getParaToInt("key");
        // 获取前端传过来的fid参数
        Integer newFid = getParaToInt("fid");
        System.out.println("key: " + key);
        System.out.println("newFid: " + newFid);

        boolean fittingSaved = false;
        try {
            if (fitting.save()) {
                fittingSaved = true;
                // 通过key去查找bse_maintenance表中对应的记录，并更新其fid字段
                int updateResult = Db.update("UPDATE bse_maintenance SET fid =? WHERE Id =?", newFid, key);
                if (updateResult < 1) {
                    System.out.println("更新bse_maintenance表中fid字段失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常时，构建一个包含错误信息的JSON格式数据并返回给前端
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("errorMessage", "保存配件数据时出现错误，请检查输入数据及相关配置");
            renderJson(errorMap);
            return;
        }

        if (fittingSaved) {
            // 保存成功时，构建一个包含成功信息的JSON格式数据并返回给前端
            Map<String, Object> successMap = new HashMap<>();
            successMap.put("success", true);
            renderJson(successMap);
            redirect("/maintenance/complete");
        } else {
            // 保存失败时，构建一个包含失败信息的JSON格式数据并返回给前端
            Map<String, Object> failureMap = new HashMap<>();
            failureMap.put("success", false);
            failureMap.put("errorMessage", "配件数据保存失败，请检查相关数据后重试");
            renderJson(failureMap);
        }
        completes(getParaToInt("key"));
    }
}
