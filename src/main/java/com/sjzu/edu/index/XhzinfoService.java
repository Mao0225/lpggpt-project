package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.stat.ast.Set;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class XhzinfoService {

    // 定义时间格式化工具（线程安全处理）
    private static final ThreadLocal<SimpleDateFormat> sdfThreadLocal = ThreadLocal.withInitial(() ->
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    );

    /**
     * 分页查询 t_iot_sync_rds_records_v3 表数据，并转换时间戳为可读格式
     *
     * @param pageNumber 当前页码
     * @param pageSize   每页记录数
     * @param xiaoheziid 设备名称查询条件
     * @param alarm      报警状态查询条件
     * @return 分页结果集（包含转换后的时间）
     */
    public Page<Record> paginate(int pageNumber, int pageSize, String xiaoheziid, Integer alarm) {
        // 基础查询语句
        String select = "SELECT *";
        StringBuilder sqlExceptSelect = new StringBuilder("FROM t_iot_sync_rds_records_v3 WHERE 1=1");

        // 动态条件（参数化查询防SQL注入）
        List<Object> params = new ArrayList<>();
        if (xiaoheziid != null && !xiaoheziid.trim().isEmpty()) {
            sqlExceptSelect.append(" AND devicename = ?");
            params.add(xiaoheziid);
        }
        if (alarm != null) {
            sqlExceptSelect.append(" AND alarm = ?");
            params.add(alarm);
        }

        // 按时间倒序排序
        sqlExceptSelect.append(" ORDER BY created_time DESC");

        // 执行分页查询
        Page<Record> recordPage = Db.use("lpg").paginate(
                pageNumber,
                pageSize,
                select,
                sqlExceptSelect.toString(),
                params.toArray()
        );

        // 转换时间戳为可读格式（核心处理，添加打印日志）
        List<Record> recordList = recordPage.getList();
        SimpleDateFormat sdf = sdfThreadLocal.get(); // 获取线程安全的格式化工具
        for (Record record : recordList) {
            // 获取原始时间戳
            Long timestamp = record.getLong("created_time");
            // 打印转换前的时间戳
            System.out.println("转换前（created_time）: " + timestamp + "（类型：" + (timestamp == null ? "null" : "Long") + "）");

            if (timestamp != null) {
                // 处理时间戳（判断是否为秒级，10位数字）
                long timeInMillis = timestamp;
                if (String.valueOf(timestamp).length() == 10) {
                    timeInMillis = timestamp * 1000; // 秒级转毫秒级
                    System.out.println("检测到秒级时间戳，转换为毫秒级：" + timeInMillis);
                }

                // 格式化时间
                String formattedTime = sdf.format(new Date(timeInMillis));
                record.set("formatted_created_time", formattedTime);
                // 打印转换后的时间
                System.out.println("转换后（formatted_created_time）: " + formattedTime);
            } else {
                record.set("formatted_created_time", "-");
                System.out.println("转换后（formatted_created_time）: -（原始时间戳为null）");
            }
        }
        sdfThreadLocal.remove(); // 清除线程局部变量，避免内存泄漏


//        System.out.println("\n===== 本次查询的所有记录详情 =====");
//        int index = 1;
//        for (Record record : recordList) {
//            System.out.println("\n----- 第 " + index + " 条记录 -----");
//            // 获取记录中所有字段名（String[]数组类型）
//            String[] columnNames = record.getColumnNames();
//            // 遍历字段名数组
//            for (String column : columnNames) {
//                // 输出字段名和对应的值
//                System.out.println(column + ": " + record.get(column));
//            }
//            index++;
//        }
//        System.out.println("\n===== 记录打印结束 =====");

        return recordPage;
    }

    public void deleteById(Integer id) {
        Db.use("lpg").deleteById("t_iot_sync_rds_records_v3", "id", id);
    }
}