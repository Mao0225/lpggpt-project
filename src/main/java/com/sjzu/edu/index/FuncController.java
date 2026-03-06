package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.sjzu.edu.common.model.Func;

/**
 * 前台  controller service dao
 * **/
@Path(value = "/", viewPath = "/func")
public class FuncController extends Controller {

    @Inject
    FuncService service;
    public void funclist(){
        Integer pageNumber=getParaToInt("page",1);
        String searchKey = getPara("searchKey");
        if (StrKit.notBlank(searchKey)) {
            searchKey = searchKey.trim();
        } else {
            searchKey = null;
        }
        setAttr("searchKey", searchKey);
        setAttr("funcs", service.paginate(pageNumber, 10, searchKey));
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
