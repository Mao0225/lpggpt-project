package com.sjzu.edu.api;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.PressureGaugeRecords;
import com.sjzu.edu.common.model.Restaurant;
import com.sjzu.edu.common.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/applogin2")
//@Before(IndexInterceptor.class)
@Clear
public class Applogin2Controller extends Controller {
    private User dao = new User().dao();
    private Restaurant restaurantdao = new Restaurant().dao();
    private PressureGaugeRecords pressuregaugedao = new PressureGaugeRecords().dao();

    // 测试接口地址：http://localhost:8099/applogin/login  参数：username：13591402086  password：1
    public void login() {
        String telephone = getPara("telephone");
        String password = getPara("password");
        System.out.println("hello jason");
        System.out.println("telephone " + telephone);
        System.out.println("password " + password);
        // 根据前端传回来的账号和密码在user表中查找对应的stationid
        User user = dao.findFirst("SELECT * FROM user WHERE telephone =? AND password =?", telephone, password);
        JSONObject json = new JSONObject();
        if (user != null) {
            setSessionAttr("telephone", telephone);
            json.put("code", 200);
            json.put("users", user);
            System.out.println("right");
            // 获取查找到的用户对应的stationid
            String stationid = user.getStr("stationid");
            System.out.println("在登录的时候存储的stationid: " + stationid);
            // 通过获取到的stationid连接user表和station表，并获取所需属性列，明确指定user表的telephone
            List<Record> joinedRecords = Db.use().find("SELECT user.*, user.telephone AS user_telephone, gas_station.*, gas_station.telephone AS gas_station_telephone " +
                    "FROM user " +
                    "JOIN gas_station ON user.stationid = gas_station.id " +
                    "WHERE user.stationid =? ", stationid);
            JSONArray jsonArray = new JSONArray();
            for (Record record : joinedRecords) {
                jsonArray.add(record.getColumns());
            }
            json.put("joinedData", jsonArray);
            // 打印返回给前端的数据
            System.out.println("返回给前端的数据: " + json.toJSONString());
            // 将stationid和staff存储到会话中
            HttpSession session = getSession(true);
            session.setAttribute("stationid", stationid);
        } else {
            json.put("flag", "300");
        }
        renderJson(json);
    }
    public void transport() {
        // 从会话中获取在login方法中存储的stationid和staff
        int stationid=getParaToInt("stationid");

        String stationname = getPara("stationname");
        String bottleid = getPara("bottleid");
        String address = getPara("address");
        String jingdu = getPara("jingdu");
        String weidu = getPara("weidu");
        String staff = getPara("staff");
        String location = getPara("location");
        String telephone = getPara("telephone");
        String customer = getPara("customerSelected");
        System.out.println("transport里的staff " + staff);
        System.out.println("location " + location);
        // 创建一个Record对象，用于存储要插入的数据
        Record record = new Record();
        record.set("stationid", stationid);
        record.set("stationname", stationname);
        record.set("bottleid", bottleid);
        record.set("address", address);
        record.set("staff", staff);
        record.set("jingdu", jingdu);
        record.set("weidu", weidu);
        record.set("location", location);
        record.set("telephone", telephone);
        record.set("customer", customer);
        // 将数据插入到transport表中
        boolean success = Db.use().save("transport", record);

        JSONObject json = new JSONObject();
        if (success) {
            json.put("flag", "200");
            json.put("message", "数据插入成功");
        } else {
            json.put("flag", "300");
            json.put("message", "数据插入失败");
        }

        renderJson(json);
    }

