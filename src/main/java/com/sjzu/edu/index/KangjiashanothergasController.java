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
        String tradedateStart = getPara("tradedateStart");
        String tradedateEnd = getPara("tradedateEnd");
        System.out.println("tradedateStart: " + tradedateStart);
        System.out.println("tradedateEnd: " + tradedateEnd);
        System.out.println("stationcode: " + stationcode);
        setAttr("stationcode",stationcode);
        setAttr("tradedateStart", tradedateStart);
        setAttr("tradedateEnd", tradedateEnd);
         setAttr("othergslist", service.search(pageNumber,20,stationcode,tradedateStart,tradedateEnd));
        render("othergslist.html");
    }
}