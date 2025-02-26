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

@Path(value = "/", viewPath = "/appbaojing")

public class AppshexiangtoubaojingController extends Controller {
    public void shexiangtoulist() {
        addCorsHeaders();
//这个应该是给手机端返回摄像头列表 功能
        String camerid = getPara("camerid", "");
        int pageNum = Integer.parseInt(getPara("pageNum"));  // 接收pageNum
        int pageSize = Integer.parseInt(getPara("pageSize"));  // 接收pageSize
        int offset = (pageNum - 1) * pageSize;  // 计算offset

        List<Record> cameraNames = Db.use().find("SELECT DISTINCT shexiangtouno FROM shexiangtou");
        List<Record> records = Db.use().find("SELECT shexiangtou.*, basshexiangtouinfo.restaurantname, basshexiangtouinfo.restaurantid, basshexiangtouinfo.restaurantphone, basshexiangtouinfo.restaurantaddress " +
                "FROM shexiangtou LEFT JOIN basshexiangtouinfo ON shexiangtou.shexiangtouno = basshexiangtouinfo.shexiangtoubianhao WHERE shexiangtouno LIKE '%"+camerid+"%' order by shexiangtou.id desc LIMIT "+ pageSize + " OFFSET " + offset);

        System.out.println("asdsdasdasda"+records);

        JSONObject json = new JSONObject();

        if (records != null) {
            json.put("flag","200" );
            json.put("shexiangtoulist",records );
            json.put("cameraNames", cameraNames);
            json.put("total", Db.use().queryInt("SELECT COUNT(*) FROM shexiangtou WHERE shexiangtouno LIKE '%"+camerid+"%'"));  // 返回总记录数
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
