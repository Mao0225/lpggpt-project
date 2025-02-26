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

//绑定奉天码和镂空码
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
        String gasnumber = jsonObj.getString("gasnumber");
        System.out.println("gasnumber=" + gasnumber + "fengtiangasno=" + fengtiangasno + "loukongno=" + loukongno);

        JSONObject json = new JSONObject();
        if (StrKit.notBlank(gasnumber)) {
            // 先检查气瓶档案是否存在
            GasFile gasFile = dao.findFirst("select * from gas_file where gas_number = ?", gasnumber);
            if (gasFile == null) {
                json.put("flag", "300");
                json.put("message", "气瓶档案不存在，请扫描二维码添加");
                renderJson(json);
                return;
            }

            // 校验封头气瓶编号是否已被其他气瓶绑定
            if (StrKit.notBlank(fengtiangasno)) {
                GasFile existingFengtian = dao.findFirst(
                        "select * from gas_file where fengtiangasno = ? and gas_number != ?",
                        fengtiangasno, gasnumber
                );
                if (existingFengtian != null) {
                    json.put("flag", "300");
                    json.put("message", "奉天码已被其他气瓶绑定");
                    renderJson(json);
                    return;
                }
            }

            // 校验镂空编号是否已被其他气瓶绑定
            if (StrKit.notBlank(loukongno)) {
                GasFile existingLoukong = dao.findFirst(
                        "select * from gas_file where loukongno = ? and gas_number != ?",
                        loukongno, gasnumber
                );
                if (existingLoukong != null) {
                    json.put("flag", "300");
                    json.put("message", "镂空码已被其他气瓶绑定");
                    renderJson(json);
                    return;
                }
            }

            // 更新字段
            boolean updated = false;
            if (StrKit.notBlank(fengtiangasno)) {
                gasFile.setFengtiangasno(fengtiangasno);
                updated = true;
            }
            if (StrKit.notBlank(loukongno)) {
                gasFile.setLoukongno(loukongno);
                updated = true;
            }

            // 如果有更新，保存到数据库
            if (updated) {
                gasFile.update();
            }

            json.put("flag", "200");
            json.put("message", "绑定成功");
            json.put("gasFile", gasFile);
            renderJson(json);
        } else {
            json.put("flag", "300");
            json.put("message", "气瓶编号不能为空");
            renderJson(json);
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
