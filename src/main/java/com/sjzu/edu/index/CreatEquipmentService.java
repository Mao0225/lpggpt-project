package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseMaintenance;
import com.sjzu.edu.common.model.BseEquipment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreatEquipmentService {

    private BseMaintenance maintenanceDao = new BseMaintenance().dao();
    private BseEquipment equipmentDao = new BseEquipment().dao();
    public List<Record> getData(Integer stationid) {
        try {
            List<Record> resultList = Db.find(
                    "SELECT m.kind, m.reportday,m.repairday, m.reporter, m.content, m.repairer, m.phone, m.process, m.remark, " +
                            "e.name AS equipment_name, e.model, e.buyday, e.location, e.factory, " +
                            "f.name AS fitting_name " +
                            "FROM bse_maintenance m " +
                            "LEFT JOIN bse_equipment e ON m.companyid = e.companyid AND m.equipmentid = e.id " +
                            "LEFT JOIN bse_fitting f ON m.companyid  = f.companyid  AND m.fid = f.id " +  // 修改此处连接条件，直接关联
                            "WHERE m.companyid =? ORDER BY m.id DESC", stationid
            );
            if (resultList == null) {
                // 如果查询结果为null（有些情况可能返回null，虽然一般返回空列表），返回空列表
                for (Record record : resultList) {
                    System.out.println(record);
                }
                System.out.println("空");
                return Collections.emptyList();
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            // 出现SQL异常时也返回空列表，避免向外抛出异常影响上层调用逻辑
            return Collections.emptyList();
        }
    }

    // 获取维修记录列表，支持分页和多条件搜索
    public Page<Record> searchMaintenanceList(int pageNumber, int pageSize, Integer stationid, String equipmentName, String kind, String startDate, String endDate, String step) {
        // 构建查询 SQL 语句，关联 bse_maintenance 和 bse_equipment 表
        StringBuilder sql = new StringBuilder("SELECT m.id ,m.kind, m.reportday, m.reporter, m.content, m.repairer, m.writetime, m.step,m.repairday,m.equipmentid, e.name AS equipment_name ");
        StringBuilder sqlExceptSelect = new StringBuilder("FROM bse_maintenance m LEFT JOIN bse_equipment e ON m.companyid = e.companyid AND m.equipmentid = e.id WHERE m.companyid = ? ");

        // 构建查询参数列表
        List<Object> params = new ArrayList<>();
        params.add(stationid); // stationid 参数

        // 动态添加查询条件
        if (equipmentName != null && !equipmentName.trim().isEmpty()) {
            sqlExceptSelect.append(" AND e.name LIKE ?");
            params.add("%" + equipmentName + "%");
        }

        if (kind != null) {
            sqlExceptSelect.append(" AND m.kind = ?");
            params.add(kind);
        }

        if (startDate != null && !startDate.trim().isEmpty()) {
            sqlExceptSelect.append(" AND m.reportday >= ?");
            params.add(startDate + " 00:00:00");
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            sqlExceptSelect.append(" AND m.reportday <= ?");
            params.add(endDate + " 23:59:59");
        }

        if (step != null) {
            sqlExceptSelect.append(" AND m.step = ?");
            params.add(step);
        }

        // 执行分页查询，传入 select 和 sqlExceptSelect 部分，并将参数转化为数组
        return Db.paginate(pageNumber, pageSize, sql.toString(), sqlExceptSelect.toString(), params.toArray());
    }

    // 删除维修记录
    public boolean deleteMaintenanceRecord(int id) {
        // 根据 ID 删除记录
        int result = Db.delete("DELETE FROM bse_maintenance WHERE id = ?", id);
        return result > 0;
    }
    public Record searchequipmentdetial(int id) {
        // 构建 SQL 语句，通过关联 bse_maintenance 和 bse_equipment 表查询所需字段
        String sql = "SELECT m.id,m.kind, m.reportday, m.reporter, m.content, m.repairer, m.writetime, m.step,m.repairday,m.equipmentid, m.repairer,m.phone,m.process,e.model,e.useday,e.location,e.factory,e.name AS equipment_name " +
                "FROM bse_maintenance m LEFT JOIN bse_equipment e ON m.equipmentid = e.id WHERE m.id =?";
        // 使用 Db.findFirst 方法执行查询，获取符合条件的第一条记录（因为期望返回一个整合后的记录，这里假设根据传入id查询出来是唯一对应的记录情况，如果实际可能有多条需调整逻辑）
        Record record = Db.findFirst(sql, id);
        return record;
    }

    public List<Record> getFittOperateRecordsByMid(int id) {
        try {
            // 构建查询 SQL 语句，从bse_fitt_operate表中查询对应Mid的记录，选择name和count字段
            String sql = "SELECT name, count FROM bse_fitt_operate WHERE Mid =?";
            // 执行查询，获取符合条件的所有记录列表，这里使用Db.find方法，因为可能有多条符合条件的记录
            List<Record> resultList = Db.find(sql, id);
            if (resultList == null) {
                // 如果查询结果为null（有些情况可能返回null，虽然一般返回空列表），返回空列表
                return Collections.emptyList();
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            // 出现SQL异常等情况时，返回空列表，避免向外抛出异常影响上层调用逻辑
            return Collections.emptyList();
        }
    }

    public void deleteById(int id) {
        maintenanceDao.deleteById(id);
    }

    // 根据ID查询单个维修记录
    public BseMaintenance findById(int id) {
        return maintenanceDao.findById(id);
    }
}
