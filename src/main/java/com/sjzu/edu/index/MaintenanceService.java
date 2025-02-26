package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseFitting;
import com.sjzu.edu.common.model.BseMaintenance;

import java.util.Collections;
import java.util.List;

public class MaintenanceService {
    private BseMaintenance dao = new BseMaintenance().dao();
    private BseFitting fdao = new BseFitting().dao();

    public Page<BseMaintenance> paginate(int pageNumber, int pageSize, String step, String equipmentname, String maintenancename, String kind, String startTime, String endTime) {
        String addsql = "FROM bse_equipment r right join bse_maintenance t ON (r.id = t.equipmentid) ";
        int cout = 0;
        if (step!= null &&!step.isEmpty()) {
            addsql += (cout == 0? " WHERE " : " AND ") + "t.step LIKE '%" + step + "%' ";
            cout++;
        }

        if (equipmentname!= null &&!equipmentname.isEmpty()) {
            addsql += (cout == 0? " WHERE " : " AND ") + "r.name LIKE '%" + equipmentname + "%' ";
            cout++;
        }

        if (maintenancename!= null &&!maintenancename.isEmpty()) {
            addsql += (cout == 0? " WHERE " : " AND ") + "t.name LIKE '%" + maintenancename + "%' ";
            cout++;
        }

        if (kind!= null &&!kind.isEmpty()) {
            addsql += (cout == 0? " WHERE " : " AND ") + "t.kind LIKE '%" + kind + "%' ";
            cout++;
        }

        if (startTime!= null &&!startTime.isEmpty()) {
            addsql += (cout == 0? " WHERE " : " AND ") + "t.reportday >= '" + startTime + " 00:00:00' ";
            cout++;
        }

        if (endTime!= null &&!endTime.isEmpty()) {
            addsql += (cout == 0? " WHERE " : " AND ") + "t.reportday <= '" + endTime + " 23:59:59' ";
            cout++;
        }
        // 添加按照t表的id字段倒序排列的语句，使用ORDER BY关键字，DESC表示倒序
        addsql += " ORDER BY t.id DESC ";
        System.out.println(addsql);

        // 使用paginate方法执行分页查询，并返回Page<Record>类型结果
        return dao.paginate(pageNumber, pageSize,"SELECT r.name as ename ,r.id as eid,t.*",addsql);
    }
    // 新增的根据id查询并关联获取stationname的函数
    public Record findByIdWithStationName(int id) {
        // 先从bse_equipment表中查询对应id的记录
        Record equipmentRecord = Db.findFirst("SELECT * FROM bse_equipment WHERE id =?", id);
        if (equipmentRecord!= null) {
            // 获取station.id
            Integer stationId = equipmentRecord.getInt("companyid");
            System.out.println("stationId: "+stationId);
            if (stationId!= null) {
                // 根据station.id从gas_station表中查询stationname
                Record stationRecord = Db.findFirst("SELECT station_name FROM gas_station WHERE id =?", stationId);
                if (stationRecord!= null) {
                    // 将查询到的stationname添加到equipmentRecord中
                    equipmentRecord.set("station_name", stationRecord.getStr("station_name"));
                    System.out.println("设置后的stationname值为: " + equipmentRecord.getStr("stationname"));
                }
            }
        }
        return equipmentRecord;
    }

public Record findRelatedInfoById(int id) {
    // 先从bse_maintenance表中查询对应id的记录，获取其name字段以及equipmentid字段，同时获取id字段
    Record maintenanceRecord = Db.findFirst("SELECT name, equipmentid, id,content FROM bse_maintenance WHERE id =?", id);
    if (maintenanceRecord!= null) {
        Integer equipmentId = maintenanceRecord.getInt("equipmentid");
        if (equipmentId!= null) {
            // 根据获取到的equipmentid从bse_equipment表中查询对应的name、location、model、factory等字段
            Record equipmentRecord = Db.findFirst("SELECT name, location, model, factory FROM bse_equipment WHERE id =?", equipmentId);
            if (equipmentRecord!= null) {
                // 将bse_maintenance表中的name字段添加到equipmentRecord中，你可以根据实际需求调整字段名避免冲突等情况
                equipmentRecord.set("equipment_name", maintenanceRecord.getStr("name"));
                // 将bse_maintenance表中的id字段添加到equipmentRecord中，这里添加的字段名为maintenance_id（可根据需求修改）
                equipmentRecord.set("maintenance_id", maintenanceRecord.get("id"));
                equipmentRecord.set("content", maintenanceRecord.get("content"));
                return equipmentRecord;
            }
        }
    }
    return null;
}
    // 从bse_maintenance表中查询对应id的记录，并获取equipmentid字段值
    public Record getEquipmentRecordById(int id) {
        return Db.findFirst("SELECT name, equipmentid, id, content FROM bse_maintenance WHERE id =?", id);
    }

    public List<BseFitting> findAllFittingNames() {
        String sql = "SELECT * FROM bse_fitting";
        return fdao.find(sql);
    }
    public Record repairinfo(Integer mid, Integer eid) {
        try {
            Record result = Db.findFirst(
                    "SELECT " +
                            "m.kind, m.repairer, m.repairday, m.content, m.process, " +
                            "e.name AS equipment_name, " +
                            "f.name AS fitting_name, f.id as fitting_id " +  // 添加配件的id
                            "FROM bse_maintenance m " +
                            "LEFT JOIN bse_equipment e ON m.equipmentid = e.id " +
                            "LEFT JOIN bse_fitting f ON m.fid = f.id AND m.companyid = f.companyid "+ // 使用mid和companyid关联
                            "WHERE m.id = ? AND m.equipmentid = ? ORDER BY m.id DESC",
                    mid,eid
            );
            if (result == null) {
                // 如果查询结果为null，返回空列表
                System.out.println("空");
                return null;
            }
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // 出现SQL异常时也返回空列表，避免向外抛出异常影响上层调用逻辑
            return null;
        }
    }
}