    public void downcar() {
        // 获取前端传来的参数
        String bottleid = getPara("bottleid");
        String stationname = getPara("stationname");
        String downcarAddress = getPara("downcarAddress");
        String customerSelected = getPara("customerSelected");
        String keyword = getPara("keyword");
        int stationid = getParaToInt("stationid");
        String staff = getPara("staff");
        String address = getPara("address");
        String jingdu = getPara("jingdu");
        String weidu = getPara("weidu");
        String location = getPara("location");
        String telephone = getPara("telephone");

        // 创建一个Record对象，用于存储要插入到downcar表的数据
        Record record = new Record();
        record.set("stationid", stationid);
        record.set("stationname", stationname);
        record.set("bottleid", bottleid);
        record.set("customer", customerSelected);
        record.set("staff", staff);
        record.set("address", address);
        record.set("jingdu", jingdu);
        record.set("weidu", weidu);
        record.set("location", location);
        record.set("telephone", telephone);

        if (downcarAddress != null && !downcarAddress.isEmpty()) {
            record.set("downaddress", downcarAddress);
        }
        if (customerSelected != null && !customerSelected.isEmpty()) {
            record.set("customer", customerSelected);
        }

        // 将数据插入到downcar表中
        boolean success = Db.use().save("downcar", record);

        JSONObject json = new JSONObject();
        if (success) {
            json.put("flag", "200");
            json.put("message", "数据插入成功");
        } else {
            json.put("flag", "300");
            json.put("message", "数据插入失败");
        }

        renderJson(json);
    }

