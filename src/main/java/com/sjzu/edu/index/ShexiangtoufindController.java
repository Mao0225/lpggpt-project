package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Shexiangtou;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/shexiangtoufind")
public class ShexiangtoufindController extends Controller {
    @Inject
    RestaurantService service;
    private Shexiangtou dao = new Shexiangtou().dao();

    public void shexiangtoulist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String shexiangtouno = getPara("shexiangtouno"); // 获取参数

        // 构建查询语句
        String select = "SELECT shexiangtou.*, basshexiangtouinfo.restaurantname, basshexiangtouinfo.restaurantid, basshexiangtouinfo.restaurantphone, basshexiangtouinfo.restaurantaddress";
        String from = "FROM shexiangtou LEFT JOIN basshexiangtouinfo ON shexiangtou.shexiangtouno = basshexiangtouinfo.shexiangtoubianhao";

        // 添加条件
        if (shexiangtouno != null && !shexiangtouno.trim().isEmpty()) {
            from += " WHERE shexiangtou.shexiangtouno LIKE '%" + shexiangtouno + "%'";
        }

        from += " ORDER BY shexiangtou.id DESC";
        if (shexiangtouno == null){
            setAttr("shexiangtouno", "");

        }else{
        setAttr("shexiangtouno", shexiangtouno);
        }

        setAttr("shexiangtou", dao.paginate(pageNumber, pageSize, select, from));
        render("shexiangtous.html");
    }
}




