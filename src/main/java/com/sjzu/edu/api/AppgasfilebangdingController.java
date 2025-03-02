package com.sjzu.edu.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.sjzu.edu.common.model.GasFile;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


@Path(value = "/", viewPath = "/appgasfilebangding")
public class AppgasfilebangdingController extends Controller {

    private GasFile dao = new GasFile().dao();


    //绑定镂空码和奉天码
    public void bangding() {
        addCorsHeaders();
        String jsonStr = getRawData(); // 获取原始请求体
        JSONObject jsonObj = JSON.parseObject(jsonStr); // 解析 JSON
        String loukongno = jsonObj.getString("loukongno");
        String fengtiangasno = jsonObj.getString("fengtiangasno");
        String filingGasStation = jsonObj.getString("filingGasStation");
        String gasnumber = jsonObj.getString("gasnumber");
        System.out.println("gasnumber=" + gasnumber + "fengtiangasno=" + fengtiangasno +
                "loukongno=" + loukongno + "filingGasStation=" + filingGasStation);

        JSONObject json = new JSONObject();
        if (StrKit.notBlank(gasnumber)) {
            GasFile gasFile = dao.findFirst("select * from gas_file where gas_number = ?", gasnumber);
            if (gasFile != null) {
                // 检查每个字段，如果数据库中为空且传入值不为空，就更新
                if (StrKit.isBlank(gasFile.getFengtiangasno()) && StrKit.notBlank(fengtiangasno)) {
                    gasFile.setFengtiangasno(fengtiangasno);
                }
                if (StrKit.isBlank(gasFile.getLoukongno()) && StrKit.notBlank(loukongno)) {
                    gasFile.setLoukongno(loukongno);
                }
                if (StrKit.isBlank(gasFile.getFilingGasStation()) && StrKit.notBlank(filingGasStation)) {
                    gasFile.setFilingGasStation(filingGasStation);
                }

                gasFile.update();
                json.put("flag", "200");
                json.put("message", "绑定成功");
                json.put("gasFile", gasFile);
                renderJson(json);
            } else {
                json.put("flag", "300");
                json.put("message", "气瓶档案不存在，请扫描二维码添加");
                renderJson(json);
            }
        }
    }

    public String getRequestBody() {
        // 获取请求体内容
        // 直接从请求中读取 body 内内容
        return new String(getRawData().getBytes(), StandardCharsets.UTF_8);
    }

    public void addGasFile() {
        addCorsHeaders();
        JSONObject json = new JSONObject();
        try {
            // 获取原始 JSON 请求体
            String requestBody = getRawData();
            System.out.println("请求体内容: " + requestBody);

            // 手动解析 JSON
            GasFile body = JsonKit.parse(requestBody, GasFile.class);
            System.out.println("档案信息：" + body);

            if (body.getGasNumber() != null) {
                String gasNumber = body.getGasNumber();

                GasFile gasFile = dao.findFirst("select * from gas_file where gas_number = ?", gasNumber);
                if (gasFile != null) {
                    json.put("flag", "200");
                    json.put("message", "档案已经存在");
                    json.put("gasFile", gasFile);
                    renderJson(json);
                } else {
                    boolean save = body.save();
                    if (save) {
                        json.put("flag", "200");
                        json.put("message", "档案未存在，添加成功");
                        json.put("gasFile", body);
                    } else {
                        json.put("flag", "500");
                        json.put("message", "档案添加失败");
                    }
                    renderJson(json);
                }
            } else {
                json.put("flag", "300");
                json.put("message", "缺少必要参数");
                renderJson(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("flag", "500");
            json.put("message", "系统错误");
            renderJson(json);
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
