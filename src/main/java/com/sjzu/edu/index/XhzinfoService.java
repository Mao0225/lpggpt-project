package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class XhzinfoService {

    /**
     * 分页查询 t_iot_sync_rds_records_v3 表数据
     *
     * @param pageNumber 当前页码
     * @param pageSize   每页记录数
     * @param xiaohezi   设备名称查询条件
     * @return 分页结果集
     */
    public Page<Record> paginate(int pageNumber, int pageSize, String xiaohezi,Integer alarm) {
        // 基础查询语句
        String select = "SELECT *";
        StringBuilder sqlExceptSelect = new StringBuilder("FROM t_iot_sync_rds_records_v3 WHERE 1=1");

        // 动态添加查询条件
        if (xiaohezi != null && !xiaohezi.trim().isEmpty()) {
            System.out.println(xiaohezi);
            sqlExceptSelect.append(" AND devicename = '").append(xiaohezi).append("'");
        }
        if (alarm != null) {
            System.out.println(xiaohezi);
            sqlExceptSelect.append(" AND alarm = ").append(alarm);
        }

        // 添加排序条件（按时间倒序）
        sqlExceptSelect.append(" ORDER BY id DESC");

        // 执行查询（使用 lpg 数据源）
        return Db.use("lpg").paginate(
                pageNumber,
                pageSize,
                select,
                sqlExceptSelect.toString()
        );
    }
    public void deleteById(Integer id) {
        Db.use("lpg").deleteById(" t_iot_sync_rds_records_v3", "id", id);
    }
}

