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
}
