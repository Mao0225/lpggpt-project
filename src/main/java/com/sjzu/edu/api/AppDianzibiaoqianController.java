package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Dianzibiaoqian;
import com.sjzu.edu.common.model.GasBottle;
import com.sjzu.edu.common.model.GasFile;
import com.sjzu.edu.common.model.Uploaddianzibiaoqian;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Jason 2024-03-09
 * AppbottleController//这个是电子标签信息的接口
 */
@Path(value = "/", viewPath = "/appdianzibiaoqian")
@Clear
public class AppDianzibiaoqianController extends Controller {
    //测试接口地址：http://localhost/appbottle/getBottleinfo  参数：bottlenumber：889998654360
    private Uploaddianzibiaoqian dao = new Uploaddianzibiaoqian().dao();

    public void inputdzbqdata() {
        // jason，上传电子标签
        //测试接口地址：http://localhost:8099/appdianzibiaoqian/inputdzbqdata
        //云数据库测试接口：http://114.115.156.201:8099/appdianzibiaoqian/inputdzbqdata
        addCorsHeaders(); // 添加这一行来允许跨域请求
        //参数列表
        String deviceid = getPara("deviceid");//
        String biaopianlist = getPara("biaopianlist");//
        String jingdu = getPara("jingdu");//
        String weidu = getPara("weidu");//
        String address = getPara("address");//
        String drivername = getPara("drivername");//
        String driverphone = getPara("driverphone");//
        String drivercarid = getPara("drivercarid");//
        String carno = getPara("carno");//
        String biaoqiancount = getPara("biaoqiancount");//

        LocalDateTime currentTime = LocalDateTime.now();
        // 格式化当前时间为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(currentTime);
//处理biaopianlist，处理重复数据
        String processedBiaopianlist = "";
        int uniqueCount = 0;
        if (biaopianlist != null && !biaopianlist.isEmpty()) {
            // 将字符串按逗号分割成列表
            List<String> list = Arrays.asList(biaopianlist.split(","));

            // 使用 HashSet 去除重复数据
            HashSet<String> uniqueSet = new HashSet<>(list);

            // 获取处理后的数据个数
            uniqueCount = uniqueSet.size();

            // 将处理后的列表和个数转换回需要的格式
            processedBiaopianlist = String.join(",", uniqueSet);

            // 输出结果（可以根据需要进行其他操作）
            System.out.println("Processed List: " + processedBiaopianlist);
            System.out.println("Unique Count: " + uniqueCount);

        } else {
            // 处理 biaopianlist 为空的情况
            System.out.println("biaopianlist is null or empty");
            renderJson("message", "biaopianlist is null or empty");
        }
        ////处理biaopianlist，处理重复数据结束
        Uploaddianzibiaoqian pre = new Uploaddianzibiaoqian();
        pre.setDeviceid(deviceid);
        pre.setBiaoqianlist(processedBiaopianlist);
        pre.setJingdu(jingdu);
        pre.setWeidu(weidu);
        pre.setAddress(address);
        pre.setDrivername(drivername);
        pre.setDriverphone(driverphone);
        pre.setDrivercarid(drivercarid);
        pre.setCarno(carno);
        pre.setFlag("10");
        pre.setBiaoqiancount(String.valueOf(uniqueCount));
        pre.setUploadtime(formattedDateTime);
        boolean result = pre.save();
        // 构造响应 JSON
        JSONObject json = new JSONObject();
        if (result) {
            json.put("flag", "200");
            json.put("message", "保存成功");
            json.put("data", pre);
        } else {
            json.put("flag", "300");
            json.put("message", "保存失败");
        }
        renderJson(json);
    }

    public void outputdzbqdata() {
        // jason，
        //测试接口地址：http://localhost:8099/appdianzibiaoqian/savedzbqdata
        //云数据库测试接口：http://114.115.156.201:8099/appdianzibiaoqian/savedzbqdata
        addCorsHeaders(); // 添加这一行来允许跨域请求
        //参数列表
        String deviceid = getPara("deviceid");//
        String dzbqvalue = getPara("dzbqvalue");//

        LocalDateTime currentTime = LocalDateTime.now();
        // 格式化当前时间为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(currentTime);

        Dianzibiaoqian pre = new Dianzibiaoqian();
        pre.setDeviceid(deviceid);
        pre.setDzbqvalue(dzbqvalue);
        pre.setFlag("20");

        pre.setCreattime(formattedDateTime);
        boolean result = pre.save();
        // 构造响应 JSON
        JSONObject json = new JSONObject();
        if (result) {
            json.put("flag", "200");
            json.put("message", "保存成功");
        } else {
            json.put("flag", "300");
            json.put("message", "保存失败");
        }
        renderJson(json);
    }


