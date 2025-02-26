package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Shexiangtoulanya;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/shexiangtoulanya")
@Before(UserInterceptor.class)
public class ShexiangtoulanyaController extends Controller {
    //private User dao = new User().dao();
    @Inject
    RestaurantService service;
    private Shexiangtoulanya dao = new Shexiangtoulanya().dao();
    public void shexiangtoulanyalist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String select = "SELECT shexiangtoulanya.*, basshexiangtouinfo.restaurantname,basshexiangtouinfo.restaurantid,basshexiangtouinfo.restaurantphone,basshexiangtouinfo.restaurantaddress";
        String from = "FROM shexiangtoulanya LEFT JOIN basshexiangtouinfo ON shexiangtoulanya.shexiangtouno = basshexiangtouinfo.shexiangtoubianhao order by shexiangtoulanya.id desc";
        setAttr("shexiangtoulanya",dao.paginate(pageNumber, pageSize, select,from));
        render("shexiangtoulanyas.html");
    }
}



