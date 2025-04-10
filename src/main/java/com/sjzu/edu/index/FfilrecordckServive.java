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
        StringBuilder baseSql = new StringBuilder(
                "FROM fill_record_check1 f " +
                        "LEFT JOIN (" +
                        "    SELECT g.* " +
                        "    FROM gas_file g " +
                        "    WHERE g.id = (" +
                        "        SELECT g2.id " +
                        "        FROM gas_file g2 " +
                        "        WHERE g2.gas_number = g.gas_number " +
                        "        ORDER BY g2.id " +
                        "        LIMIT 1 " +
                        "    )" +
                        ") g " +
                        "ON g.gas_number = f.gas_number ");
        String selectSql = "SELECT f.*, g.* ";
        List<Object> params = new ArrayList<>();
        boolean hasCondition = false;

        // 添加 now_gas IS NOT NULL 条件
        baseSql.append(" WHERE now_gas IS NOT NULL ");
        hasCondition = true;

        // 添加 after_filling 和 before_filling 都为合格的条件
        appendCondition(baseSql, hasCondition);
        baseSql.append("f.after_filling = ? AND f.before_filling = ? ");
        params.add("合格");
        params.add("合格");
        hasCondition = true;

        // 如果 finditem 为 null，使用当天日期作为查询条件
        if (finditem == null) {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
            Timestamp endTimestamp = Timestamp.valueOf(endOfDay);

            appendCondition(baseSql, hasCondition);
            baseSql.append("f.fill_time >= ? AND f.fill_time < ? ");
            params.add(startTimestamp);
            params.add(endTimestamp);
            hasCondition = true;
        } else {
            // 处理传入的日期条件
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

        // 处理 companyid 条件
        if (companyid != null && !companyid.isEmpty()) {
            appendCondition(baseSql, hasCondition);
            baseSql.append("f.gasstation = ? ");
            params.add(companyid);
            System.out.println("companyid:" + companyid);
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

