package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.Bangdingren;

import java.io.File;
import java.util.Arrays;
import java.util.List;


@Path(value = "/", viewPath = "/bangdingren")

public class KangjiashanController extends Controller {
    //private Bangdingren dao = new Bangdingren().dao();
    @Inject
    KangjiashanService service;


    public void jiaqilist() {
        Integer pageNumber = getParaToInt("page",1);
        String name = getPara("name");
        setAttr("jiaqilist", service.paginate(pageNumber,10));
        render("jiaqilist.html");
    }
}