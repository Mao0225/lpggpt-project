package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Func;
import com.sjzu.edu.common.model.Restaurant;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/restaurant")
@Before(UserInterceptor.class)
public class RestaurantController extends Controller {
    //private User dao = new User().dao();
    @Inject
    RestaurantService service;
    public void reslist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String restaurantno = getPara("restaurantno");
        String name = getPara("name");
        String address = getPara("address");
        String leader = getPara("leader");
        String telephone = getPara("telephone");
        String chengno = getPara("chengno");

        setAttr("restaurantno", restaurantno);
        setAttr("name", name);
        setAttr("address", address);
        setAttr("leader", leader);
        setAttr("telephone", telephone);
        setAttr("chengno", chengno);

        setAttr("restaurant",service.search(pageNumber, pageSize,restaurantno,name,address,leader,telephone,chengno));
        render("restaurants.html");
    }
    public void searchdata(){
        String restaurantno = getPara("restaurantno");
        String name = getPara("name");
        String address = getPara("address");
        String leader = getPara("leader");
        String telephone = getPara("telephone");
        String chengno = getPara("chengno");
        System.out.println(service.search(1, 10,restaurantno,name,address,leader,telephone,chengno));
        setAttr("restaurant",service.search(1, 10,restaurantno,name,address,leader,telephone,chengno));
        render("restaurants.html");
    }
    public void edit() {
        setAttr("restaurant", service.findById(getParaToInt()));
    }
    public void update() {
        getBean(Restaurant.class).update();
        redirect("/restaurant/reslist");
    }
    public void add() {
        redirect("add.html");

    }
    public void save() {
        Restaurant restaurant = getModel(Restaurant.class, "restaurant");
        System.out.println(restaurant);
        if (restaurant.save()) {
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/restaurant/reslist");
        } else {
            redirect("/user/userlist");
        }
    }
    public void delete() {
        System.out.println("delete function");
        service.deleteById(getParaToInt());
        redirect("/restaurant/reslist");
    }
}



