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
        // 接收参数
        String camerid = getPara("camerid", "");
        String fire = getPara("fire", ""); // 接收fire参数，默认为空
        int pageNum = Integer.parseInt(getPara("pageNum"));
        int pageSize = Integer.parseInt(getPara("pageSize"));
        int offset = (pageNum - 1) * pageSize;

        // 构建查询条件
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("WHERE shexiangtouno LIKE '%").append(camerid).append("%'");

        // 如果fire参数为10，则添加额外条件
        if ("10".equals(fire)) {
            whereClause.append(" AND shexiangtou.alarmmes LIKE '%fire%'");
        }

        // 构建SQL语句
        String sql = "SELECT shexiangtou.*, basshexiangtouinfo.restaurantname, basshexiangtouinfo.restaurantid, " +
                "basshexiangtouinfo.restaurantphone, basshexiangtouinfo.restaurantaddress " +
                "FROM shexiangtou LEFT JOIN basshexiangtouinfo " +
                "ON shexiangtou.shexiangtouno = basshexiangtouinfo.shexiangtoubianhao " +
                whereClause.toString() +
                " ORDER BY shexiangtou.id DESC LIMIT " + pageSize + " OFFSET " + offset;

        // 执行查询
        List<Record> cameraNames = Db.use().find("SELECT DISTINCT shexiangtouno FROM shexiangtou");
        List<Record> records = Db.use().find(sql);

        System.out.println("查询结果：" + records);

        // 构建返回JSON
        JSONObject json = new JSONObject();
        if (records != null) {
            // 构建总记录数查询SQL
            String countSql = "SELECT COUNT(*) FROM shexiangtou " + whereClause.toString();

            json.put("flag", "200");
            json.put("shexiangtoulist", records);
            json.put("cameraNames", cameraNames);
            json.put("total", Db.use().queryInt(countSql));
        } else {
            json.put("flag", "300");
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
