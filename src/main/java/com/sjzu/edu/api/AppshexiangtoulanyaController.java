package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Shexiangtou;


import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Path(value = "/", viewPath = "/applanya")

public class AppshexiangtoulanyaController extends Controller {
    //测试接口地址：http://localhost:8099/shexiangtoulanya/shexinagtoulist
    public void shexiangtoulist() {
        addCorsHeaders();

        String camerid = getPara("camerid", "");
        int pageNum = Integer.parseInt(getPara("pageNum","1"));  // 接收pageNum
        int pageSize = Integer.parseInt(getPara("pageSize","10"));  // 接收pageSize
        int offset = (pageNum - 1) * pageSize;  // 计算offset

        List<Record> cameraNames = Db.use().find("SELECT DISTINCT shexiangtouno FROM shexiangtoulanya");
        List<Record> records = Db.use().find("SELECT shexiangtoulanya.*, basshexiangtouinfo.restaurantname, basshexiangtouinfo.restaurantid, basshexiangtouinfo.restaurantphone, basshexiangtouinfo.restaurantaddress " +
                "FROM shexiangtoulanya LEFT JOIN basshexiangtouinfo ON shexiangtoulanya.shexiangtouno = basshexiangtouinfo.shexiangtoubianhao WHERE shexiangtouno LIKE '%"+camerid+"%' order by shexiangtoulanya.id desc LIMIT "+ pageSize + " OFFSET " + offset);

        System.out.println("asdsdasdasda"+records);

        JSONObject json = new JSONObject();

        if (records != null) {
            json.put("flag","200" );
            json.put("lanyalist",records );
            json.put("cameraNames", cameraNames);
            json.put("total", Db.use().queryInt("SELECT COUNT(*) FROM shexiangtoulanya WHERE shexiangtouno LIKE '%"+camerid+"%'"));  // 返回总记录数
        } else {
            json.put("flag","300" );
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
}
