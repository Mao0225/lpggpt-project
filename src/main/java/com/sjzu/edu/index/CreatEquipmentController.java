package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseMaintenance;
import com.sjzu.edu.common.model.BseEquipment;
import com.sjzu.edu.common.model.Custromer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path(value = "/", viewPath = "/equipment") // 根据实际的视图路径进行修改

public class CreatEquipmentController extends Controller {


    @Inject
    CreatEquipmentService service;
    private BseEquipment dao = new BseEquipment().dao();

    /**
     * 显示维修记录列表页面，整合搜索和翻页功能
     */

    public void CElist() {

        // 获取session中的stationid
        Integer stationid = getSessionAttr("stationid");
        System.out.println(stationid);
        // 调用Service层方法查询数据
        List<Record> CEmain= service.getData(stationid);
        for (Record record : CEmain) {
            System.out.println(record);
        }

        // 将数据传递到前端页面
        setAttr("CEmain", CEmain);

        // 渲染JSP页面
        render("createquipment.html");
    }
    public void maintenanceList() {


            int pageNumber = getParaToInt("page", 1); // 获取当前页码，默认第1页
            int pageSize = 10; // 每页显示记录数，可按需调整

            // 从会话等合适地方获取stationid，这里假设通过getSessionAttr获取，根据实际调整
            Integer stationid = getSessionAttr("stationid");

            // 获取各种搜索条件参数
            String equipmentName = getPara("equipmentName");
            String kind = getPara("kind");
            String startDate = getPara("startDate");
            String endDate = getPara("endDate");
            String step = getPara("step");
            setAttr("equipmentName",equipmentName);
            setAttr("kind",kind);
            setAttr("startDate",startDate);
            setAttr("endDate",endDate);
            setAttr("step",step);


            // 调用服务层方法进行带条件的分页查询
            Page<Record> maintenances = service.searchMaintenanceList(pageNumber, pageSize, stationid, equipmentName, kind, startDate, endDate, step);
            setAttr("maintenances", maintenances);
            render("equipment.html");

    }

    /**
     * 编辑功能页面，展示对应记录的可修改和不可修改字段等信息
     */
    public void edit() {

            Integer id = getParaToInt();
            System.out.println(id);
            BseMaintenance maintenance = service.findById(id);

            if (maintenance!= null) {
                String equipment = findEquipmentByMaintenanceId(id);
                setAttr("maintenance", maintenance);
                setAttr("equipment", equipment);
                render("edit.html");
            } else {
                renderError(404);
            }

    }

    /**
     * 更新功能，按照要求只更新部分字段
     */

    public void update() {
        getBean(BseMaintenance.class).update();
        redirect("/Cequipment/maintenanceList");
    }

    /**
     * 添加功能页面，准备添加记录时需要的一些下拉选项等数据（比如设备列表等）
     */
    public void add() {
        try {
            List<BseEquipment> equipments = getAvailableEquipmentList();
            setAttr("equipments", equipments);
            setAttr("companyid",getSessionAttr("stationid"));
            render("add.html");
        } catch (Exception e) {
            e.printStackTrace();
            renderError(500);
        }
    }

    /**
     * 保存功能，保存新添加的维修记录等信息
     */

    public void save() {
        BseMaintenance maintenance = getModel(BseMaintenance.class, "bseMaintenance");
        System.out.println(maintenance);
        maintenance.save();
        // 保存成功，可以重定向到列表页面或者显示成功消息
        redirect("/Cequipment/maintenanceList");

    }
    public void equipmentdetail()
    {
        Integer id = getParaToInt("id");
        System.out.println("id: "+id);
        if (id!= null) {
            // 调用业务层方法获取 Record 类型的设备详细信息数据
            Record record = service.searchequipmentdetial(id);
            if (record!= null) {
                // 将 Record 对象设置到请求属性中，方便后续视图层获取数据渲染展示
                setAttr("equipmentdetail", record);

                // 调用业务层方法获取与该id对应的bse_fitt_operate表中的相关记录列表
                List<Record> fittOperateRecords = service.getFittOperateRecordsByMid(id);
                if (fittOperateRecords!= null &&!fittOperateRecords.isEmpty()) {
                    // 将获取到的列表数据也设置到请求属性中，方便前端获取
                    setAttr("fittOperateRecords", fittOperateRecords);
                }

                // 渲染对应的视图页面（这里假设视图页面名为 "equipmentdetail.html"，你可根据实际情况修改）
                render("equipmentdetail.html");
            } else {
                // 如果没查询到对应设备详细信息数据，返回提示信息
                renderText("未查询到对应设备的详细信息");
            }
        } else {
            // 如果参数获取失败，返回错误提示信息
            renderText("参数错误，未获取到有效的 equipmentid");
        }
    }

    /**
     * 删除功能，调用服务层方法删除对应维修记录
     */

    public void delete() {
        System.out.println("delete function");
        service.deleteById(getParaToInt());
        redirect("/Cequipment/maintenanceList");
    }

    // 辅助方法，根据维修记录ID获取对应的设备信息，可按需完善关联查询逻辑
    private BseEquipment getEquipmentByMaintenanceId(int id) {
        return dao.findFirst("SELECT e.* FROM bse_equipment e JOIN bse_maintenance m ON m.companyid = e.companyid AND m.equipmentid = e.id WHERE m.id =?", id);
    }
    private String findEquipmentByMaintenanceId(int id) {
        Record record = dao.findFirst("SELECT e.name FROM bse_equipment e JOIN bse_maintenance m ON m.companyid = e.companyid AND m.equipmentid = e.id WHERE m.id =? ", id).toRecord();
        if (record!= null) {
            return record.getStr("name"); // 使用getStr方法从Record对象中获取String类型的name字段值
        }
        return null;
    }

    // 辅助方法，获取可用的设备列表，可根据实际需求扩展逻辑（如筛选条件等）
    private List<BseEquipment> getAvailableEquipmentList() {
        return dao.find("SELECT * FROM bse_equipment");
    }
}