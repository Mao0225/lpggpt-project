package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Basshexiangtouinfo;
import com.sjzu.edu.common.model.Restaurant;

import java.util.List;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/basshexiangtouinfo")
@Before(UserInterceptor.class)
public class BasshexiangtouinfoController extends Controller {
    @Inject
    BasshexiangtouinfoService service;
    private Basshexiangtouinfo dao = new Basshexiangtouinfo().dao();
    private Restaurant restaurant= new Restaurant().dao();

    public void shexiangtoulist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String bianhao=getPara("shexiangtoubianhao");
        String addsql = "from basshexiangtouinfo ";
        if(bianhao != null && bianhao.length()!=0){
            addsql = addsql + "where shexiangtoubianhao like '%"+bianhao+"%' order by id asc";
        }else {
            addsql = addsql + "order by id asc";
        }
        System.out.println(addsql);
        setAttr("shexiangtoubianhao",bianhao);
        setAttr("basshexiangtou",dao.paginate(pageNumber,   pageSize, "select *", addsql));
        render("basshexiangtous.html");
    }

    public void edit() {
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        setAttr("restaurants",restaurants);
        setAttr("shexiangtou", service.findById(getParaToInt()));
    }
    public void update() {
        Basshexiangtouinfo basshexiangtouinfo = getModel(Basshexiangtouinfo.class, "shexiangtou");
        if (basshexiangtouinfo.update()) {
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/basshexiangtouinfo/shexiangtoulist");
        } else {
            redirect("/user/userlist");
        }
    }
    public void add() {
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        System.out.println("======================="+restaurants);
        setAttr("restaurants",restaurants);
        render("add.html");
    }
    public void save() {
        Basshexiangtouinfo basshexiangtouinfo = getModel(Basshexiangtouinfo.class, "shexiangtou");
        if (basshexiangtouinfo.save()) {
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/basshexiangtouinfo/shexiangtoulist");
        } else {
            redirect("/user/userlist");
        }
    }
    public void delete() {
        service.deleteById(getParaToInt());
        redirect("/basshexiangtouinfo/shexiangtoulist");
    }
}



