package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;


@Path(value = "/", viewPath = "/bangdingren")

public class KangjiashanothergasController extends Controller {
    //private Bangdingren dao = new Bangdingren().dao();
    @Inject
    KangjiashanothergasService service;


    public void othergslist() {
        Integer pageNumber = getParaToInt("page",1);
        String stationcode = getPara("stationcode");
        String tradedate = getPara("tradedate");
        System.out.println("tradedate: " + tradedate);
        System.out.println("stationcode: " + stationcode);
        setAttr("stationcode",stationcode);
        setAttr("tradecode",tradedate);
         setAttr("othergslist", service.search(pageNumber,20,stationcode,tradedate));
        render("othergslist.html");
    }
}