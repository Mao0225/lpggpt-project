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
            // 1.接收基础参数
            String worker = getPara("worker");
            String jingdu = getPara("jingdu");
            String weidu = getPara("weidu");
            String address = getPara("address");
            String xiaoheziNumber = getPara("xiaohezi_number");

            // 2.参数校验
            if (xiaoheziNumber == null || xiaoheziNumber.isEmpty()) {
                result.put("code", 400);
                result.put("msg", "小盒子编号不能为空");
                renderJson(result);
                return;
            }

            // 3.创建单条记录
            Record record = new Record();
            // 设置基础字段
            record.set("worker", worker)
                    .set("jingdu", jingdu)
                    .set("weidu", weidu)
                    .set("address", address)
                    .set("xiaohezi_number", xiaoheziNumber);

            // 4.处理4个气瓶数据
            for (int i = 1; i <= 4; i++) {
                String gasNumber = getPara("gas_number" + i);
                String tongdao = getPara("tongdao" + i);

                // 动态设置字段（gas_number1、tongdao1等）
                if (gasNumber != null && !gasNumber.isEmpty()) {
                    record.set("gas_number" + i, gasNumber)
                            .set("tongdao" + i, tongdao);
                } else {
                    // 如果未填写则设为空
                    record.set("gas_number" + i, "")
                            .set("tongdao" + i, "");
                }
            }

            // 5.保存单条记录
            try {
                Db.save("bse_xiaohezi", record);
                result.put("code", 200);
                result.put("msg", "数据保存成功");
            } catch (Exception e) {
                result.put("code", 500);
                result.put("msg", "保存失败: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
            e.printStackTrace();
        }
        renderJson(result);
    }

}
