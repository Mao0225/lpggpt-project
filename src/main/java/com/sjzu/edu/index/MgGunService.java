package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.sjzu.edu.common.model.BseGun;

import java.util.Collections;

public class MgGunService {
    private BseGun dao = new BseGun().dao();

    public Page<Record> paginate(int pageNumber, int pageSize, Integer stationId) {
        try {
            // 基础查询语句
            StringBuilder sqlBuilder = new StringBuilder()
                    .append("SELECT bg.*, gs.station_name ")
                    .append("FROM bse_gun bg ")
                    .append("LEFT JOIN gas_station gs ON bg.stationid = gs.id ");

            // 动态添加查询条件
            if (stationId != null) {
                sqlBuilder.append("WHERE bg.stationid = ? ");
            }

            // 统一排序条件
            sqlBuilder.append("ORDER BY bg.id DESC");

            // 执行参数化查询
            return stationId != null ?
                    Db.paginate(pageNumber, pageSize,
                            "SELECT bg.*, gs.station_name", // select 部分
                            sqlBuilder.toString().replace("SELECT bg.*, gs.station_name", ""), // from 部分
                            stationId) : // 参数
                    Db.paginate(pageNumber, pageSize,
                            "SELECT bg.*, gs.station_name",
                            sqlBuilder.toString().replace("SELECT bg.*, gs.station_name", ""));

        } catch (Exception e) {
            e.printStackTrace();
            return new Page<>(Collections.emptyList(), 0, pageSize, pageNumber, 0);
        }
    }


    // 修改后的方法，返回 Record
    public Record findgun(int id) {
        try {
            if (id <= 0) {
                return null; // 返回 null
            }

            // 构建查询 SQL 语句，查询所有字段
            String sql = "SELECT * FROM bse_gun WHERE id = ?";

            // 执行查询，获取符合条件的单条记录
            Record record = Db.findFirst(sql, id);

            // 返回查询结果,可能为null
            return record;
        } catch (Exception e) {
            e.printStackTrace();
            // 出现SQL异常等情况时，返回null，避免向外抛出异常影响上层调用逻辑
            return null;
        }
    }
    public Record getStationNameByGunId(int id) {
        try {
            if (id <= 0) {
                return null;
            }

            // 1. 在 bse_gun 表中查找对应的 stationid
            String gunSql = "SELECT stationid FROM bse_gun WHERE id = ?";
            Record gunRecord = Db.findFirst(gunSql, id);

            if (gunRecord == null) {
                return null; // 如果没有找到对应的 gun 记录，返回 null
            }

            Integer stationId = gunRecord.getInt("stationid");

            if(stationId == null){
                return null; // 如果 stationid 为空，返回null
            }

            // 2. 在 gas_station 表中查找对应的 station_name
            String stationSql = "SELECT station_name FROM gas_station WHERE id = ?";
            Record stationRecord = Db.findFirst(stationSql, stationId);

            // 3. 将 gas_station 的 station_name 保存到record对象中并返回
            if(stationRecord!=null){
                Record result = new Record();
                result.set("station_name", stationRecord.get("station_name"));
                return result;
            }
            // 如果没有查询到station名称，返回null
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