package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Transport;

public class SetransController  extends Controller {
    SetransServcie service = new SetransServcie();
    public void transportlist(){
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);
        String stationname = getPara("stationname");
        System.out.println("stationname: "+stationname);
        String upTime = getPara("uptime");
        String downTime = getPara("downtime");
        System.out.println("upTime: "+upTime);
        setAttr("stationname", stationname);
        setAttr("uptime", upTime);
        setAttr("downtime", downTime);
        Page<Transport> transportPage = service.paginate(pageNumber, pageSize, stationname, upTime, downTime);
        setAttr("Transportlist", transportPage);
        System.out.println("transportPage: "+transportPage);
        render("setrans.html");
    }

    public void add(){

    }
    public void delete(){
        Integer id = getParaToInt("id");
        System.out.println("id: "+id);
        service.deleteById(id);
        redirect("/setrans/transportlist");
    }

}
