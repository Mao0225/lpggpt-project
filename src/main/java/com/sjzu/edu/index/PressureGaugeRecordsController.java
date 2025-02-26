package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Basshexiangtouinfo;
import com.sjzu.edu.common.model.PressureGaugeRecords;
import com.sjzu.edu.common.model.Restaurant;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/pressuregauge")
@Before(UserInterceptor.class)
public class PressureGaugeRecordsController extends Controller {
    @Inject
    PressureGaugeRecordsService service;
    private PressureGaugeRecords dao = new PressureGaugeRecords().dao();
    private Restaurant restaurant= new Restaurant().dao();

    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new java.util.Date(timestamp.getTime()));
    }

    public void edit() {
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        PressureGaugeRecords record = service.findById(getParaToInt());
        // 格式化日期
        record.set("created_time", formatDate(record.getTimestamp("created_time")));
        record.set("updated_time", formatDate(record.getTimestamp("updated_time")));
        record.set("deleted_time", formatDate(record.getTimestamp("deleted_time")));
        // 将格式化后的数据发送到前端
        setAttr("pressuregauge", record);
        setAttr("restaurants",restaurants);
        render("edit.html");
    }
    public void pressuregaugelist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String name = getPara("name");
        String select = "SELECT t_pressure_gauge_records.*,restaurant.name AS restaurantname,restaurant.address,restaurant.telephone";
        String from = "FROM t_pressure_gauge_records LEFT JOIN restaurant ON t_pressure_gauge_records.affiliation_user_id = restaurant.id";
        if(name != null && name.length()!=0){
            from = from + " where t_pressure_gauge_records.name like '%"+name+"%' order by id asc";
        }
        setAttr("name",name);
        setAttr("pressuregauge",dao.paginate(pageNumber,   pageSize, select, from));
        System.out.println(dao.paginate(pageNumber,   pageSize, select, from).getList());
        render("pressuregauges.html");
    }


    public void update() {
        PressureGaugeRecords pressure = getModel(PressureGaugeRecords.class, "pressuregauge");
        if (pressure.update()) {
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/pressuregauge/pressuregaugelist");
        } else {
            redirect("/user/userlist");
        }
    }
    public void add() {
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        setAttr("restaurants",restaurants);
        render("add.html");
    }
    public void save() {
        PressureGaugeRecords pressureGaugeRecords= getModel(PressureGaugeRecords.class, "pressuregauge");
        if (pressureGaugeRecords.save()) {
            // 保存成功，可以重定向到列表页面或者显示成功消息
            redirect("/pressuregauge/pressuregaugelist");
        } else {
            redirect("/user/userlist");
        }
    }
    public void delete() {
        service.deleteById(getParaToInt());
        redirect("/pressuregauge/pressuregaugelist");
    }
}



