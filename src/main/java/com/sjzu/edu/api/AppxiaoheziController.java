package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Bangdingren;
import com.sjzu.edu.common.model.User;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Path(value = "/", viewPath = "/appxiaohezi")
public class AppxiaoheziController extends Controller {
    private Bangdingren dao = new Bangdingren().dao();
    public void login() {
        String telephone = getPara("telephone");
        String password = getPara("password");
        System.out.println("hello jason");
        System.out.println("telephone " + telephone);
        System.out.println("password " + password);
        // 根据前端传回来的账号和密码在user表中查找对应的stationid
       Bangdingren bangdingren = dao.findFirst("SELECT * FROM bangdingren WHERE telphone =? AND psw =?", telephone, password);
        JSONObject json = new JSONObject();
        if (bangdingren != null) {
            setSessionAttr("telephone", telephone);
            json.put("code", 200);
            json.put("data", bangdingren);
            System.out.println("right");
        } else {
            json.put("flag", "300");
        }
        renderJson(json);
    }

    public void saveXiaoheziData() {
        System.out.println("接收参数: " + getParaMap());

        JSONObject result = new JSONObject();
        try {
            // 接收参数
            String worker = getPara("worker");
            String jingdu = getPara("jingdu");
            String weidu = getPara("weidu");
            String address = getPara("address");
            String xiaoheziNumber = getPara("xiaohezi_number");
            String uuid= getPara("uuid");
            String telephone = getPara("telephone");
            // 参数校验
            if (xiaoheziNumber == null || xiaoheziNumber.isEmpty()) {
                result.put("code", 400);
                result.put("msg", "小盒子编号不能为空");
                renderJson(result);
                return;
            }

            Record record = new Record();
            record.set("worker", worker)
                    .set("jingdu", jingdu)
                    .set("weidu", weidu)
                    .set("address", address)
                    .set("xiaohezi_number", xiaoheziNumber)
                     .set("uuid", uuid)
                    .set("telephone", telephone);


            // 处理每个气瓶和通道
            for (int i = 1; i <= 4; i++) {
                String gasNumber = getPara("gas_number" + i);
                String tongdao = getPara("tongdao" + i);

                if (gasNumber != null && !gasNumber.isEmpty()) {
                    record.set("gas_number" + i, gasNumber);
                    // 处理通道值
                    if (tongdao != null && !tongdao.isEmpty()) {
                        try {
                            // 验证是否为整数
                            Integer.parseInt(tongdao);
                            record.set("tongdao" + i, tongdao);
                        } catch (NumberFormatException e) {
                            result.put("code", 400);
                            result.put("msg", "通道" + i + "的值无效，必须为整数");
                            renderJson(result);
                            return;
                        }
                    } else {
                        // 设置为默认值0或NULL，根据数据库约束调整
                        record.set("tongdao" + i, 0); // 或 null
                    }
                } else {
                    // 气瓶为空时不设置或设为NULL/默认值
                    record.set("gas_number" + i, null);
                    record.set("tongdao" + i, null); // 或 0
                }
            }

            // 保存记录
            Db.save("bse_xiaohezi", record);
            result.put("code", 200);
            result.put("msg", "数据保存成功");

        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
            e.printStackTrace();
        }
        renderJson(result);
    }

    public void getData() {
        // 获取分页参数
        // 获取分页参数（修改参数名）
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10); // 参数名改为pageSize
        System.out.println("pageNumber " + pageNumber);
        System.out.println("pageSize " + pageSize);
        // 接收查询参数
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        String restaurantName = getPara("restaurantname");
        String gasNumber = getPara("gas_number");
        String telephone = getPara("telephone");
        String baseUrl = "http://172.20.10.2:8099/";
        System.out.println("telephone " + telephone);
        System.out.println("startTime " + startTime);
        System.out.println("endTime " + endTime);
        // 构建SQL查询
        String select = "SELECT " +
                "d.door_video, d.qiguan_video, d.daoguan_video, d.louguan_video, " +
                "x.creattime AS x_creattime, " +
                "x.worker AS x_worker, " +
                "x.telephone AS x_telephone, " +
                "x.xiaohezi_number AS x_xiaohezi_number, " +
                "r.name AS r_name, " +
                "r.address AS r_address, " +
                "x.gas_number1 AS x_gas_number1, " +
                "x.gas_number2 AS x_gas_number2, " +
                "x.gas_number3 AS x_gas_number3, " +
                "x.gas_number4 AS x_gas_number4 ";

        StringBuilder sqlExceptSelect = new StringBuilder(
                "FROM bse_data d " +
                        "INNER JOIN bse_xiaohezi x ON d.uuid = x.uuid " +
                        "INNER JOIN restaurant r ON x.xiaohezi_number = r.xiaohezi "
        );

