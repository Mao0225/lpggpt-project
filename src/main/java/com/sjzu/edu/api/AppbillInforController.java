package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;


@Path(value = "/", viewPath = "/appbillinfo")
public class AppbillInforController extends Controller {
    public void success_list() {
        int pageNumber = getParaToInt("pageNumber");
        int pageSize = getParaToInt("pageSize");
        String telephone = getPara("telephone");

        System.out.println("telephone: " + telephone);
        try {
            String fromSql = "from bill_info where buyer = " + telephone + " AND jiedanstatus = 1 order by id desc";
            System.out.println("构建的查询SQL: " + fromSql);

            // 修改后的总数查询（添加jiedan_status条件）
            String countSql = "SELECT COUNT(*) AS total FROM bill_info WHERE buyer = " + telephone + " AND jiedanstatus = 1";
            Record totalRecord = Db.use().findFirst(countSql);
            if (totalRecord != null) {
                int total = totalRecord.getInt("total");
                System.out.println("总记录数：" + total);

                // 获取当前页的记录
                Page<Record> records = Db.use().paginate(pageNumber, pageSize, "select *", fromSql);
                System.out.println(records.toString());

                JSONArray jsonArray = new JSONArray();
                List<Record> recordList = records.getList();
                if (recordList != null && !recordList.isEmpty()) {
                    System.out.println("正确进入了if循环");
                    for (Record record : recordList) {
                        JSONObject recordJson = new JSONObject();
                        recordJson.put("number", record.get("number"));
                        recordJson.put("station", record.get("station"));
                        recordJson.put("price", record.get("price"));
                        recordJson.put("driver", record.get("driver"));
                        recordJson.put("jiedanstatus", record.get("jiedanstatus"));
                        recordJson.put("writetime", record.get("writetime"));
                        jsonArray.add(recordJson);
                        System.out.println("正确进入了for循环");
                    }
                } else {
                    System.out.println("查询结果为空");
                    // 如果查询结果为空，返回一个空的JSONArray给前端
                    jsonArray = new JSONArray();
                }

                // 创建一个JSONObject用于返回给前端
                JSONObject json = new JSONObject();
                json.put("total", total);
                json.put("rows", jsonArray);
                // 添加 telephone 信息到返回的 JSON 对象中
                json.put("telephone", telephone);

                // 打印要返回给前端的JSONObject内容
                System.out.println("即将返回给前端的JSON数据：" + json.toJSONString());
                // 将结果返回给前端
                renderJson(json);
            } else {
                System.out.println("totalRecord!= null");
                // 如果没有找到记录，可以返回一个空的JSON对象
                JSONObject json = new JSONObject();
                json.put("total", 0);
                json.put("rows", new ArrayList<>());
                // 添加 telephone 信息到返回的 JSON 对象中
                json.put("telephone", telephone);

                // 将结果返回给前端
                renderJson(json);
            }
        } catch (Exception e) {
            // 这里可以添加更详细的错误处理，比如记录日志
            e.printStackTrace();
            renderError(500);
        }
    }

    public void Notreceive_list() {
        int pageNumber = getParaToInt("pageNumber");
        int pageSize = getParaToInt("pageSize");
        String telephone = getPara("telephone");

        System.out.println("telephone: " + telephone);
        try {
            String fromSql = "from bill_info where buyer = " + telephone + " AND jiedanstatus = 0 order by id desc";
            System.out.println("构建的查询SQL: " + fromSql);

            // 修改后的总数查询（添加jiedan_status条件）
            String countSql = "SELECT COUNT(*) AS total FROM bill_info WHERE buyer = " + telephone + " AND jiedanstatus = 0";
            Record totalRecord = Db.use().findFirst(countSql);
            if (totalRecord != null) {
                int total = totalRecord.getInt("total");
                System.out.println("总记录数：" + total);

                // 获取当前页的记录
                Page<Record> records = Db.use().paginate(pageNumber, pageSize, "select *", fromSql);
                System.out.println(records.toString());

                JSONArray jsonArray = new JSONArray();
                List<Record> recordList = records.getList();
                if (recordList != null && !recordList.isEmpty()) {
                    System.out.println("正确进入了if循环");
                    for (Record record : recordList) {
                        JSONObject recordJson = new JSONObject();
                        recordJson.put("number", record.get("number"));
                        recordJson.put("station", record.get("station"));
                        recordJson.put("price", record.get("price"));
                        recordJson.put("driver", record.get("driver"));
                        recordJson.put("jiedanstatus", record.get("jiedanstatus"));
                        recordJson.put("writetime", record.get("writetime"));
                        jsonArray.add(recordJson);
                        System.out.println("正确进入了for循环");
                    }
                } else {
                    System.out.println("查询结果为空");
                    // 如果查询结果为空，返回一个空的JSONArray给前端
                    jsonArray = new JSONArray();
                }

                // 创建一个JSONObject用于返回给前端
                JSONObject json = new JSONObject();
                json.put("total", total);
                json.put("rows", jsonArray);
                // 添加 telephone 信息到返回的 JSON 对象中
                json.put("telephone", telephone);

                // 打印要返回给前端的JSONObject内容
                System.out.println("即将返回给前端的JSON数据：" + json.toJSONString());
                // 将结果返回给前端
                renderJson(json);
            } else {
                System.out.println("totalRecord!= null");
                // 如果没有找到记录，可以返回一个空的JSON对象
                JSONObject json = new JSONObject();
                json.put("total", 0);
                json.put("rows", new ArrayList<>());
                // 添加 telephone 信息到返回的 JSON 对象中
                json.put("telephone", telephone);

                // 将结果返回给前端
                renderJson(json);
            }
        } catch (Exception e) {
            // 这里可以添加更详细的错误处理，比如记录日志
            e.printStackTrace();
            renderError(500);
        }
    }
    // 在 AppbillInforController 类中添加以下两个方法

