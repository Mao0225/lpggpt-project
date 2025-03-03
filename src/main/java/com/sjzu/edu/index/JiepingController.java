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
            System.out.println(service.paginate(getParaToInt(0, 1), 10).toString());
            int pageNumber = getParaToInt("page", 1); // 默认为第1页
            setAttr("jiepinglist", service.paginate(pageNumber, 10));
            render("jieping.html");
        } catch (Exception e) {
            e.printStackTrace();
            renderError(500);
        }
    }

}