        StringBuilder whereClause = new StringBuilder();
        List<Object> params = new ArrayList<>();
        boolean hasWhere = false;

        // 1. 时间范围条件（优化时间格式处理）
        if (StrKit.notBlank(startTime) && StrKit.notBlank(endTime)) {
            whereClause.append("x.creattime BETWEEN ? AND ? ");
            params.add(startTime + " 00:00:00");
            params.add(endTime + " 23:59:59");
            hasWhere = true;
        }

        // 2. 餐厅名称模糊查询
        if (!StrKit.isBlank(restaurantName)) {
            if (hasWhere) {
                whereClause.append(" AND ");
            }
            whereClause.append("r.name LIKE ? ");
            params.add("%" + restaurantName + "%");
            hasWhere = true;
        }

        // 3. 气瓶号查询
        if (!StrKit.isBlank(gasNumber)) {
            if (hasWhere) {
                whereClause.append(" AND ");
            }
            whereClause.append("(x.gas_number1 = ? OR x.gas_number2 = ? OR x.gas_number3 = ?) ");
            Collections.addAll(params, gasNumber, gasNumber, gasNumber);
            hasWhere = true;
        }

        // 4. 电话号码精确查询
        if (!StrKit.isBlank(telephone)) {
            if (hasWhere) {
                whereClause.append(" AND ");
            }
            whereClause.append("x.telephone = ? ");
            params.add(telephone);
            hasWhere = true;
        }


        // 组合WHERE条件
        if (hasWhere) {
            sqlExceptSelect.append(" WHERE ").append(whereClause);
        }
        sqlExceptSelect.append(" ORDER BY d.id DESC");

        // 执行分页查询
        Page<Record> page = Db.paginate(pageNumber, pageSize, select, sqlExceptSelect.toString(), params.toArray());
        JSONObject result = new JSONObject();
        if (page.getList().isEmpty()) {
            result.put("flag", "300");
            result.put("message", "未找到相关记录");
        } else {
            List<JSONObject> formattedList = page.getList().stream().map(record -> {
                JSONObject item = new JSONObject();

                // 基础字段
                item.put("createTime", record.getStr("x_creattime"));
                item.put("worker", record.getStr("x_worker"));
                item.put("telephone", record.getStr("x_telephone"));
                item.put("xiaoheziNumber", record.getStr("x_xiaohezi_number"));

                // 新增餐厅信息
                item.put("restaurantName", record.getStr("r_name"));     // 餐厅名称
                item.put("restaurantAddress", record.getStr("r_address")); // 餐厅地址

                // 气瓶编号
                item.put("gasnumber1", record.getStr("x_gas_number1"));
                item.put("gasnumber2", record.getStr("x_gas_number2"));
                item.put("gasnumber3", record.getStr("x_gas_number3"));
                item.put("gasnumber4", record.getStr("x_gas_number4"));

                // 媒体处理（保持不变）
                convertMediaPath(record, item, "door_video", "anzhuanghuanjing", baseUrl);
                convertMediaPath(record, item, "qiguan_video", "lianjiefa", baseUrl);
                convertMediaPath(record, item, "daoguan_video", "lianjieguan", baseUrl);
                convertMediaPath(record, item, "louguan_video", "gongqihetong", baseUrl);

                return item;
            }).collect(Collectors.toList());

            result.put("flag", "200");
            result.put("bangdinglist", formattedList);
            result.put("total", page.getTotalRow());       // 关键修改：统一字段名
            result.put("totalPages", page.getTotalPage()); // 总页数
        }

        renderJson(result);
    }

    // 新增的媒体路径转换方法
    private void convertMediaPath(Record record, JSONObject item,
                                  String dbField, String jsonKey,
                                  String baseUrl) {
        String rawPath = record.getStr(dbField);
        System.out.println("【DEBUG】原始路径 - " + dbField + ": " + rawPath); // 新增调试输出
        if (rawPath != null && !rawPath.isEmpty()) {
            String[] paths = rawPath.split(",");
            List<String> urls = Arrays.stream(paths)
                    .map(path -> {
                        String fullUrl = path.startsWith("http") ? path : baseUrl + path;
                        System.out.println("【DEBUG】生成URL - " + jsonKey + ": " + fullUrl); // 调试输出
                        return fullUrl;
                    })
                    .collect(Collectors.toList());
            item.put(jsonKey, urls);
        } else {
            System.out.println("【WARN】字段 " + dbField + " 为空"); // 空路径警告
            item.put(jsonKey, new ArrayList<>());
        }
    }

}