    public void searchCustomers() {
        // 获取前端传来的关键字和站点 ID
        String keyword = getPara("keyword");
        int stationid = getParaToInt("stationid");

        // 构建模糊查询customer的SQL语句
        StringBuilder sql = new StringBuilder("SELECT * FROM custromer WHERE stationid =? ");
        List<Object> params = new ArrayList<>();
        params.add(stationid);

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (customer LIKE? OR caddress LIKE? )");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        // 执行查询，获取所有符合条件的记录
        List<Record> customerRecords = Db.use().find(sql.toString(), params.toArray());
        JSONArray jsonArray = new JSONArray();
        for (Record customerRecord : customerRecords) {
            JSONObject customerJson = new JSONObject();
            customerJson.put("customer", customerRecord.get("customer"));
            customerJson.put("caddress", customerRecord.get("caddress"));
            jsonArray.add(customerJson);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("customers", jsonArray);

        // 将查询结果返回给前端
        renderJson(resultJson);
    }
    public void list() {
        // 从会话中获取在其他地方（比如登录方法）存储的stationid
        int pageNum = getParaToInt("pageNum");
        int pageSize = getParaToInt("pageSize");
        int stationid = getParaToInt("stationid");

        // 从会话中获取 telephone 信息
        String telephone = (String) getSession().getAttribute("telephone");

        System.out.println("当前页码：" + pageNum);
        System.out.println("每页大小：" + pageSize);
        System.out.println("stationid：" + stationid);
        System.out.println("telephone：" + telephone);

        try {
            // 构建查询SQL语句，从transport表中选择指定字段，并根据stationid进行筛选
            String fromSql = "from transport where stationid = " + stationid + " order by id desc";
            System.out.println(fromSql);

            // 获取总记录数
            Record totalRecord = Db.use().findFirst("SELECT COUNT(*) AS total FROM transport WHERE stationid = " + stationid + "");
            if (totalRecord != null) {
                int total = totalRecord.getInt("total");
                System.out.println("总记录数：" + total);

                // 获取当前页的记录
                Page<Record> records = Db.use().paginate(pageNum, pageSize, "select *", fromSql);
                System.out.println(records.toString());

                // 创建一个JSONArray用于存储所有符合条件的记录（以JSON形式）
                JSONArray jsonArray = new JSONArray();

                List<Record> recordList = records.getList();
                if (recordList != null && !recordList.isEmpty()) {
                    System.out.println("正确进入了if循环");
                    // 遍历查询结果，将每个符合条件的记录转换为JSON对象并添加到JSONArray中
                    for (Record record : recordList) {
                        JSONObject recordJson = new JSONObject();
                        recordJson.put("stationname", record.get("stationname"));
                        recordJson.put("staff", record.get("staff"));
                        recordJson.put("telephone", record.get("telephone"));
                        recordJson.put("location", record.get("location"));
                        recordJson.put("bottleid", record.get("bottleid"));
                        recordJson.put("uptime", record.get("uptime"));
                        recordJson.put("address", record.get("address"));
                        recordJson.put("customer", record.get("customer"));
                        recordJson.put("jingdu", record.get("jingdu"));
                        recordJson.put("weidu", record.get("weidu"));
                        jsonArray.add(recordJson);
                        System.out.println("正确进入了for循环");
                        System.out.println("staff: " + record.get("staff"));
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

    public void getDistinctBottleIds() {
        // 构建 SQL 查询语句，使用 DISTINCT 关键字对 bottleid 进行去重查询
        String sql = "SELECT DISTINCT bottle_id FROM gas_bottle";
        // 执行 SQL 查询，获取去重后的 bottleid 记录列表
        List<Record> records = Db.use().find(sql);
        // 创建 JSONArray 用于存储结果
        JSONArray jsonArray = new JSONArray();
        // 遍历查询结果，将每个 bottleid 添加到 JSONArray 中
        for (Record record : records) {
            jsonArray.add(record.get("bottle_id"));
        }
        // 创建 JSONObject 用于封装最终返回给前端的数据
        JSONObject json = new JSONObject();
        json.put("bottleIds", jsonArray);
        // 将结果以 JSON 格式返回给前端
        renderJson(json);
    }



    public void searchTransportData() {
        // 添加这一行来允许跨域请求（假设你的框架有对应的方法来添加跨域请求头，这里只是示例，具体根据实际框架调整）
//        addCorsHeaders();

//        // 从会话中获取在其他地方（比如登录方法）存储的stationid
//        String stationid = (String) getSession().getAttribute("stationid");

        // 获取前端传递的参数
        int pageNum = getParaToInt("pageNum");
        int pageSize = getParaToInt("pageSize");
        int stationid = getParaToInt("stationid");
        String qipingid = getPara("qipingid");
        String riqi = getPara("riqi");

        System.out.println(qipingid + "\n" + riqi + "\n" + pageNum + "\n" + pageSize + "\n");

        try {
            String fromSql = "from transport where stationid = " + stationid + " ";

            if (qipingid!= null) {
                fromSql += "and bottleid like '%" + qipingid + "%' ";
            }

            if (riqi!= null) {
                fromSql += " AND uptime >= '" + riqi + " 00:00:00' AND uptime < '" + riqi + " 23:59:59'";
            }

            fromSql += " order by id desc";
            System.out.println(fromSql);

            // 获取总记录数
            Record totalRecord = Db.use().findFirst("SELECT COUNT(*) AS total FROM transport WHERE stationid = " + stationid + " ");
            if (totalRecord!= null) {
                int total = totalRecord.getInt("total");
                System.out.println(total);

                // 获取当前页的记录
                Page<Record> records = Db.use().paginate(pageNum, pageSize, "select *", fromSql);

                // 创建一个JSONArray用于存储所有符合条件的记录（以JSON形式）
                JSONArray jsonArray = new JSONArray();

                for (Record record : records.getList()) {
                    JSONObject recordJson = new JSONObject();
                    recordJson.put("stationname", record.get("stationname"));
                    recordJson.put("staff", record.get("staff"));
                    recordJson.put("telephone", record.get("telephone"));
                    recordJson.put("location", record.get("location"));
                    recordJson.put("bottleid", record.get("bottleid"));
                    recordJson.put("uptime", record.get("uptime"));
                    recordJson.put("address", record.get("address"));
                    recordJson.put("customer", record.get("customer"));
                    recordJson.put("jingdu", record.get("jingdu"));
                    recordJson.put("weidu", record.get("weidu"));

                    jsonArray.add(recordJson);
                }

                // 创建一个JSONObject用于返回给前端
                JSONObject json = new JSONObject();
                json.put("total", total);
                json.put("rows", jsonArray);

                // 将结果返回给前端
                renderJson(json);
            } else {
                System.out.println(fromSql);
                // 如果没有找到记录，可以返回一个空的JSON对象
                JSONObject json = new JSONObject();
                json.put("total", 0);
                json.put("rows", new ArrayList<>());

                // 将结果返回给前端
                renderJson(json);
            }
        } catch (Exception e) {
            // 这里可以添加更详细的错误处理，比如记录日志
            e.printStackTrace();
            renderError(500);
        }
    }

    public void cklist() {
        // 从会话中获取在其他地方（比如登录方法）存储的stationid
        int pageNum = getParaToInt("pageNum");
        int pageSize = getParaToInt("pageSize");
        int stationid = getParaToInt("stationid");

        // 从会话中获取 telephone 信息
        String telephone = (String) getSession().getAttribute("telephone");

        System.out.println("当前页码：" + pageNum);
        System.out.println("每页大小：" + pageSize);
        System.out.println("stationid：" + stationid);
        System.out.println("telephone：" + telephone);

        try {
            // 构建查询SQL语句，从transport表中选择指定字段，并根据stationid进行筛选
            String fromSql = "from downcar where stationid = " + stationid + " order by id desc";
            System.out.println(fromSql);

            // 获取总记录数
            Record totalRecord = Db.use().findFirst("SELECT COUNT(*) AS total FROM downcar WHERE stationid = " + stationid + "");
            if (totalRecord != null) {
                int total = totalRecord.getInt("total");
                System.out.println("总记录数：" + total);

                // 获取当前页的记录
                Page<Record> records = Db.use().paginate(pageNum, pageSize, "select *", fromSql);
                System.out.println(records.toString());

                // 创建一个JSONArray用于存储所有符合条件的记录（以JSON形式）
                JSONArray jsonArray = new JSONArray();

                List<Record> recordList = records.getList();
                if (recordList != null && !recordList.isEmpty()) {
                    System.out.println("正确进入了if循环");
                    // 遍历查询结果，将每个符合条件的记录转换为JSON对象并添加到JSONArray中
                    for (Record record : recordList) {
                        JSONObject recordJson = new JSONObject();
                        recordJson.put("stationname", record.get("stationname"));
                        recordJson.put("staff", record.get("staff"));
                        recordJson.put("telephone", record.get("telephone"));
                        recordJson.put("location", record.get("location"));
                        recordJson.put("bottleicustrod", record.get("bottleid"));
                        recordJson.put("downtime", record.get("downtime"));
                        recordJson.put("address", record.get("address"));
                        recordJson.put("customer", record.get("customer"));
                        recordJson.put("jingdu", record.get("jingdu"));
                        recordJson.put("weidu", record.get("weidu"));
                        jsonArray.add(recordJson);
                        System.out.println("正确进入了for循环");
                        System.out.println("staff: " + record.get("staff"));
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

    public void cksearchTransportData() {
        // 添加这一行来允许跨域请求（假设你的框架有对应的方法来添加跨域请求头，这里只是示例，具体根据实际框架调整）
//        addCorsHeaders();

//        // 从会话中获取在其他地方（比如登录方法）存储的stationid
//        String stationid = (String) getSession().getAttribute("stationid");

        // 获取前端传递的参数
        int pageNum = getParaToInt("pageNum");
        int pageSize = getParaToInt("pageSize");
        int stationid = getParaToInt("stationid");
        String qipingid = getPara("qipingid");
        String riqi = getPara("riqi");

        System.out.println(qipingid + "\n" + riqi + "\n" + pageNum + "\n" + pageSize + "\n");

        try {
            String fromSql = "from downcar where stationid = " + stationid + " ";

            if (qipingid!= null) {
                fromSql += "and bottleid like '%" + qipingid + "%' ";
            }

            if (riqi!= null) {
                fromSql += " AND downtime >= '" + riqi + " 00:00:00' AND downtime < '" + riqi + " 23:59:59'";
            }

            fromSql += " order by id desc";
            System.out.println(fromSql);

            // 获取总记录数
            Record totalRecord = Db.use().findFirst("SELECT COUNT(*) AS total FROM downcar WHERE stationid = " + stationid + " ");
            if (totalRecord!= null) {
                int total = totalRecord.getInt("total");
                System.out.println(total);

                // 获取当前页的记录
                Page<Record> records = Db.use().paginate(pageNum, pageSize, "select *", fromSql);

                // 创建一个JSONArray用于存储所有符合条件的记录（以JSON形式）
                JSONArray jsonArray = new JSONArray();

                for (Record record : records.getList()) {
                    JSONObject recordJson = new JSONObject();
                    recordJson.put("stationname", record.get("stationname"));
                    recordJson.put("staff", record.get("staff"));
                    recordJson.put("telephone", record.get("telephone"));
                    recordJson.put("location", record.get("location"));
                    recordJson.put("bottleid", record.get("bottleid"));
                    recordJson.put("downtime", record.get("downtime"));
                    recordJson.put("address", record.get("address"));
                    recordJson.put("customer", record.get("customer"));
                    recordJson.put("jingdu", record.get("jingdu"));
                    recordJson.put("weidu", record.get("weidu"));
                    System.out.println("downtime: " + recordJson.toJSONString());
                    jsonArray.add(recordJson);
                }

                // 创建一个JSONObject用于返回给前端
                JSONObject json = new JSONObject();
                json.put("total", total);
                json.put("rows", jsonArray);

                // 将结果返回给前端
                renderJson(json);
            } else {
                System.out.println(fromSql);
                // 如果没有找到记录，可以返回一个空的JSON对象
                JSONObject json = new JSONObject();
                json.put("total", 0);
                json.put("rows", new ArrayList<>());

                // 将结果返回给前端
                renderJson(json);
            }
        } catch (Exception e) {
            // 这里可以添加更详细的错误处理，比如记录日志
            e.printStackTrace();
            renderError(500);
        }
    }

    public void getinfo() {
        String bottleid = getPara("bottleid");
        System.out.println("bottleid: " + bottleid);

        JSONObject json = new JSONObject();

        if (bottleid != null && !bottleid.isEmpty()) {
            try {
                // 修改后的SQL，使用JOIN关联两个表
                String sql = "SELECT gf.*, gb.trans_staff, gb.tel " +
                        "FROM gas_file gf " +
                        "LEFT JOIN gas_bottle gb ON gf.gas_number = gb.bottle_id " +
                        "WHERE gf.gas_number = ?";

                // 执行查询
                Record record = Db.use().findFirst(sql, bottleid);

                if (record != null) {
                    // 获取gas_file所有字段
                    json.putAll(record.getColumns());

                    // 添加gas_bottle的特定字段
                    json.put("trans_staff", record.getStr("trans_staff"));
                    json.put("tel", record.getStr("tel"));

                    json.put("flag", "200");
                    json.put("message", "数据查询成功");
                } else {
                    json.put("flag", "300");
                    json.put("message", "未找到对应的气瓶信息");
                }
            } catch (Exception e) {
                // 处理数据库异常
                json.put("flag", "500");
                json.put("message", "数据库查询异常: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            json.put("flag", "300");
            json.put("message", "传入的气瓶编号为空");
        }

        renderJson(json);
    }

    public void checkBottleExists() {
        // 获取前端传来的二维码编号
        String qrcode = getPara("qrcode");
        JSONObject json = new JSONObject();

        // 参数有效性验证
        if (qrcode == null || qrcode.isEmpty()) {
            json.put("flag", "300");
            json.put("message", "二维码编号不能为空");
            renderJson(json);
            return;
        }

        try {
            // 构建查询SQL
            String sql = "SELECT id FROM transport WHERE bottleid = ? LIMIT 1";
            // 执行查询
            Record record = Db.use().findFirst(sql, qrcode);

            if (record != null) {
                json.put("flag", "200");
                json.put("message", "存在运输记录");
            } else {
                json.put("flag", "300");
                json.put("message", "无对应运输记录");
            }
        } catch (Exception e) {
            // 数据库异常处理
            json.put("flag", "500");
            json.put("message", "数据库查询异常: " + e.getMessage());
            e.printStackTrace();
        }

        renderJson(json);
    }


    }

