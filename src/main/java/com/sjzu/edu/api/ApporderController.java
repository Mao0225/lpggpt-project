package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.json.Json;
import com.jfinal.plugin.activerecord.Db;
import com.sjzu.edu.common.model.Jiebangding;

import java.util.List;

@Path(value = "/", viewPath = "/apporder")
@Clear
public class ApporderController extends Controller {

    private Jiebangding dao = new Jiebangding().dao();

    public void order() {
        String chengbianma = getPara("chengbianma");
        String flag = getPara("flag");


//        System.out.println("chengbianma:"+chengbianma);
//        System.out.println("flag:"+flag);

        // 检查是否为空
        if (chengbianma == null ||flag == null) {
            renderJson(new JSONObject().fluentPut("error", "Received null or 'undefined' parameters"));
            return;
        }

        String sql =String.format("SELECT status,id,bangdingshijian,bdqcszl,bdqdqzl,bdqsyzl,jbdhcszl," +
                "jbdhdqzl,jbdhsyzl,danjia FROM jiebangding WHERE chengbianma = '%s' AND flag = %s AND status = '30' ORDER BY id DESC ",chengbianma,flag);

//        System.out.println("Generated SQL: " + sql);

        List<Jiebangding> jiebangdingList = dao.find(sql);

//        System.out.println(jiebangdingList);

        JSONObject json = new JSONObject();

        if (jiebangdingList != null && !jiebangdingList.isEmpty()) {
            json.put("flag", "200");
            json.put("order",jiebangdingList);

        } else {
            json.put("flag", "300");
        }

        renderJson(json);
    }
       public void historyorder() {
        String chengbianma = getPara("chengbianma");
        String flag = getPara("flag");

//        System.out.println("chengbianma:"+chengbianma);
//        System.out.println("flag:"+flag);

        // 检查是否为空
        if (chengbianma == null ||flag == null) {
            renderJson(new JSONObject().fluentPut("error", "Received null or 'undefined' parameters"));
            return;
        }

        String sql =String.format("SELECT status,bangdingshijian,bdqcszl,bdqdqzl,bdqsyzl,jbdhcszl,jbdhdqzl,jbdhsyzl,danjia FROM jiebangding WHERE chengbianma = '%s' AND flag = %s AND status = '20' ORDER BY id DESC ",chengbianma,flag);

//        System.out.println("Generated SQL: " + sql);

        List<Jiebangding> jiebangdingList = dao.find(sql);

//        System.out.println(jiebangdingList);

        JSONObject json = new JSONObject();

        if (jiebangdingList != null && !jiebangdingList.isEmpty()) {
            json.put("flag", "200");
            json.put("order",jiebangdingList);

        } else {
            json.put("flag", "300");
        }

        renderJson(json);
    }
    public void update() {
        String id = getPara("id");
        // 检查是否为空
        if (id == null ) {
            renderJson(new JSONObject().fluentPut("error", "Received null or 'undefined' parameters"));
            return;
        }

        String updateSql = "UPDATE jiebangding SET status = '20' WHERE id = ? AND status = '30'";

        int affectedRows = Db.update(updateSql, id);
        JSONObject json = new JSONObject();

        if(affectedRows > 0) {
            System.out.println("更新成功");
            json.put("update", "success");
        } else {
            System.out.println("更新失败");
            json.put("update", "failed");
        }

        renderJson(json);
    }
}
