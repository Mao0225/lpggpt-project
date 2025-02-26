package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.*;

import java.util.List;
@Path(value = "/", viewPath = "/Fequipment")
public class FittingController  extends  Controller{
    @Inject
    FittingService service;
    private Station station= new Station().dao();
    public void FElist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String name = getPara("name");
        String kind = getPara("kind");
        setAttr("name",name);
        setAttr("kind",kind);
        setAttr("fequipment",service.search(pageNumber, pageSize,name,kind,getSessionAttr("stationid")));
        render("Fequipment.html");
    }
    public void oFElist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String name = getPara("name");
        String starttime = getPara("starttime");
        String endtime = getPara("endtime");




        setAttr("name", name);
        setAttr("starttime", starttime);
        setAttr("endtime", endtime);

        setAttr("ofequipment",service.osearch(pageNumber, pageSize,name,starttime,endtime,getSessionAttr("stationid")));
        render("oequipment.html");
    }
    public void edit() {
        String stations = station.find("SELECT station_name FROM gas_station WHERE id = "+ getSessionAttr("stationid")+" ").toString();
        stations = stations.substring(stations.indexOf(":") + 1, stations.lastIndexOf("}"));
            // 你可以继续使用 stationname 变量

        User user = getSessionAttr("user");
        String writer;

            // 用户存在，可以继续获取相关信息
        writer = user.getUsername();  // 假设 getStationid() 方法返回 stationid
        setAttr("writer",writer);
        setAttr("station",stations);
        setAttr("fequipment", service.findById(getParaToInt()));
    }

    public void update() {
        getBean(BseFitting.class).update();
        redirect("/Fequipment/FElist");
    }
    public void oadd() {



        // 获取第一个Station对象的station_name字段
        // 你可以继续使用 stationname 变量

        User user = getSessionAttr("user");
        String writer;

        // 用户存在，可以继续获取相关信息
        writer = user.getUsername();  // 假设 getStationid() 方法返回 stationid
        setAttr("fequipment", service.findById(getParaToInt()));
        setAttr("writer",writer);
        setAttr("stationid",getSessionAttr("stationid"));
        render("oadd.html");
    }
    public void add() {
        String stations = station.find("SELECT station_name FROM gas_station WHERE id = "+ getSessionAttr("stationid")+" ").toString();
        stations = stations.substring(stations.indexOf(":") + 1, stations.lastIndexOf("}"));

         // 获取第一个Station对象的station_name字段
        // 你可以继续使用 stationname 变量

        User user = getSessionAttr("user");
        String writer;

        // 用户存在，可以继续获取相关信息
        writer = user.getUsername();  // 假设 getStationid() 方法返回 stationid
        setAttr("writer",writer);
        setAttr("station",stations);
        setAttr("companyid",getSessionAttr("stationid"));
        render("add.html");
    }
    public void osave() {
        BseFittOperate fequipment = getModel(BseFittOperate.class, "bseFittOperate");
        System.out.println(fequipment);
        int count = fequipment.getCount();
        int number = getParaToInt("number");
        BseFitting fit = service.findById(getParaToInt("key"));
        fit.setNumber(number - count);
        fit.update();
        fequipment.save();
        // 保存成功，可以重定向到列表页面或者显示成功消息
        redirect("/Fequipment/FElist");
    }

    public void save() {
        BseFitting fequipment = getModel(BseFitting.class, "bseFitting");
        System.out.println(fequipment);
        fequipment.save();
        // 保存成功，可以重定向到列表页面或者显示成功消息
        redirect("/Fequipment/FElist");
    }

    public void delete() {
        System.out.println("delete function");
        service.deleteById(getParaToInt());
        redirect("/Fequipment/FElist");
    }


}
