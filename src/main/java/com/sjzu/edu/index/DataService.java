package com.sjzu.edu.index;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseData;
import com.sjzu.edu.common.model.base.BaseBseData;

import java.util.List;

public class DataService {
    BseData dao = new BseData().dao();


    public Page<BseData> paginate(int pageNumber, int pageSize, String uptime) {
        String select = "SELECT *";
        String sqlExceptSelect = "FROM bse_data"; // 初始化为表名
        Object[] params = new Object[0];

        if (StrKit.notBlank(uptime)) { // 检查 uptime 是否为空
            sqlExceptSelect = "FROM bse_data WHERE createTime >= ? AND createTime < ?"; // 确保 FROM 子句在正确的位置
            params = new Object[]{uptime + " 00:00:00", uptime + " 23:59:59"};
        }

        sqlExceptSelect += " ORDER BY id DESC"; // 添加排序

        // 打印 SQL 语句和参数，方便调试
        System.out.println("SQL: " + select + " " + sqlExceptSelect);
        System.out.print("Params: ");
        for (Object param : params) {
            System.out.print(param + " ");
        }
        System.out.println();

        return dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, params);
    }
    public void printTableStructure() {
        // 查询表结构的 SQL 语句
        String sql = "SHOW COLUMNS FROM bse_data";

        // 执行查询
        List<Record> columns = Db.find(sql);

        // 输出表结构信息
        System.out.println("表结构 - bse_data:");
        for (Record column : columns) {
            System.out.println("字段名: " + column.get("Field"));
            System.out.println("数据类型: " + column.get("Type"));
            System.out.println("是否允许为空: " + column.get("Null"));
            System.out.println("键: " + column.get("Key"));
            System.out.println("默认值: " + column.get("Default"));
            System.out.println("额外信息: " + column.get("Extra"));
            System.out.println("-----------------------------");
        }
    }

    public void delete(int id) {
        dao.deleteById(id);
    }
}
