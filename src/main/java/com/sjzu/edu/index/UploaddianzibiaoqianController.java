package com.sjzu.edu.index;


import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;


/**
 * 这个是查看气瓶的界面
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/uploaddianzibiaoqian")
//@Before(UserInterceptor.class) 没用上的解释器
public class UploaddianzibiaoqianController extends Controller {
    //private User dao = new User().dao();
    @Inject
    UploaddianzibiaoqianService service;

    public void getbiaoqianlist() {
        System.out.println(service.paginate(getParaToInt(0, 1), 10).toString());
        String drivername = getPara("drivername");
        String biaoqian = getPara("biaoqian");
        String drivercarno = getPara("drivercarno");
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        setAttr("dianzibiaoqianlist",service.search(pageNumber, pageSize,drivername,drivercarno,biaoqian));

        setAttr("drivername", drivername);
        setAttr("biaoqian", biaoqian);
        setAttr("drivercarno", drivercarno);
     //   setAttr("dianzibiaoqianlist", service.paginate(getParaToInt(0, 1), 10));
        render("dianzibiaoqianlist.html");

    }
}



