package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Func;
import com.sjzu.edu.common.model.User;

/**
 * 前台  controller service dao
 * **/
@Path(value = "/", viewPath = "/func")
public class FuncController extends Controller {

    @Inject
    FuncService service;
    public void funclist(){
        Integer pageNumber=getParaToInt("page",1);
        setAttr("funcs", service.paginate(pageNumber, 10));
        render("funcs.html");

    }
    public void edit() {
        setAttr("func", service.findById(getParaToInt()));
    }
    public void add(){
        redirect("add.html");
    }
    public void save(){
        getBean(Func.class).save();
        redirect("/func/funclist");
    }

    public void update(){
        getBean(Func.class).update();
        redirect("/func/funclist");
    }

    public void delete(){
        service.delete(getParaToInt());
        redirect("/func/funclist");
    }

}