    // 获取加气站列表
    public void station_list() {
        try {
            List<Record> stations = Db.use().find("SELECT id, station_name FROM gas_station");
            JSONArray jsonArray = new JSONArray();

            for (Record station : stations) {
                JSONObject item = new JSONObject();
                item.put("id", station.getInt("id"));
                item.put("name", station.getStr("station_name"));
                jsonArray.add(item);
            }

            JSONObject result = new JSONObject();
            result.put("code", 200);
            result.put("data", jsonArray);
            renderJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            renderError(500);
        }
    }

    // 获取司机列表
    public void driver_list() {
        try {
            List<Record> drivers = Db.use().find("SELECT id, drivername FROM drivercar");
            JSONArray jsonArray = new JSONArray();

            for (Record driver : drivers) {
                JSONObject item = new JSONObject();
                item.put("id", driver.getInt("id"));
                item.put("name", driver.getStr("drivername"));
                jsonArray.add(item);
            }

            JSONObject result = new JSONObject();
            result.put("code", 200);
            result.put("data", jsonArray);
            renderJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            renderError(500);
        }
    }

    public void submit() {
        try {
            // 1. 获取原始JSON数据
            String rawData = getRawData();
            System.out.println("接收到的原始数据：" + rawData);

            // 2. 解析JSON
            JSONObject jsonData = JSONObject.parseObject(rawData);

            // 3. 获取参数（注意使用jsonData对象）
            String telephone = jsonData.getString("telephone");
            Integer number = jsonData.getInteger("quantity");  // 注意前端字段名是quantity
            String station = jsonData.getString("station");
            String station_id = jsonData.getString("station_id");
//            String driver = jsonData.getString("driver");
            String gas_specification = jsonData.getString("gas_specification");
            String deliverytime = jsonData.getString("deliverytime");
            String jiedanstatus = jsonData.getString("jiedanstatus");
            String restaurant = jsonData.getString("restaurant");
            Integer restaurantid = jsonData.getInteger("restaurantid");
            String custormer = jsonData.getString("name");
            System.out.println("解析后参数：");
            System.out.println("telephone: " + telephone);
            System.out.println("number: " + number);
            System.out.println("station: " + station);
//            System.out.println("driver: " + driver);
            System.out.println("station: " + station);
            System.out.println("station_id: " + station_id);
            System.out.println("custormer: " + custormer);
            System.out.println("deliverytime: " + deliverytime);
            // 2. 参数校验
            if (telephone == null || telephone.isEmpty()) {
                renderJson(new JSONObject().fluentPut("code", 400).fluentPut("msg", "缺少用户标识"));
                return;
            }
            if (number == null || station == null) {
                renderJson(new JSONObject().fluentPut("code", 400).fluentPut("msg", "缺少必要参数"));
                return;
            }

            // 3. 构建数据库记录
            Record bill = new Record();
            bill.set("buyer", telephone);
            bill.set("number", number);
            bill.set("station", station);
//            bill.set("driver", driver);
            bill.set("gas_specification", gas_specification);
            bill.set("jiedanstatus", jiedanstatus);
            bill.set("restaurant", restaurant);
            bill.set("restaurantid", restaurantid);
            bill.set("expect_time", deliverytime);
            bill.set("station_id", station_id);
            // 4. 保存到数据库
            boolean success = Db.use().save("bill_info", bill);
            System.out.println("success: " + success);
            Number generatedId = bill.getNumber("id"); // 根据实际主键字段名调整
            if (generatedId == null) {
                renderJson(new JSONObject().fluentPut("code", 500).fluentPut("msg", "获取订单ID失败"));
                return;
            }
            // 5. 创建bas_bill记录并设置bid
            Record basBill = new Record();
            // 复制公共字段
            basBill.set("bid", generatedId); // 设置关联ID
            basBill.set("buyer", telephone);
            basBill.set("number", number);
            basBill.set("station", station);
//            basBill.set("driver", driver);
            basBill.set("gas_specification", gas_specification);
            basBill.set("expect_time", deliverytime);
            basBill.set("jiedanstatus", jiedanstatus);
            basBill.set("restaurant", restaurant);
            basBill.set("restaurantid", restaurantid);
            basBill.set("custormer", custormer);
            basBill.set("station_id", station_id);
            boolean success2 = Db.use().save("bas_bill", basBill);
            // 5. 返回响应
            if (success && success2) {
                System.out.println("保存成功!");
                renderJson(new JSONObject().fluentPut("code", 200).fluentPut("msg", "订单创建成功89779"));
            } else {
                renderJson(new JSONObject().fluentPut("code", 500).fluentPut("msg", "订单创建失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(new JSONObject().fluentPut("code", 500).fluentPut("msg", "服务器异常"));
        }
    }

    public void Success_Search_Data() {
        int pageNum = getParaToInt("pageNum");
        int pageSize = getParaToInt("pageSize");
        String station = getPara("station");
        String buyer = getPara("buyer");
        String riqi = getPara("riqi");

        try {
            // 构建动态 WHERE 条件（参数化查询）
            StringBuilder whereSql = new StringBuilder();
            List<Object> params = new ArrayList<>();

            whereSql.append("WHERE buyer = ? AND jiedanstatus = 1 ");
            params.add(buyer); // buyer 参数

            if (station != null && !station.isEmpty()) {
                whereSql.append("AND station LIKE ? ");
                params.add("%" + station + "%"); // station 模糊匹配
            }

            if (riqi != null && !riqi.isEmpty()) {
                whereSql.append("AND writetime >= ? AND writetime < ? ");
                params.add(riqi + " 00:00:00"); // 开始时间
                params.add(riqi + " 23:59:59"); // 结束时间
            }

            // 获取总记录数（使用相同条件）
            String countSql = "SELECT COUNT(*) AS total FROM bill_info " + whereSql;
            Record totalRecord = Db.use().findFirst(countSql, params.toArray());
            int total = totalRecord != null ? totalRecord.getInt("total") : 0;

            // 构建分页查询（正确拆分 SELECT 和 FROM）
            String selectPart = "SELECT *";
            String fromWhereSql = "FROM bill_info " + whereSql + " ORDER BY id DESC";

            // 执行分页查询（参数化）
            Page<Record> records = Db.use().paginate(pageNum, pageSize, selectPart, fromWhereSql, params.toArray());
            System.out.println("total1: " + total);
            // 构建返回的 JSON
            JSONObject json = new JSONObject();
            json.put("total", total);
            json.put("rows", convertRecordsToJson(records.getList()));
            renderJson(json);

        } catch (Exception e) {
            e.printStackTrace();
            renderError(500);
        }
    }


    public void Wait_Search_Data() {
        int pageNum = getParaToInt("pageNum");
        int pageSize = getParaToInt("pageSize");
        String station = getPara("station");
        String buyer = getPara("buyer");
        String riqi = getPara("riqi");

        try {
            // 构建动态 WHERE 条件（带参数化查询）
            StringBuilder whereSql = new StringBuilder();
            List<Object> params = new ArrayList<>();

            whereSql.append("WHERE buyer = ? AND jiedanstatus = 0 ");
            params.add(buyer);

            if (station != null && !station.isEmpty()) {
                whereSql.append("AND station LIKE ? ");
                params.add("%" + station + "%");
            }

            if (riqi != null && !riqi.isEmpty()) {
                whereSql.append("AND writetime >= ? AND writetime < ? ");
                params.add(riqi + " 00:00:00");
                params.add(riqi + " 23:59:59");
            }

            // 获取总记录数（必须与分页查询条件一致）
            String countSql = "SELECT COUNT(*) AS total FROM bill_info " + whereSql;
            Record totalRecord = Db.use().findFirst(countSql, params.toArray());
            int total = totalRecord != null ? totalRecord.getInt("total") : 0;

            // 构建分页查询（关键修改点）
            String selectPart = "SELECT * "; // SELECT 子句
            String fromWhereSql = "FROM bill_info " + whereSql + " ORDER BY id DESC"; // FROM 及后续子句

            // 调用正确的 paginate 方法
            Page<Record> records = Db.use().paginate(
                    pageNum,
                    pageSize,
                    selectPart,
                    fromWhereSql,
                    params.toArray()
            );
            System.out.println("total2: " + total);
            // 构建返回的JSON
            JSONObject json = new JSONObject();
            json.put("total", total);
            json.put("rows", convertRecordsToJson(records.getList()));
            renderJson(json);

        } catch (Exception e) {
            e.printStackTrace();
            renderError(500);
        }
    }

    // 辅助方法：Record列表转JSONArray
    private JSONArray convertRecordsToJson(List<Record> records) {
        JSONArray jsonArray = new JSONArray();
        for (Record record : records) {
            JSONObject json = new JSONObject();
            json.put("number", record.get("number"));
            json.put("station", record.get("station"));
            json.put("price", record.get("price"));
            json.put("driver", record.get("driver"));
            json.put("jiedanstatus", record.get("jiedanstatus"));
            json.put("writetime", record.get("writetime"));
            jsonArray.add(json);
        }
        return jsonArray;
    }

}


