package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Bangdingren;
import com.sjzu.edu.common.model.FillRecordCheck1;
import com.sjzu.edu.common.model.GasStation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FilrecordckServive {
    private FillRecordCheck1 dao = new FillRecordCheck1().dao();
    private GasStation daos = new GasStation().dao();

    // 分页查询充装记录

    public Page<FillRecordCheck1> search(int pageNumber, int pageSize, Timestamp finditem, String gastion, String gasnumber) {
        // 核心修改1：简化LEFT JOIN，直接关联gas_file（无重复gas_number，无需子查询）
        StringBuilder baseSql = new StringBuilder(
                "FROM fill_record_check1 f " +
                        "LEFT JOIN gas_file g ON f.gas_number = g.gas_number ");

        // 核心修改2：指定查询g的字段，且filing_gas_station别名gasstation
        String selectSql = "SELECT f.*, g.filling_medium, g.filling_specification, g.filing_gas_station as gasstation, g.terminate_use_date  ";

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

        // 处理 companyid 条件（保留原有注释占位）
        // 如需添加可在此处补充

        // 添加排序 —— 逻辑完全保留（按f.id降序）
        baseSql.append(" ORDER BY f.id DESC");

        // 打印最终SQL（便于调试）
        String finalSql = selectSql + baseSql.toString();
        System.out.println("Final SQL: " + finalSql);

        // 分页查询（参数、分页逻辑完全保留）
        return dao.paginate(pageNumber, pageSize, selectSql, baseSql.toString(), params.toArray());
    }

    // 查询所有加气站
    public List<GasStation> pagdetail() {
        List<GasStation> list = daos.find("SELECT * FROM gas_station");
        return list != null ? list : new ArrayList<>();
    }

    public FillRecordCheck1 findById(Integer id) {
        return dao.findById(id);
    }

    public void deleteById(Integer id) {
        dao.deleteById(id);
    }


}
