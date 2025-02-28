package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.sjzu.edu.common.model.GasStation;
import com.sjzu.edu.common.model.GasStationStaff;

public class GgasStationStaffController extends Controller {
    @Inject
    GgasStationStaffService  service;

    private GasStation Station= new GasStation().dao();
    private GasStationStaff Staff= new GasStationStaff().dao();

    public void GasStationStaffList() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String staff_name = getPara("staff_name");
        String station_name = getPara("station_name");
        String stationid = getSessionAttr("companyid");
        setAttr("staff_name",staff_name);
        setAttr("station_name",station_name);
        setAttr("GasStationStaff", service.paginate(pageNumber, pageSize,staff_name,station_name,stationid));
        render("GasStationStaff.html");
    }

    public void edit() {
        //下拉框中信息
        setAttr("gasStations", Station.find("select * from gas_station  WHERE id ="+ getSessionAttr("stationid") +""));
        setAttr("GasStationStaff", service.findById(getParaToInt()));
// 现在你可以使用 stationName
    }

    public void update() {
        GasStationStaff staff = getModel(GasStationStaff.class, "GasStationStaff");
        if(staff.update()) {
            redirect("/GgasStationStaff/GasStationStaffList");
        } else {
            System.out.println("asdkalsjdasidjiasojdiasdasdsa");
        }
    }
    public void add() {
        //下拉框中信息
        setAttr("gasStations", Station.find("select * from gas_station WHERE id ="+ getSessionAttr("stationid") +""));
        render("add.html");
    }
    public void save() {
        GasStationStaff d = getModel(GasStationStaff.class, "GasStationStaff");
        boolean save = d.save();
        if (save) {

            redirect("/GgasStationStaff/GasStationStaffList");
        } else {
        }
    }
    public void delete() {
        Staff.deleteById(getParaToInt());
        redirect("/GgasStationStaff/GasStationStaffList");
    }
}
