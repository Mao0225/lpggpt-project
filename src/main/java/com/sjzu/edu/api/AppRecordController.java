package com.sjzu.edu.api;

import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.expr.ast.Id;
import com.sjzu.edu.common.model.*;

import com.sjzu.edu.common.model.BseGun;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Path(value = "/", viewPath = "/apprecord")
@Clear
public class AppRecordController extends Controller {
    private FillRecordCheck1 dao = new FillRecordCheck1().dao();
    private GasFile gasfiledao = new GasFile().dao();
    private GasStationStaff stationstaffdao = new GasStationStaff().dao();

    private GasStation station = new GasStation().dao();

    private AutoGasFillingRecord dao1 = new AutoGasFillingRecord();

    private BseGun dao2 = new BseGun().dao();


    public void deleteDaijianData() {
        // 获取参数并进行初步验证
        String id = getPara("id");

        if (id == null || id.trim().isEmpty()) {
            JSONObject json = new JSONObject();
            json.put("flag", "400");
            json.put("msg", "ID不能为空");
            renderJson(json);
            return;
        }

        try {
            // 执行删除操作
            int affectedRows = Db.use("jiaqi").delete("DELETE FROM auto_gas_filling_wait_record WHERE id = ?", id);

            JSONObject json = new JSONObject();
            if (affectedRows > 0) {
                // 删除成功
                json.put("flag", "200");
                json.put("msg", "删除成功");
                json.put("affectedRows", affectedRows);
            } else {
                // 未找到对应记录
                json.put("flag", "404");
                json.put("msg", "未找到对应记录");
            }
            renderJson(json);
        } catch (Exception e) {
            // 记录异常日志
            e.printStackTrace();
            JSONObject json = new JSONObject();
            json.put("flag", "500");
            json.put("msg", "服务器内部错误：" + e.getMessage());
            renderJson(json);
        }
    }
    public void getdaijiandata() {
        // 获取待检气瓶信息
        String org_code = getPara("org_code");
        int pageNumber = getParaToInt("pageNumber", 1); // 默认第一页
        int pageSize = getParaToInt("pageSize", 10); // 默认每页显示10条记录

        // 打印前端传递的数据
        System.out.println("org_code: " + org_code);
        System.out.println("pageNumber: " + pageNumber);
        System.out.println("pageSize: " + pageSize);

        // 计算偏移量
        int offset = (pageNumber - 1) * pageSize;

        // 查询待检气瓶数据 (使用 jiaqi 数据源)
        String sql = "SELECT * FROM auto_gas_filling_wait_record WHERE org_code = ? ORDER BY id DESC LIMIT ? OFFSET ?";
        List<Record> data = Db.use("jiaqi").find(sql, org_code, pageSize, offset);
        System.out.println("待检测气罐数据data: " + data);

        // 获取所有气瓶的编号（gas_bottle_no），并去除前导零
        List<String> gasBottleNos = data.stream()
                .map(record -> record.getStr("gas_bottle_no").replaceFirst("^0+(?!$)", "")) // 去除前导零
                .collect(Collectors.toList());

        // 查询与这些气瓶编号关联的 gas_file 数据 (使用 ruoyi_lpg 数据源)
        List<Record> gasFileData = new ArrayList<>();
        if (!gasBottleNos.isEmpty()) {
            // 将 gasBottleNos 转换成 SQL 参数格式
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < gasBottleNos.size(); i++) {
                placeholders.append("?");
                if (i < gasBottleNos.size() - 1) {
                    placeholders.append(",");
                }
            }

            // 更新查询语句，支持多个气瓶编号，并去除 gas_number 的前导零
            String gasFileSql = "SELECT * FROM gas_file WHERE TRIM(LEADING '0' FROM gas_number) IN (" + placeholders.toString() + ")";
            gasFileData = Db.find(gasFileSql, gasBottleNos.toArray());
            System.out.println("关联的gas_file数据: " + gasFileData);
        }

