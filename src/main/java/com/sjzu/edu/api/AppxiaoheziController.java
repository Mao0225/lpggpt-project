package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.json.JFinalJsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.Bangdingren;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Path(value = "/", viewPath = "/appxiaohezi")
public class AppxiaoheziController extends Controller {
    private Bangdingren dao = new Bangdingren().dao();
    private static final String WEBAPP_ROOT = PathKit.getWebRootPath();
    private static final String UPLOAD_DIR = WEBAPP_ROOT + "/upload/temp/data";

    ////小盒子 2025-04-01年更新后代码开始，刘国奇，这段代码是新的小盒子代码，主要用于饭店手机端获取小盒子信息

    public void getxiaoheziinfo() {
        addCorsHeaders();
        String xiaohezino = getPara("xiaohezino");
        //获取小盒子的基本操作信息开始
        String sqlxiaohezi = "SELECT * FROM manage_xiaohezi WHERE xiaohezi_no = ?";

        // 执行查询
        List<Record> basxiaohezi = Db.use().find(sqlxiaohezi, xiaohezino);

        //获取小盒子的基本操作信息结束
        System.out.println("jason:"+xiaohezino);
        // 根据小盒子编号，获取最后 10 条小盒子信息
        String sql = "SELECT * FROM t_iot_sync_rds_records_v3 v WHERE v.devicename = ? ORDER BY v.id DESC LIMIT 8";
        List<Record> xiaohezilist = Db.use("lpg").find(sql, xiaohezino);
        System.out.println("jason1:"+xiaohezilist.toString());


        // 初始化返回结果
        JSONObject result = new JSONObject();
        result.put("flag", 200);

        // 初始化存储数据的变量
        Map<String, Object> famenstatus = new HashMap<>();
        Set<String> rfidSet = new HashSet<>();

        // 遍历数据
        boolean foundFirstNonNull = false;
        for (Record record : xiaohezilist) {
            // 查找第一条 ValveStatus1 不为 Null 的数据
            if (record.get("ValveStatus1") != null && !foundFirstNonNull) {
                // 转换时间戳为日期格式
                long timestamp = Long.parseLong(record.get("created_time").toString());
                Date date = new Date(timestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                famenstatus.put("created_time", sdf.format(date));
                famenstatus.put("devicename", record.get("devicename"));
                // 转换 ValveStatus 值
                famenstatus.put("ValveStatus1", convertValveStatus(record.get("ValveStatus1")));
                famenstatus.put("ValveStatus2", convertValveStatus(record.get("ValveStatus2")));
                famenstatus.put("ValveStatus3", convertValveStatus(record.get("ValveStatus3")));
                famenstatus.put("ValveStatus4", convertValveStatus(record.get("ValveStatus4")));

                // 转换 Alarm 值
                famenstatus.put("Alarm", convertAlarm(record.get("Alarm")));

                // 转换 PowerStatus 值
                famenstatus.put("PowerStatus", convertPowerStatus(record.get("PowerStatus")));

                // 转换 M_ValStat 值
                famenstatus.put("M_ValStat", convertM_ValStat(record.get("M_ValStat")));

                foundFirstNonNull = true;
            } else {
                // 收集所有 ValveStatus1 为 Null 的数据中的 Rfid1, Rfid2, Rfid3, Rfid4, Rfid5, Rfid6, Rfid7, Rfid8

                System.out.println("rfid1:"+record.get("Rfid1"));
                System.out.println("rfid2:"+record.get("Rfid2"));
                System.out.println("rfid3:"+record.get("Rfid3"));
                if (record.get("Rfid1") != null) {
                    rfidSet.add(record.get("Rfid1").toString());
                }
                if (record.get("Rfid2") != null) {
                    rfidSet.add(record.get("Rfid2").toString());
                }
                if (record.get("Rfid3") != null) {
                    rfidSet.add(record.get("Rfid3").toString());
                }
                if (record.get("Rfid4") != null) {
                    rfidSet.add(record.get("Rfid4").toString());
                }
                if (record.get("Rfid5") != null) {
                    rfidSet.add(record.get("Rfid5").toString());
                }
                if (record.get("Rfid6") != null) {
                    rfidSet.add(record.get("Rfid6").toString());
                }
                if (record.get("Rfid7") != null) {
                    rfidSet.add(record.get("Rfid7").toString());
                }
                if (record.get("Rfid8") != null) {
                    rfidSet.add(record.get("Rfid8").toString());
                }
            }
        }

        // 处理 Rfid 数据

        List<String> processedRfidList = new ArrayList<>();

        for (String rfid : rfidSet) {
            // 去掉 []
            String cleanedRfid = rfid.substring(1, rfid.length() - 1);
            System.out.println("小盒子cleanedRfid: " + cleanedRfid);

            // 分割为数组
            String[] rfidArray = cleanedRfid.split(",");

            // 获取最后7组数据
            if (rfidArray.length >= 7) {
                String[] finalArray = Arrays.copyOfRange(rfidArray, rfidArray.length - 7, rfidArray.length);

                // 转换为16进制，并补足2位
                StringBuilder hexBuilder = new StringBuilder();
                for (String value : finalArray) {
                    int intValue = Integer.parseInt(value.trim());
                    hexBuilder.append(String.format("%02X", intValue));
                }

                // 组合成一组数字
                String combinedValue = hexBuilder.toString();
                processedRfidList.add(combinedValue);
            }
        }

        // 输出处理后的结果
        for (String value : processedRfidList) {
            System.out.println("处理后的组合数字: " + value);
        }



        // 输出处理后的结果
        for (String value : processedRfidList) {
            System.out.println("处理后的10进制值: " + value);
        }
        // 将结果存入返回对象basxiaohezi
        result.put("famenstatus", famenstatus);
        result.put("processedRfidList", processedRfidList);
        result.put("basxiaohezi", basxiaohezi);
        // 返回 JSON 数据
        renderJson(result);
    }

    private String convertValveStatus(Object status) {
        if (status == null) return null;
        return (Integer) status == 0 ? "关闭" : "打开";
    }

    private String convertAlarm(Object alarm) {
        if (alarm == null) return null;
        int alarmValue = (Integer) alarm;
        switch (alarmValue) {
            case 0: return "无报警";
            case 1: return "操作间报警";
            case 2: return "气瓶间报警";
            case 3: return "视频报警";
            case 4: return "视频+操作间 报警";
            case 5: return "压力表超压报警";
            default: return "未知报警";
        }
    }

    private String convertPowerStatus(Object status) {
        if (status == null) return null;
        return (Integer) status == 0 ? "正常" : "故障";
    }

    private String convertM_ValStat(Object status) {
        if (status == null) return null;
        return (Integer) status == 0 ? "关" : "开";
    }
    public void appanzhuanginfo() {
        //app手机端，获取
        //http://localhost:8099/appxiaohezi/appanzhuanginfo
        addCorsHeaders();

        // 获取分页参数，默认值为第一页，每页10条
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);
        // 获取小盒子编号参数
        String xiaohezino = getPara("xiaohezino", "");

        // 构建查询语句
        String select = "SELECT *";
        String sqlExceptSelect = "FROM bse_xiaohezi WHERE xiaohezi_number = ? ORDER BY id DESC";

        // 执行分页查询
        Page<Record> page = Db.paginate(pageNumber, pageSize, select, sqlExceptSelect, xiaohezino);

        // 构建返回的JSON对象
        JSONObject json = new JSONObject();
        if (page != null && page.getList().size() > 0) {
            json.put("flag", "200");
            json.put("anzhuanglist", page.getList());
            json.put("totalPage", page.getTotalPage());
            json.put("totalRow", page.getTotalRow());
            json.put("pageNumber", page.getPageNumber());
            json.put("pageSize", page.getPageSize());
        } else {
            json.put("flag", "300");
        }

        // 返回JSON格式数据
        renderJson(json);
    }

