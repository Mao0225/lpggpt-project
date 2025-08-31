package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * App端小盒子信息数据Controller
 */
@Path(value = "/", viewPath = "/appxhzinfo")
@Clear
public class AppXhzinfoController extends Controller {

    /**
     * App端获取小盒子数据列表
     * 对应Web端的xhzlist方法
     * http://localhost:8099/appxhzinfo/listAll
     */
    public void listAll() {
        addCorsHeaders();

        try {
            System.out.println("=== App端小盒子数据查询开始 ===");

            // 获取分页参数
            int pageNumber = getParaToInt("page", 1);
            int pageSize = getParaToInt("size", 10);

            // 获取查询条件参数
            String xiaohezi = getPara("companyId");  // 保持与Web端相同的参数名
            Integer alarm = getParaToInt("alarm");

            System.out.println("查询参数 - xiaohezi: " + xiaohezi + ", alarm: " + alarm);
            System.out.println("分页参数 - pageNumber: " + pageNumber + ", pageSize: " + pageSize);

            // 构建查询SQL - 与Service层逻辑完全一致
            String select = "SELECT *";
            StringBuilder sqlExceptSelect = new StringBuilder("FROM t_iot_sync_rds_records_v3 WHERE 1=1");

            // 动态添加查询条件
            if (xiaohezi != null && !xiaohezi.trim().isEmpty()) {
                System.out.println("添加xiaohezi查询条件: " + xiaohezi);
                sqlExceptSelect.append(" AND devicename = '").append(xiaohezi).append("'");
            }

            if (alarm != null) {
                System.out.println("添加alarm查询条件: " + alarm);
                sqlExceptSelect.append(" AND alarm = ").append(alarm);
            }

            // 添加排序条件（按时间倒序）
            sqlExceptSelect.append(" ORDER BY id DESC");

            String finalSql = sqlExceptSelect.toString();
            System.out.println("最终SQL: " + finalSql);

            // 执行查询（使用lpg数据源）
            Page<Record> recordPage = Db.use("lpg").paginate(
                    pageNumber,
                    pageSize,
                    select,
                    finalSql
            );

            // 查询餐厅信息 - 与Web端保持一致
            List<Record> restaurants = Db.find("SELECT * FROM restaurant");

            // 格式化时间戳 - 为每条记录添加格式化后的时间
            List<Record> recordList = recordPage.getList();
            for (Record record : recordList) {
                String createdTime = record.getStr("created_time");
                if (createdTime != null) {
                    String formattedTime = convertTimestamp(createdTime);
                    record.set("formatted_time", formattedTime);
                }
            }

            System.out.println("查询结果数量: " + recordList.size());
            System.out.println("总页数: " + recordPage.getTotalPage());
            System.out.println("总记录数: " + recordPage.getTotalRow());

            // 构建返回的JSON对象
            JSONObject json = new JSONObject();
            json.put("flag", "200");
            json.put("message", "查询成功");
            json.put("recordlist", recordPage);
            json.put("restaurants", restaurants);
            json.put("pageNumber", recordPage.getPageNumber());
            json.put("pageSize", recordPage.getPageSize());
            json.put("totalPage", recordPage.getTotalPage());
            json.put("totalRow", recordPage.getTotalRow());

//            // 调试输出
//            recordList.forEach(record -> {
//                System.out.println("ID: " + record.get("id"));
//                System.out.println("DeviceName: " + record.get("devicename"));
//                System.out.println("Alarm: " + record.get("alarm"));
//                System.out.println("CreatedTime: " + record.get("created_time"));
//                System.out.println("FormattedTime: " + record.get("formatted_time"));
//                System.out.println("------");
//            });

            renderJson(json);

        } catch (Exception e) {
            System.err.println("App端小盒子数据查询异常: " + e.getMessage());
            e.printStackTrace();

            JSONObject json = new JSONObject();
            json.put("flag", "300");
            json.put("message", "查询失败: " + e.getMessage());
            json.put("data", null);

            renderJson(json);
        }
    }

    /**
     * 时间戳转换方法
     */
    public String convertTimestamp(String timestampStr) {
        try {
            long timestamp = Long.parseLong(timestampStr);
            Instant instant = Instant.ofEpochMilli(timestamp);
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Shanghai")); // 指定时区
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return zonedDateTime.format(formatter);
        } catch (NumberFormatException e) {
            return "无效时间戳";
        }
    }

    /**
     * 测试接口
     */
    public void test() {
        addCorsHeaders();
        JSONObject json = new JSONObject();
        json.put("flag", "200");
        json.put("message", "AppXhzinfoController工作正常");
        renderJson(json);
    }

    /**
     * 添加跨域请求头
     */
    public void addCorsHeaders() {
        // 获取 HttpServletResponse 对象
        HttpServletResponse response = getResponse();
        // 设置允许跨域的来源
        String origin = getHeader("Origin");
        if (StrKit.notBlank(origin)) {
            // 设置允许跨域的方法
            ((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            // 设置允许跨域的头
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
            // 设置允许跨域的最大缓存时间（单位：秒）
            response.setHeader("Access-Control-Max-Age", "3600");
            // 设置允许跨域的来源
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        renderNull();
    }
}