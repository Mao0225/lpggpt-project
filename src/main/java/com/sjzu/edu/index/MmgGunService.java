package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseGun;

import java.util.Collections;

public class MmgGunService {
    private BseGun dao = new BseGun().dao();
    public Page<Record> paginate(int pageNumber, int pageSize, Integer stationid) {
        try {
            // 构建多表连接查询的 SQL 语句
            String select = "SELECT bg.*, gs.station_name ";
            StringBuilder from = new StringBuilder(" FROM bse_gun bg LEFT JOIN gas_station gs ON bg.stationid = gs.id");

            // 如果 stationid 不为空，添加 WHERE 子句进行筛选
            if (stationid != null) {
                from.append(" WHERE bg.stationid = ").append(stationid);
            }

            from.append(" ORDER BY bg.id DESC");

            // 使用 Db.paginate 进行分页查询
            Page<Record> page = Db.paginate(pageNumber, pageSize, select, from.toString());

            return page;
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<>(Collections.emptyList(), 0, pageSize, pageNumber, 0);
        }
    }

    public Page<Record> search(int pageNumber, int pageSize, Integer stationid) {  // 参数id改为包装类型Integer，方便处理可能为null的情况
        String selectFields = "select *";
        StringBuilder sqlBuilder = new StringBuilder("from bse_gun");

        // 构建动态查询条件，添加根据companyid与传入id匹配的筛选条件
        if (stationid != null) {
            sqlBuilder.append(" WHERE stationid = ").append(stationid);  // 精确匹配companyid与传入的id相等
            // 如果需要模糊匹配，可以使用如下方式（根据实际需求选择）
            // sqlBuilder.append(" WHERE companyid LIKE '%").append(id).append("%'");
        }
        sqlBuilder.append(" ORDER BY id DESC");
        String sql = sqlBuilder.toString();
        System.out.println(sql);
        return Db.paginate(pageNumber, pageSize, selectFields, sql);
    }

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