        // 将gas_file的部分字段合并到每条auto_gas_filling_wait_record数据中
        List<Map<String, Object>> combinedResults = new ArrayList<>();
        for (Record record : data) {
            Map<String, Object> combined = new HashMap<>();
            // 将 auto_gas_filling_wait_record 的所有数据放入合并结果
            combined.putAll(record.getColumns());

            // 根据 gas_bottle_no 匹配 gas_file 数据，并只添加需要的字段
            String gasBottleNo = record.getStr("gas_bottle_no").replaceFirst("^0+(?!$)", ""); // 去除前导零
            gasFileData.stream()
                    .filter(gfRecord -> gfRecord.getStr("gas_number").replaceFirst("^0+(?!$)", "").equals(gasBottleNo))
                    .findFirst()
                    .ifPresent(gfRecord -> {
                        // 只添加需要的字段
                        combined.put("terminate_use_date", gfRecord.get("terminate_use_date"));
                        combined.put("filling_medium", gfRecord.get("filling_medium"));
                        combined.put("gas_suttle", gfRecord.get("gas_suttle"));
                        // 你可以根据需要添加其他字段
                    });

            combinedResults.add(combined);
        }

        // 查询总记录数 (使用 jiaqi 数据源)
        String countSql = "SELECT COUNT(*) AS total FROM auto_gas_filling_wait_record WHERE org_code = ?";
        Record countRecord = Db.use("jiaqi").findFirst(countSql, org_code);
        int total = countRecord != null ? countRecord.getInt("total") : 0;

        // 构造响应数据
        JSONObject json = new JSONObject();
        if (combinedResults != null && !combinedResults.isEmpty()) {
            System.out.println("combinedResults: " + combinedResults);
            json.put("flag", "200");
            json.put("data", combinedResults);
            json.put("total", total);
        } else {
            System.out.println("combinedResults == null || combinedResults.isEmpty()");
            json.put("flag", "300");
            json.put("data", new ArrayList<>()); // 返回空列表
            json.put("total", 0);
        }

        renderJson(json);
    }


    public void gasgunlist() {
        // 从请求中获取 stationid 参数
        String stationid = getPara("stationid");  // 获取加气枪所属的加气站ID
        System.out.println("Received stationid: " + stationid);  // 输出接收到的 stationid

        // 根据 stationid 查询 gas_station 表，获取 station 的 id
        GasStation sta = station.findFirst("SELECT id FROM gas_station WHERE station_id = ?", stationid);

        // 如果查询到的数据为空，则返回错误信息
        if (sta == null) {
            JSONObject json = new JSONObject();
            json.put("flag", "404");
            json.put("message", "No station found for stationid: " + stationid);
            renderJson(json);
            return;
        }

        // 获取 station id
        Integer staid = sta.getId();  // 确保 getId() 方法正确获取到 id
        System.out.println("Found station id: " + staid);  // 输出查询到的 station id

        // 根据 stationid 查询 bse_gun 表中的数据
        List<BseGun> bseGunList = dao2.find("SELECT * FROM bse_gun WHERE stationid = ?", staid);
        // 创建 JSON 对象来返回数据
        JSONObject json = new JSONObject();

        // 如果查询结果不为空，则将数据返回
        if (bseGunList != null && !bseGunList.isEmpty()) {
            json.put("bseGun", bseGunList);
            json.put("flag", "200");
        } else {
            json.put("flag", "404");
            json.put("message", "No guns found for stationid: " + stationid);
        }

        // 返回 JSON 响应
        renderJson(json);
    }