    public void shoujilist() {
        //手机端，获取电子标签上传记录
        //appdianzibiaoqian/shoujilist
        addCorsHeaders(); // 添加这一行来允许跨域请求

        String telephone = getPara("telephone");
        int pageNum = getParaToInt("pageNum"); // 第二个参数是默认值
        int pageSize = getParaToInt("pageSize"); // 第二个参数是默认值
        System.out.println("手机号：" + telephone);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        System.out.println("formattedDate Date: " + formattedDate);

        try {
            String fromSql = "from uploaddianzibiaoqian where 1=1 ";
            fromSql += "and driverphone = " + telephone;
            //fromSql += "and driverphone = " + telephone+ " and date(uploadtime) = '"+formattedDate+"'";
            fromSql += " order by id desc";
            System.out.println(fromSql);
            // 获取总记录数
            Uploaddianzibiaoqian totalRecord = dao.findFirst("SELECT COUNT(*) AS total FROM uploaddianzibiaoqian WHERE driverphone = " + telephone + "");
            if (totalRecord != null) {
                int total = totalRecord.getInt("total");
                System.out.println(total);
                // 获取当前页的记录
                Page<Uploaddianzibiaoqian> records = dao.paginate(pageNum, pageSize, "select *", fromSql);
                System.out.println(records.toString());
                JSONObject json = new JSONObject();
                json.put("total", total);
                json.put("rows", records);

                renderJson(json);
            } else {
                // 如果没有找到记录，可以返回一个空的JSON对象
                JSONObject json = new JSONObject();
                json.put("total", 0);
                json.put("rows", new ArrayList<GasBottle>()); // 使用 ArrayList 而不是数组
                renderJson(json);
            }
        } catch (Exception e) {
            // 这里可以添加更详细的错误处理，比如记录日志
            e.printStackTrace();
            renderError(500); // 返回服务器内部错误
        }
    }


    public void getAlldata() {
        // jason，根据扫描的二维码，获取这个设备下有几个秤
        //测试接口地址：http://localhost:8099/appdianzibiaoqian/getAlldata
        //云数据库测试接口：http://114.115.156.201:8099/appdianzibiaoqian/getAlldata
        addCorsHeaders(); // 添加这一行来允许跨域请求
        //参数列表
        // 构造响应 JSON
        List<Record> records = Db.use().find("select * from dianzibiaoqian order by id desc");

        JSONObject json = new JSONObject();
        if (records != null) {
            json.put("flag", "200");
            json.put("message", "查找成功");
            json.put("records", records);
        } else {
            json.put("flag", "300");
            json.put("message", "查询失败");
        }
        renderJson(json);
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

    public void searchList() {
        addCorsHeaders(); // 添加这一行来允许跨域请求
        String telephone = getPara("telephone");
        String code = getPara("qipingid");
        String time = getPara("riqi");
        int pageNum = getParaToInt("pageNum"); // 第二个参数是默认值
        int pageSize = getParaToInt("pageSize"); // 第二个参数是默认值
        System.out.println(code + "\n" + time + "\n" + pageNum + "\n" + pageSize + "\n");
        try {
            String fromSql = "from uploaddianzibiaoqian where 1=1 ";

            if (code != null) {
                fromSql += "and biaoqianlist like '%" + code + "%'";
            }
            if (time != null) {

                fromSql += " AND uploadtime >= '" + time + " 00:00:00' AND uploadtime < '" + time + " 23:59:59'";
            }
            fromSql += " and driverphone = " + telephone + " order by id desc";
            System.out.println(fromSql);
            // 获取总记录数
            Uploaddianzibiaoqian totalRecord = dao.findFirst("SELECT COUNT(*) AS total FROM uploaddianzibiaoqian WHERE driverphone = " + telephone + "");
            if (totalRecord != null) {
                int total = totalRecord.getInt("total");
                System.out.println(total);
                // 获取当前页的记录
                Page<Uploaddianzibiaoqian> records = dao.paginate(pageNum, pageSize, "select *", fromSql);
                System.out.println(records.toString());
                JSONObject json = new JSONObject();
                json.put("total", total);
                json.put("rows", records);

                renderJson(json);
            } else {
                // 如果没有找到记录，可以返回一个空的JSON对象
                JSONObject json = new JSONObject();
                json.put("total", 0);
                json.put("rows", new ArrayList<GasBottle>()); // 使用 ArrayList 而不是数组
                renderJson(json);
            }
        } catch (Exception e) {
            // 这里可以添加更详细的错误处理，比如记录日志
            e.printStackTrace();
            renderError(500); // 返回服务器内部错误
        }
    }

}



