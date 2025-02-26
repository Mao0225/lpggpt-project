package com.sjzu.edu.api;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Db;
import com.sjzu.edu.common.model.GasFile;
import com.sjzu.edu.common.model.Shexiangtou;
import com.jfinal.core.Controller;

@Path(value = "/", viewPath = "/appscanqcode")
@Clear



public class AppscanqcodeController extends Controller {

    private GasFile dao = new GasFile().dao();  // 初始化 dao

    public void scan() {
        // 获取二维码内容参数
        String fengtiangasno = getPara("gas_number");

        // 输出接收到的二维码参数
        System.out.println("Received fengtiangasno: " + fengtiangasno);  // 打印接收到的 gas_number

        // 根据 gas_number 从数据库中查询 gas_file 表
        GasFile gasFile = dao.findFirst("SELECT * FROM gas_file WHERE fengtiangasno = ?", fengtiangasno);

        // 如果找到了相关数据
        if (gasFile != null) {
            // 直接输出整个查询到的 GasFile 对象
            System.out.println("Query successful. Found gasFile: " + gasFile);  // 打印整个 gasFile 对象

            // 将查询结果通过 setAttr() 方法传递到前端页面
            setAttr("gas_number", fengtiangasno);
            setAttr("gas_file", gasFile);

            // 跳转到模板页面并渲染
            render("Qcodegasfile.html");
        } else {
            // 如果没有找到相关记录，记录日志并跳转到404页面
            System.out.println("No data found for gas_number: " + fengtiangasno);  // 打印错误信息
            setAttr("fengtiangasno", fengtiangasno);  // 将编号传递给404页面
            render("404.html");
        }
    }
}



