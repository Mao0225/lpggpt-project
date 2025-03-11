package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Bangdingren;
import com.sjzu.edu.common.model.User;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

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
        JSONObject result = new JSONObject();
        try {
            // 接收参数
            String worker = getPara("worker");
            String jingdu = getPara("jingdu");
            String weidu = getPara("weidu");
            String address = getPara("address");
            String xiaoheziNumber = getPara("xiaohezi_number");

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
                    .set("xiaohezi_number", xiaoheziNumber);

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

}
