package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Dianzicheng;
import com.sjzu.edu.common.model.Restaurant;

import java.util.List;

/**
 * 功能管理 Controller
 */
@Path(value = "/", viewPath = "/dianzicheng")
@Before(DianzichengInterceptor.class)
public class JiepingController extends Controller {
    @Inject
    JiepingService service;

    private Restaurant restaurant= new Restaurant().dao();

    /**
     * 显示功能列表页面
     */
    public void jiepinglist() {
        try {
            System.out.println("jieping function");
            int pageNumber = getParaToInt("page", 1); // 默认为第1页
            String shexiangtouno = getPara("shexiangtouno"); // 获取摄像头编号参数
            String date = getPara("date"); // 获取日期参数
            String time = getPara("time"); // 获取时间参数

            setAttr("jiepinglist", service.paginate(pageNumber, 10, shexiangtouno, date, time));

            // 将检索参数传递到页面，用于保持表单状态
            setAttr("shexiangtouno", shexiangtouno);
            setAttr("date", date);
            setAttr("time", time);

            render("jieping.html");
        } catch (Exception e) {
            e.printStackTrace();
            renderError(500);
        }
    }
}


