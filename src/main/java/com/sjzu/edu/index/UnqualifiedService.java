package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.FillRecordCheck1;

public class UnqualifiedService {
    FillRecordCheck1 dao = new FillRecordCheck1().dao();

    public Page<FillRecordCheck1> paginate(int pageNumber, int pageSize, String bottle_no, String fill_time, String gas_no) {
        // 构建 SQL 查询语句的基础部分，添加表连接
        StringBuilder addsql = new StringBuilder("FROM fill_record_check1 frc JOIN gas_file gf ON frc.gas_number = gf.gas_number");

        int conditionCount = 0;

        // 添加筛选条件：after_filling 或 before_filling 字段为不合格
        addsql.append(" WHERE (frc.after_filling = '不合格' OR frc.before_filling = '不合格')");
        conditionCount++;

        // 处理 bottle_no 参数（作为时间字段处理）
        if (bottle_no != null && !bottle_no.isEmpty()) {
            addsql.append(" AND ");
            addsql.append("frc.gas_number = '").append(bottle_no).append("'");
            conditionCount++;
        }

        // 处理 fill_time 参数（精确查询）
        if (fill_time != null && !fill_time.isEmpty()) {
            addsql.append(" AND ");
            addsql.append("frc.fill_time >= '").append(fill_time).append(" 00:00:00' AND frc.fill_time < '").append(fill_time).append(" 23:59:59'");
            conditionCount++;
        }

        // 处理 gas_no 参数（精确查询）
        if (gas_no != null && !gas_no.isEmpty()) {
            addsql.append(" AND ");
            addsql.append("frc.gun_no = '").append(gas_no).append("'");
            conditionCount++;
        }

        // 按 id 降序排序
        addsql.append(" ORDER BY frc.id DESC");

        System.out.println("addsql: " + addsql);

        // 执行分页查询
        return dao.paginate(pageNumber, pageSize, "SELECT frc.*,gf.*", addsql.toString());
    }

    public void deleteById(Integer id) {
        dao.deleteById(id);
    }
}