//    public void records() {
//        // 获取前端传递的分页参数
//        int pageNum = getParaToInt("page", 1); // 默认第一页
//        int pageSize = getParaToInt("pageSize", 10); // 默认每页10条
//
//        // 获取组织代码参数
//        String org_code = getPara("org_code");
//
//        // 获取当前日期并格式化为 yyyy-MM-dd
//        LocalDate currentDate = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String formattedDate = currentDate.format(formatter);
//
//        // SQL 查询语句
//
//        //        String sqlExceptSelect = "FROM fill_record_check1 WHERE memo = ? AND gasstation = ? AND fill_time LIKE ? ORDER BY id DESC";
//
//        String select = "SELECT frc.*, gf.*"; // 明确选择字段，避免冲突
//        String sqlExceptSelect = "FROM fill_record_check1 frc " +
//                "JOIN gas_file gf ON frc.gas_number = gf.gas_number " +
//                "WHERE frc.memo = ? " +
//                "AND frc.gasstation = ? " +
//                "AND frc.fill_time LIKE ? " +
//                "ORDER BY frc.id DESC";
//
//
//        Page<FillRecordCheck1> recordPage = dao.paginate(
//                pageNum,
//                pageSize,
//                select,
//                sqlExceptSelect,
//                "充装后检查合格", // 对应 frc.memo = ?
//                org_code,       // 对应 frc.gasstation = ?
//                "%" + formattedDate + "%" // 对应 frc.fill_time LIKE ?
//        );
//
//        // 构造响应数据
//        JSONObject json = new JSONObject();
//        if (recordPage != null && recordPage.getList() != null && !recordPage.getList().isEmpty()) {
//            // 获取recordPage中的所有UUID
//            List<String> uuidList = recordPage.getList()
//                    .stream()
//                    .map(record -> record.getStr("uuid"))
//                    .collect(Collectors.toList());
//
//            // 如果uuidList不为空，则去另外一个数据源查询数据
//            List<Map<String, Object>> combinedResults = new ArrayList<>();
//            if (!uuidList.isEmpty()) {
//                // 构建查询SQL
//                String sql = "SELECT uuid, now_gas, add_gas_long, pressure_begin FROM auto_gas_filling_record WHERE uuid IN (" +
//                        uuidList.stream().map(uuid -> "?").collect(Collectors.joining(",")) +
//                        ")";
//
//                // 执行查询
//                List<Record> fillingRecords = Db.use("jiaqi").find(sql, uuidList.toArray());
//
//                // 打印fillingRecords到控制台
//                System.out.println("Fetched fillingRecords: " + fillingRecords);
//                fillingRecords.forEach(record -> System.out.println(record.toJson()));
//
//                // 按UUID分组结果便于匹配
//                Map<String, Record> fillingRecordMap = fillingRecords.stream()
//                        .collect(Collectors.toMap(
//                                record -> record.getStr("uuid"),
//                                record -> record
//                        ));
//
//                // 将结果匹配到recordPage中的数据
//                recordPage.getList().forEach(record -> {
//                    Map<String, Object> combined = new HashMap<>();
//                    // 添加 FillRecordCheck1 的字段到 combined
//                    combined.put("id", record.get("id"));
//                    combined.put("gas_number", record.get("gas_number"));
//                    combined.put("filling_staff_name", record.get("filling_staff_name"));
//                    combined.put("filling_check_staff_name", record.get("filling_check_staff_name"));
//                    combined.put("memo", record.get("memo"));
//                    combined.put("before_filling_check", record.get("before_filling_check"));
//                    combined.put("after_filling_check", record.get("after_filling_check"));
//                    combined.put("before_filling", record.get("before_filling"));
//                    combined.put("after_filling", record.get("after_filling"));
//                    combined.put("fill_time", record.get("fill_time"));
//                    combined.put("gasstation", record.get("gasstation"));
//                    combined.put("gun_no", record.get("gun_no"));//枪号
//                    combined.put("terminate_use_date", record.get("terminate_use_date"));//气瓶终止使用日期
//                    combined.put("filling_medium", record.get("filling_medium"));//充装介质
//
//                    // 根据 UUID 匹配 auto_gas_filling_record 的数据
//                    String uuid = record.getStr("uuid");
//                    if (fillingRecordMap.containsKey(uuid)) {
//                        Record fillingRecord = fillingRecordMap.get(uuid);
//                        combined.put("now_gas", fillingRecord.get("now_gas"));
//                        combined.put("add_gas_long", fillingRecord.get("add_gas_long"));
//                        combined.put("pressure_begin", fillingRecord.get("pressure_begin"));
//                    } else {
//                        combined.put("now_gas", null);
//                        combined.put("add_gas_long", null);
//                        combined.put("pressure_begin", null);
//                    }
//
//                    combinedResults.add(combined); // 添加到结果列表
//                });
//                System.out.println("Combined Results: " + combinedResults);
//            }
//
//            json.put("flag", "200");
//            json.put("record", combinedResults); // 使用合并后的结果
//            json.put("totalPage", recordPage.getTotalPage());
//            json.put("totalRow", recordPage.getTotalRow());
//        } else {
//            json.put("flag", "300");
//        }
//        renderJson(json);
//    }
//    public void records() {
//    // 获取前端传递的分页参数
//    int pageNum = getParaToInt("page", 1); // 默认第一页
//    int pageSize = getParaToInt("pageSize", 10); // 默认每页10条
//
//    // 获取组织代码参数
//    String org_code = getPara("org_code");
//
//    // 获取当前日期并格式化为 yyyy-MM-dd
//    LocalDate currentDate = LocalDate.now();
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    String formattedDate = currentDate.format(formatter);
//
//    // SQL 查询语句
//    String select = "SELECT frc.*, gf.*"; // 明确选择字段，避免冲突
//    String sqlExceptSelect = "FROM fill_record_check1 frc " +
//            "JOIN gas_file gf ON frc.gas_number = gf.gas_number " +
//            "WHERE frc.memo = ? " +
//            "AND frc.gasstation = ? " +
//            "AND frc.fill_time LIKE ? " +
//            "ORDER BY frc.id DESC";
//
//    // 分页查询
//    Page<FillRecordCheck1> recordPage = dao.paginate(
//            pageNum,
//            pageSize,
//            select,
//            sqlExceptSelect,
//            "充装后检查合格", // 对应 frc.memo = ?
//            org_code,       // 对应 frc.gasstation = ?
//            "%" + formattedDate + "%" // 对应 frc.fill_time LIKE ?
//    );
//        System.out.println("获取到的充装信息为" + recordPage.getList());
//    // 构造响应数据
//    JSONObject json = new JSONObject();
//    if (recordPage != null && recordPage.getList() != null && !recordPage.getList().isEmpty()) {
////        // 获取所有记录，并构造结果
////        List<Map<String, Object>> combinedResults = new ArrayList<>();
////        recordPage.getList().forEach(record -> {
////            Map<String, Object> combined = new HashMap<>();
////            // 将 FillRecordCheck1 和 GasFile 的字段加入 combined
////            combined.put("id", record.get("id"));
////            combined.put("gas_number", record.get("gas_number"));
////            combined.put("filling_staff_name", record.get("filling_staff_name"));
////            combined.put("filling_check_staff_name", record.get("filling_check_staff_name"));
////            combined.put("memo", record.get("memo"));
////            combined.put("before_filling_check", record.get("before_filling_check"));
////            combined.put("after_filling_check", record.get("after_filling_check"));
////            combined.put("before_filling", record.get("before_filling"));
////            combined.put("after_filling", record.get("after_filling"));
////            combined.put("fill_time", record.get("fill_time"));
////            combined.put("gasstation", record.get("gasstation"));
////            combined.put("gun_no", record.get("gun_no")); // 枪号
////            combined.put("terminate_use_date", record.get("terminate_use_date")); // 气瓶终止使用日期
////            combined.put("filling_medium", record.get("filling_medium")); // 充装介质
////
////            // 添加到结果列表
////            combinedResults.add(combined);
////        });
//
//        // 返回结果
//        json.put("flag", "200");
//        json.put("record", recordPage); // 使用合并后的结果
//        json.put("totalPage", recordPage.getTotalPage());
//        json.put("totalRow", recordPage.getTotalRow());
//    } else {
//        json.put("flag", "300");
//    }
//    renderJson(json);
//}

