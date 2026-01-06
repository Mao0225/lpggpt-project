package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.FillRecordCheck1;
import com.sjzu.edu.common.model.GasStation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
public class FfilrecordckServive {
    private FillRecordCheck1 dao = new FillRecordCheck1().dao();
    private GasStation daos = new GasStation().dao();

    public Page<FillRecordCheck1> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from fill_record_check1 order by id DESC");
    }

    public FillRecordCheck1 findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }

    public List<GasStation> pagdetail(String companyid) {
        // 如果 companyid 不为空，构建带筛选条件的 SQL 查询语句
        if (companyid != null && !companyid.isEmpty()) {
            String sql = "select * from gas_station where station_id = ? order by id asc";
            return daos.find(sql, companyid);
        }
        // 如果 companyid 为空，执行原有的查询语句
        return daos.find("select * from gas_station order by id asc");
    }


    public Page<FillRecordCheck1> search(int pageNumber, int pageSize, Timestamp finditem, String gastion, String gasnumber, String companyid) {
        // 核心修改1：简化LEFT JOIN，直接关联gas_file（无重复gas_number，移除子查询）
        StringBuilder baseSql = new StringBuilder(
                "FROM fill_record_check1 f " +
                        "LEFT JOIN gas_file g ON f.gas_number = g.gas_number ");

        // 核心修改2：指定查询g的字段（含terminate_use_date，filing_gas_station别名gasstation）
        String selectSql = "SELECT f.*, g.filling_medium, g.filling_specification, g.filing_gas_station as gasstation, g.terminate_use_date ";

        List<Object> params = new ArrayList<>();
        boolean hasCondition = false;

        // 1. 保留原有条件：add_gas_long 不为空
        baseSql.append(" WHERE f.add_gas_long IS NOT NULL ");
        hasCondition = true;

        // 2. 保留原有条件：now_gas 不为空（补充f.别名，避免字段歧义）
        baseSql.append(" AND f.now_gas IS NOT NULL ");

        // 3. 保留原有条件：after_filling 和 before_filling 都为合格
        baseSql.append(" AND f.after_filling = ? AND f.before_filling = ? ");
        params.add("合格");
        params.add("合格");
        hasCondition = true;

        // 处理日期条件（finditem为null时默认查当天）—— 逻辑完全保留
        if (finditem == null) {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
            Timestamp endTimestamp = Timestamp.valueOf(endOfDay);

            baseSql.append(" AND f.fill_time >= ? AND f.fill_time < ? ");
            params.add(startTimestamp);
            params.add(endTimestamp);
        } else {
            baseSql.append(" AND f.fill_time >= ? AND f.fill_time < ? ");
            params.add(finditem);
            params.add(new Timestamp(finditem.getTime() + 86400000));
        }

        // 处理加气站条件 —— 逻辑完全保留
        if (gastion != null && !gastion.isEmpty()) {
            baseSql.append(" AND f.gasstation LIKE ? ");
            params.add("%" + gastion + "%");
        }

        // 处理气瓶编号条件 —— 逻辑完全保留
        if (gasnumber != null && !gasnumber.isEmpty()) {
            baseSql.append(" AND f.gas_number LIKE ? ");
            params.add("%" + gasnumber + "%");
        }

        // 处理 companyid 条件 —— 逻辑完全保留（含打印日志）
        if (companyid != null && !companyid.isEmpty()) {
            baseSql.append(" AND f.gasstation = ? ");
            params.add(companyid);
            System.out.println("companyid:" + companyid);
        }

        // 添加排序 —— 逻辑完全保留（按f.id降序）
        baseSql.append(" ORDER BY f.id DESC");

        // 打印最终SQL（便于调试）
        String finalSql = selectSql + baseSql.toString();
        System.out.println("Final SQL: " + finalSql);

        // 分页查询（参数、分页逻辑完全保留）
        return dao.paginate(pageNumber, pageSize, selectSql, baseSql.toString(), params.toArray());
    }

    // 辅助方法：添加 AND 或 WHERE 条件
    private void appendCondition(StringBuilder sql, boolean hasCondition) {
        if (hasCondition) {
            sql.append("AND ");
        } else {
            sql.append("WHERE ");
        }
    }
}

