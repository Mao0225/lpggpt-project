package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Custromer;
import com.sjzu.edu.common.model.Station;

import java.util.List;

@Path(value = "/", viewPath = "/custromer")
public class CustomerController extends  Controller{
    @Inject
    CustomerService service;
    private Station station= new Station().dao();
    public void custromerlist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String station = getPara("station");
        String saddress = getPara("saddress");
        String customer = getPara("customer");
        String caddress = getPara("caddress");
        String telephone = getPara("telephone");


        setAttr("station", station);
        setAttr("saddress", saddress);
        setAttr("customer", customer);
        setAttr("caddress", caddress);
        setAttr("telephone", telephone);


        setAttr("custromer",service.search(pageNumber, pageSize,station,saddress,customer,caddress,telephone,getSessionAttr("stationid")));
        render("custromer.html");
    }
    public void supercustromerlist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String station = getPara("station");
        String saddress = getPara("saddress");
        String customer = getPara("customer");
        String caddress = getPara("caddress");
        String telephone = getPara("telephone");


        setAttr("station", station);
        setAttr("saddress", saddress);
        setAttr("customer", customer);
        setAttr("caddress", caddress);
        setAttr("telephone", telephone);


        setAttr("custromer",service.supersearch(pageNumber, pageSize,station,saddress,customer,caddress,telephone,getSessionAttr("stationid")));
        render("supercustromer.html");
    }
    public void edit() {
        List<Station> stations = station.find("SELECT * FROM gas_station");
        setAttr("stations",stations);
        setAttr("custromer", service.findById(getParaToInt()));
    }
    public void edits() {

        List<Station> stations = station.find("SELECT * FROM gas_station WHERE id = "+ getSessionAttr("stationid") +"");
        setAttr("stations",stations);
        setAttr("custromer", service.findById(getParaToInt()));
        render("edits.html");
    }
    public void update() {
        getBean(Custromer.class).update();
        redirect("/custromer/custromerlist");
    }
    public void updates() {
        getBean(Custromer.class).update();
        redirect("/custromer/supercustromerlist");
    }
    public void add() {
        List<Station> stations = station.find("SELECT * FROM gas_station ");
        setAttr("stations",stations);
        render("add.html");
    }
    public void adds() {
        List<Station> stations = station.find("SELECT * FROM gas_station WHERE id = "+ getSessionAttr("stationid") +"");
        setAttr("stations",stations);
        render("adds.html");
    }
    public void save() {
        Custromer custromer = getModel(Custromer.class, "custromer");
        System.out.println(custromer);
        custromer.save();
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/custromer/supercustromerlist");

    }
    public void saves() {
        Custromer custromer = getModel(Custromer.class, "custromer");
        System.out.println(custromer);
        custromer.save();
        // 保存成功，可以重定向到列表页面或者显示成功消息
        redirect("/custromer/custromerlist");

    }
    public void delete() {
        System.out.println("delete function");
        service.deleteById(getParaToInt());
        redirect("/custromer/custromerlist");
    }
    public void deletes() {
        System.out.println("delete function");
        service.deleteById(getParaToInt());
        redirect("/custromer/supercustromerlist");
    }
}