    // 小盒子 2025年更新后代码结束
//    private Updateapp udao = new Updateapp().dao();
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

    public void checkUpdate() {
        try {
            String clientVersion = getPara("version");
            Record latestRecord = Db.findFirst("SELECT * FROM updateapp ORDER BY create_time DESC LIMIT 1");

            JSONObject result = new JSONObject();

            if (latestRecord == null) {
                result.put("error", "未找到版本信息");
                renderJson(result);
                return;
            }

            String latestVersion = latestRecord.getStr("version");
            boolean needUpdate = compareVersion(clientVersion, latestVersion) < 0;

            // 构建标准响应结构
            result.put("code", 200);
            result.put("data", new JSONObject() {{
                put("needUpdate", needUpdate);
                put("latestVersion", latestVersion);
                put("updateUrl", latestRecord.getStr("download_url"));
                put("description", latestRecord.getStr("description"));
                put("forceUpdate", latestRecord.getBoolean("force_update"));
            }});

            renderJson(result);
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("code", 500);
            error.put("error", "版本检测失败：" + e.getMessage());
            renderJson(error);
        }
    }


    // 语义化版本比较方法
    private int compareVersion(String v1, String v2) {
        String[] arr1 = v1.split("\\.");
        String[] arr2 = v2.split("\\.");

        for (int i = 0; i < Math.max(arr1.length, arr2.length); i++) {
            int num1 = (i < arr1.length) ? Integer.parseInt(arr1[i]) : 0;
            int num2 = (i < arr2.length) ? Integer.parseInt(arr2[i]) : 0;

            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
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
        String baseUrl = "http://114.115.156.201:8099/";
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

    public void updateAccountInfo() {
        //保存手机端的上传的绑定人信息
        // 测试接口：http://114.115.156.201:8099/appxiaohezi/updateAccountInfo
        // 测试接口：http://localhost:8099/appxiaohezi/updateAccountInfo，
        //http://192.168.0.102:8099/appxiaohezi/updateAccountInfo
        try {
            // 获取上传的文件
            UploadFile trainCertificatePhotoFile = getFile("trainCertificatePhoto");
            String trainCertificatePhotoPath = null;
            if (trainCertificatePhotoFile != null) {
                trainCertificatePhotoPath = saveUploadFile(trainCertificatePhotoFile);
            }
            // 获取文本参数
            String bindName = getPara("bindName");
            String bindPhone = getPara("bindPhone");
            String bindPassword = getPara("bindPassword");
            String bindSex = getPara("bindSex");
            Integer id = getParaToInt("id");
            String identityCardNo = getPara("identityCardNo");
            String trainCertificateNo = getPara("trainCertificateNo");
            String trainCertificateIndate = getPara("trainCertificateIndate");

            // 检查 id 是否为空
            if (id == null) {
                renderJson("{\"flag\":\"500\",\"message\":\"缺少必要参数 id\"}");
                return;
            }

            // 构建 SQL 更新语句
            StringBuilder sqlBuilder = new StringBuilder("UPDATE bangdingren SET name = ?, telphone = ?, psw = ?, sex = ?, identity_card_no = ?, train_certificate_no = ?, train_certificate_indate = ?");
            List<Object> params = new ArrayList<>();
            params.add(bindName);
            params.add(bindPhone);
            params.add(bindPassword);
            params.add(bindSex);
            params.add(identityCardNo);
            params.add(trainCertificateNo);
            params.add(trainCertificateIndate);

            if (trainCertificatePhotoPath != null) {
                sqlBuilder.append(", train_certificate_image_url = ?");
                params.add(trainCertificatePhotoPath);
            }

            sqlBuilder.append(" WHERE id = ?");
            params.add(id);

            // 执行 SQL 更新操作
            int result = Db.update(sqlBuilder.toString(), params.toArray());

            if (result > 0) {
                // 更新成功，返回成功响应
                renderJson("{\"flag\":\"200\"}");
            } else {
                // 更新失败，返回失败响应
                renderJson("{\"flag\":\"500\",\"message\":\"信息更新失败，未找到对应记录\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常，返回错误响应
            renderJson("{\"flag\":\"500\",\"message\":\"请求出错，请稍后重试\"}");
        }
    }

    private String saveUploadFile(UploadFile uf) throws IOException {
        // 生成唯一文件名
        String ext = getFileExt(uf);
        String newName = UUID.randomUUID() + ext;

        // 创建目标目录
        File targetDir = new File(UPLOAD_DIR);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            throw new IOException("目录创建失败: " + targetDir.getAbsolutePath());
        }

        // 移动文件
        File destFile = new File(targetDir, newName);
        Files.move(
                uf.getFile().toPath(),
                destFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        // 返回相对路径
        return "upload/temp/data/" + newName;
    }

    private String getFileExt(UploadFile uf) {
        // 优先从文件名获取扩展名
        String fileName = uf.getFileName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            return fileName.substring(dotIndex);
        }

        // 根据MIME类型补充扩展名
        String mimeType = uf.getContentType();
        return getExtensionByMimeType(mimeType);
    }

    private String getExtensionByMimeType(String mimeType) {
        switch (mimeType) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            default:
                return ".unknown";
        }
    }
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
