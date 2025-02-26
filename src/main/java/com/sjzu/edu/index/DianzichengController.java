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
public class DianzichengController extends Controller {
    @Inject
    DianzichengService service;

    private Restaurant restaurant= new Restaurant().dao();
    /**
     * 显示功能列表页面
     */
    public void dianzichenglist() {
        try {
            System.out.println("dianzichenglist function");
            System.out.println(service.paginate(getParaToInt(0, 1), 10).toString());
            int pageNumber = getParaToInt("page", 1); // 默认为第1页
            setAttr("dianzichengs", service.paginate(pageNumber, 10));
            render("dianzichengs.html");
        } catch (Exception e) {
            e.printStackTrace();
            renderError(500);
        }
    }

    /**
     * 编辑功能页面
     */
    public void edit() {
        try {
            setAttr("dianzicheng", service.findById(getParaToInt()));
            int id = getParaToInt();
            Dianzicheng function = service.findById(id);
            List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
            setAttr("function", function);
            setAttr("restaurants", restaurants);
            render("edit.html");
        } catch (Exception e) {
            e.printStackTrace();
            renderError(500);
        }
    }

    /**
     * 更新功能
     */
    public void update() {
        Dianzicheng function = getModel(Dianzicheng.class, "dianzicheng");
        if(function.update()) {
            redirect("/dianzicheng/dianzichenglist");
        } else {
        }
    }
    /**
     * 添加功能页面
     */
    public void add() {
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        setAttr("restaurants", restaurants);
        render("add.html");
    }

    /**
     * 保存功能
     */
    public void save() {
        Dianzicheng dianzicheng = getModel(Dianzicheng.class);
        Dianzicheng function = getModel(Dianzicheng.class, "dianzicheng");
        boolean save = function.save();
        if (save) {
            redirect("/dianzicheng/dianzichenglist");
        } else {
        }
    }

    /**
     * 删除功能
     */
    public void delete(){
        service.delete(getParaToInt());
        redirect("/dianzicheng/dianzichenglist");
    }

}


