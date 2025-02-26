package com.sjzu.edu.index;


import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;



/**
 * 这个是查看气瓶的界面
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/user")
//@Before(UserInterceptor.class) 没用上的解释器
public class PressureValueRecordsController extends Controller {
    //private User dao = new User().dao();
    @Inject
    PressureValueRecordsService service;


    public void pressurevaluelist() {

        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String controllerid = getPara("controllerid");
        String status = getPara("status");
        setAttr("controllerid",controllerid);
        setAttr("status",status);
        System.out.println(controllerid+"____"+status);
        System.out.println(service.search(pageNumber, pageSize,controllerid,status));
        setAttr("pressurevaluelist", service.search(pageNumber, pageSize,controllerid,status));
        render("pressurevalues.html");
    }
}