public void records() {
    // 获取前端传递的分页参数
    int pageNum = getParaToInt("page", 1); // 默认第一页
    int pageSize = getParaToInt("pageSize", 10); // 默认每页10条

    // 获取组织代码参数
    String org_code = getPara("org_code");

    // 获取当前日期
    String currentDate = String.valueOf(LocalDate.now());

    // SQL 查询语句
    String select = "SELECT frc.*, gf.*"; // 选择所有字段
    String sqlExceptSelect = "FROM fill_record_check1 frc " +
            "LEFT JOIN gas_file gf ON frc.gas_number = gf.gas_number " +
            "WHERE frc.memo = ? " +
            "AND frc.gasstation = ? " +
            "AND frc.fill_time LIKE ? " + // 使用 = 匹配日期
            "ORDER BY frc.id DESC";

    // 分页查询
    Page<FillRecordCheck1> recordPage = dao.paginate(
            pageNum,
            pageSize,
            select,
            sqlExceptSelect,
            "充装后检查合格", // 对应 frc.memo = ?
            org_code,       // 对应 frc.gasstation = ?
            '%'+currentDate+'%'    // 对应 frc.fill_time = ?
    );
    System.out.println("Parameters: " + Arrays.asList("充装后检查合格", org_code, currentDate));


    // 构造响应数据
    JSONObject json = new JSONObject();
    if (recordPage != null && !recordPage.getList().isEmpty()) {
        json.put("flag", "200");
        json.put("record", recordPage.getList()); // 直接返回查询结果
        json.put("totalPage", recordPage.getTotalPage());
        json.put("totalRow", recordPage.getTotalRow());
    } else {
        json.put("flag", "300");
        json.put("record", new ArrayList<>()); // 返回空数组
    }
    renderJson(json);
}

    public void recordsd() {
        String org_code = getPara("org_code");
        LocalDate currentDate = LocalDate.now();

// 定义日期格式（用于日志输出）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

// SQL 查询
        String sql = "SELECT * FROM fill_record_check1 " +
                "WHERE memo LIKE ? " +
                "  AND gasstation = ? " +
                "  AND fill_time LIKE ?" +
                "ORDER BY id DESC";

        System.out.println("SQL: " + sql);
        System.out.println("Parameters: " + Arrays.asList("%不合格%", org_code, formattedDate));

// 使用参数绑定查询数据
        List<FillRecordCheck1> recordCheck1s = dao.find(sql, "%不合格%", org_code, "%"+currentDate+"%");

// 构造返回结果
        JSONObject json = new JSONObject();
        if (recordCheck1s != null && !recordCheck1s.isEmpty()) {
            json.put("flag", "200");
            json.put("record", recordCheck1s);
        } else {
            json.put("flag", "300");
            json.put("record", new ArrayList<>()); // 返回空数组
        }
        renderJson(json);

    }


    public void addRecords(int flag) {
    String rawData = getRawData();
    JSONObject json = JSON.parseObject(rawData);
    FillRecordCheck1 record = new FillRecordCheck1();
    record.setFlag(flag);

    setIfPresent(json, "memo", record::setMemo);
    setIfPresent(json, "lastuuid", record::setUuid, "uuid: ");
    setIfPresent(json, "gunno", record::setGunNo);
    setIfPresent(json, "gasnumber", record::setGasNumber);
    setIfPresent(json, "selectfillingman", record::setFillingStaffName);
    setIfPresent(json, "okcheckman", record::setFillingCheckStaffName);
    setIfPresent(json, "orgCode", record::setGasstation);

    setDateIfPresent(json, "fill_time", "yyyy-MM-dd HH:mm:ss", record::setFillTime);
    setJsonFieldIfPresent(json, "before_filling_check", record::setBeforeFillingCheck);
    setJsonFieldIfPresent(json, "before_filling", record::setBeforeFilling);
    setJsonFieldIfPresent(json, "after_filling", record::setAfterFilling);
    setJsonFieldIfPresent(json, "after_filling_check", record::setAfterFillingCheck);

    if (record.save()) {
        renderJson("{\"msg\":\"新纪录保存成功！\",\"status\":\"200\"}");
    } else {
        renderJson("{\"msg\":\"新纪录保存失败！\",\"status\":\"500\"}");
    }
}


    public void addBeforeFillingRecords() {
        // 获取请求体
        addRecords(10); // 充装前合格记录
    }

    public void addokrecords() {
        addRecords(20); // 充装后合格记录
    }

    public void addnorecords() {
        addRecords(30); // 充装后不合格记录
    }
    // 辅助方法：处理字符串字段
    private void setIfPresent(JSONObject json, String key, Consumer<String> setter, String... logPrefix) {
        if (json.containsKey(key) && !json.getString(key).isEmpty()) {
            String value = json.getString(key);
            setter.accept(value);
            System.out.println((logPrefix.length > 0 ? logPrefix[0] : "") + value);
        }
    }

    // 辅助方法：处理日期字段
    private void setDateIfPresent(JSONObject json, String key, String format, Consumer<Date> setter) {
        if (json.containsKey(key) && !json.getString(key).isEmpty()) {
            try {
                Date date = new SimpleDateFormat(format).parse(json.getString(key));
                setter.accept(date);
            } catch (ParseException e) {
                System.err.println("Error parsing " + key + ": " + e.getMessage());
            }
        }
    }

    // 辅助方法：处理JSON对象字段
    private void setJsonFieldIfPresent(JSONObject json, String key, Consumer<String> setter) {
        if (json.containsKey(key)) {
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                setter.accept(((JSONObject) value).toJSONString());
            } else {
                setter.accept(value.toString());
            }
            System.out.println(key + " set: " + value);
        }
    }
    public void stationlogin() {
        //测试接口地址：http://localhost:8099/apprecord/stationlogin  参数：telephone：155111122222
        //测试接口地址：http://114.115.156.201:8099/apprecord/stationlogin  参数：telephone：155111122222

        String telephone = getPara("telephone");
        String password = getPara("password");
        System.out.println("hello jason");
        // 查询数据库中是否存在匹配的用户记录


        //User user = User.dao.findFirst("SELECT * FROM user WHERE username = ? AND password = ?", username, password);
        GasStationStaff stationinfo = stationstaffdao.findFirst("SELECT station_id as stationId,staff_name as stationName, staff_id as staffId ,telephone FROM gas_station_staff WHERE telephone = ? AND password = ?", telephone, password);
        //System.out.println(user.toString());
        JSONObject json = new JSONObject();
        if (stationinfo != null) {
            json.put("code", 200);
            json.put("msg", "操作成功");
            json.put("data", stationinfo);
        } else {
            json.put("code", "300");
        }

        renderJson(json);
    }

    public void getStaff() {
        //测试接口地址：http://localhost:8099/apprecord/getStaff  参数：stationid：91210104MA0TP1782R1
        //测试接口地址：http://114.115.156.201:8099/apprecord/getStaff  参数：stationid：91210104MA0TP1782R1

        String telephone = getPara("telephone");
        System.out.println("hello jason");
        // 查询数据库中是否存在匹配的用户记录


        //User user = User.dao.findFirst("SELECT * FROM user WHERE username = ? AND password = ?", username, password);
        List<GasStationStaff> stationinfo = stationstaffdao.find("SELECT staff_name as staffName, staff_id as staffId from gas_station_staff where station_id= (SELECT station_id FROM gas_station_staff WHERE telephone = ? LIMIT 1)", telephone);
        //System.out.println(user.toString());
        JSONObject json = new JSONObject();
        if (stationinfo != null) {
            json.put("code", 200);
            json.put("msg", "操作成功");
            json.put("data", stationinfo);
        } else {
            json.put("code", 300);
        }

        renderJson(json);
    }

    public void getMaxID() {
        // 使用数据源执行查询
        String sqlqiping = "SELECT id FROM auto_gas_filling_record ORDER BY id DESC limit 1";
        List<Record> maxId = Db.use("jiaqi").find(sqlqiping);
        JSONObject json = new JSONObject();
        if (maxId != null) {
            json.put("flag", "200");
            json.put("maxId", maxId);
            System.out.println(maxId);
        } else {
            json.put("flag", "300");
        }
        // 返回JSON对象，而不是尝试渲染HTML页面
        renderJson(json);
    }
    
    public void getdata() {
        //获取加气后的数据
        String rawData = getRawData();
        // 将请求体解析为JSON对象
        JSONObject jsonObject = JSON.parseObject(rawData);
        int id = jsonObject.getIntValue("id");
        String gunno = jsonObject.getString("gunno");
        // 打印前端传递的数据
        System.out.println("id: " + id);
        System.out.println("gunno: " + gunno);
        String sql = "SELECT now_gas,pressure_begin,add_gas_long FROM auto_gas_filling_record WHERE id > ? AND gun_no = ?";
        List<Record> data = Db.use("jiaqi").find(sql, id, gunno);
        JSONObject json = new JSONObject();
        if (data != null) {
            json.put("flag", "200");
            json.put("data", data);
        } else {
            json.put("flag", "300");
        }
        renderJson(json);
    }

    //本地数据库测试
    public void getdata1() {
        String rawData = getRawData();
        // 将请求体解析为JSON对象
        JSONObject jsonObject = JSON.parseObject(rawData);
        int id = jsonObject.getIntValue("id");
        String gunno = jsonObject.getString("gunno");
        // 打印前端传递的数据
        System.out.println("id: " + id);
        System.out.println("gunno: " + gunno);
        String sql = "SELECT now_gas,pressure_begin,add_gas_long FROM auto_gas_filling_record WHERE id > ? AND gun_no = ?";
        List<AutoGasFillingRecord> data = dao1.find(sql, id, gunno);
        JSONObject json = new JSONObject();
        if (data != null) {
            json.put("flag", "200");
            json.put("data", data);
            System.out.println(data);
        } else {
            json.put("flag", "300");
        }
        renderJson(json);
    }

    public void gasbottleinstore() {
        // 加气站气瓶入库
        //测试接口地址：http://localhost:8099/apprecord/gasbottleinstore  参数：bottlenumber：889998654360
        //测试接口地址：http://114.115.156.201:8099/apprecord/gasbottleinstore  参数：bottlenumber：889998654360

        String gasnumber = getPara("gasnumber");
        String rukuren = getPara("rukuren");
        String jingdu = getPara("jingdu");
        String weidu = getPara("weidu");
        String address = getPara("address");
        LocalDate currentDate = LocalDate.now();

        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 格式化日期
        String formattedDate = currentDate.format(formatter);
        System.out.println(formattedDate);

        //获取气瓶基本信息
        GasFile gasfile = gasfiledao.findFirst("SELECT * FROM gas_file WHERE gas_number = ? LIMIT 1", gasnumber);
        JSONObject json = new JSONObject();

        if (gasfile != null) {

            //获取气瓶基本信息结束
            // 创建Drivergpsinfo对象，并设置属性
            Gasbottleinstore gasbottleinfo = new Gasbottleinstore();
            gasbottleinfo.setGasstationid(gasfile.getGasstationid().toString());
            gasbottleinfo.setGasstationname(gasfile.getFilingGasStation());
            gasbottleinfo.setGasnumber(gasnumber);
            gasbottleinfo.setRukuren(rukuren);
            gasbottleinfo.setZhuangtai("合格");
            gasbottleinfo.setRukushijian(formattedDate);
            gasbottleinfo.setAddress(address);
            gasbottleinfo.setJingdu(jingdu); // 假设Drivergpsinfo有这样的setter方法
            gasbottleinfo.setWeidu(weidu);

            // 保存到数据库
            gasbottleinfo.save();

            // 返回JSON响应
            json.put("flag", "200");
            json.put("rukuinfo", gasbottleinfo);
            json.put("qipinginfo", gasfile);
            json.put("info", "入库成功");

        } else {
            json.put("flag", "300");
            json.put("info", "没有该气瓶");

        }
        renderJson(json);

    }
}
