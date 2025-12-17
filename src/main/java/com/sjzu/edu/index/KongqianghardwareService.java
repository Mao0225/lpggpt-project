package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Kongqianghardware;

import java.util.Collections;

public class KongqianghardwareService {
    private Kongqianghardware dao = new Kongqianghardware().dao();

    // 将参数从Integer stationId改为String stationName，支持模糊查询
    public Page<Record> paginate(int pageNumber, int pageSize, String stationName) {
        try {
            // 基础查询语句
            StringBuilder sqlBuilder = new StringBuilder()
                    .append("SELECT kh.*, gs.station_name ")
                    .append("FROM kongqianghardware kh ")
                    .append("LEFT JOIN gas_station gs ON kh.stationid = gs.id ");

            // 动态添加查询条件，支持按名称模糊查询
            if (stationName != null && !stationName.isEmpty()) {
                sqlBuilder.append("WHERE gs.station_name LIKE ? ");
            }

            // 统一排序条件
            sqlBuilder.append("ORDER BY kh.id DESC");

            // 执行参数化查询
            return stationName != null && !stationName.isEmpty() ?
                    Db.paginate(pageNumber, pageSize,
                            "SELECT kh.*, gs.station_name",
                            sqlBuilder.toString().replace("SELECT kh.*, gs.station_name", ""),
                            "%" + stationName + "%") :  // 添加模糊查询通配符
                    Db.paginate(pageNumber, pageSize,
                            "SELECT kh.*, gs.station_name",
                            sqlBuilder.toString().replace("SELECT kh.*, gs.station_name", ""));

        } catch (Exception e) {
            e.printStackTrace();
            return new Page<>(Collections.emptyList(), 0, pageSize, pageNumber, 0);
        }
    }

    public Record findById(int id) {
        try {
            if (id <= 0) {
                return null;
            }

            String sql = "SELECT * FROM kongqianghardware WHERE id = ?";
            return Db.findFirst(sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Record getStationNameByHardwareId(int id) {
        try {
            if (id <= 0) {
                return null;
            }

            // 1. 在 kongqianghardware 表中查找对应的 stationid
            String hardwareSql = "SELECT stationid FROM kongqianghardware WHERE id = ?";
            Record hardwareRecord = Db.findFirst(hardwareSql, id);

            if (hardwareRecord == null) {
                return null;
            }

            Integer stationId = hardwareRecord.getInt("stationid");

            if (stationId == null) {
                return null;
            }

            // 2. 在 gas_station 表中查找对应的 station_name
            String stationSql = "SELECT station_name FROM gas_station WHERE id = ?";
            Record stationRecord = Db.findFirst(stationSql, stationId);

            // 3. 返回结果
            if (stationRecord != null) {
                Record result = new Record();
                result.set("station_name", stationRecord.get("station_name"));
                return result;
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}