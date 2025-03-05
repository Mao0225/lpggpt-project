package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.FillRecordCheck1;
import com.sjzu.edu.common.model.Restaurant;
import com.sjzu.edu.common.model.GasStation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Timestamp;

public class FilrecordckServive {
    private FillRecordCheck1 dao = new FillRecordCheck1().dao();
    private GasStation daos = new GasStation().dao();

    public Page<FillRecordCheck1> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from fill_record_check1 order by id asc");
    }

    public FillRecordCheck1 findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }

    public List<GasStation> pagdetail() {
        return daos.find("select * from gas_station order by id asc");
    }

    public Page<FillRecordCheck1> search(int pageNumber, int pageSize, Timestamp finditem, String gastion, String gasnumber) {
        StringBuilder baseSql = new StringBuilder(
                "FROM fill_record_check1 f " +
                        "LEFT JOIN gas_file g " +
                        "ON g.gas_number = f.gas_number ");
        String selectSql = "SELECT f.*, g.* ";
        List<Object> params = new ArrayList<>();
        boolean hasCondition = false;

        // 添加 now_gas IS NOT NULL 条件
        baseSql.append("WHERE now_gas IS NOT NULL ");
        hasCondition = true;

        // 添加 after_filling 和 before_filling 都为合格的条件
        appendCondition(baseSql, hasCondition);
        baseSql.append("f.after_filling = ? AND f.before_filling = ? ");
        params.add("合格");
        params.add("合格");
        hasCondition = true;

        // 处理日期条件
        if (finditem != null) {
            appendCondition(baseSql, hasCondition);
            baseSql.append("f.fill_time >= ? AND f.fill_time < ? ");
            params.add(finditem);
            params.add(new Timestamp(finditem.getTime() + 86400000));
            hasCondition = true;
        }

        // 处理加气站条件
        if (gastion != null && !gastion.isEmpty()) {
            appendCondition(baseSql, hasCondition);
            baseSql.append("f.gasstation LIKE ? ");
            params.add("%" + gastion + "%");
            hasCondition = true;
        }

        // 处理气瓶编号条件
        if (gasnumber != null && !gasnumber.isEmpty()) {
            appendCondition(baseSql, hasCondition);
            baseSql.append("f.gas_number LIKE ? ");
            params.add("%" + gasnumber + "%");
            hasCondition = true;
        }

        // 添加排序
        baseSql.append("ORDER BY f.id DESC");

        String finalSql = selectSql + baseSql.toString();
        System.out.println("Final SQL: " + finalSql);

        // 使用参数化查询分页（需根据框架调整）
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