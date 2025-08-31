package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.sjzu.edu.common.model.AppManage;


import javax.servlet.http.HttpServletResponse;


//处理app更新获取最新版本
@Path(value = "/", viewPath = "/appversion")
@Clear
public class AppVersionController extends Controller {


    private AppManage dao = new AppManage();
    /**
     * 通过appcode获取最新版本
     */
    public void getLatestVersion() {

        //测试接口地址：http://localhost:8099/appversion/getLatestVersion  参数：appcode
        //云数据库测试接口：http://114.115.156.201:8099/appversion/getLatestVersion
        addCorsHeaders(); // 添加这一行来允许跨域请求
        String appcode = getPara("appcode");//传递的参数
        System.out.println("应用更新包名："+appcode);
        AppManage app = dao.findFirst("SELECT * FROM app_manage WHERE appcode = ? ORDER BY updatetime DESC LIMIT 1", appcode);;

        JSONObject json = new JSONObject();
        if (app != null) {
            json.put("flag","200" );
            json.put("app",app);
        }else{
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
