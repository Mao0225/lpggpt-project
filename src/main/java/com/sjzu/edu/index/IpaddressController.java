package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Ipaddress;
import java.text.SimpleDateFormat;
import java.util.Date;

@Path(value = "/", viewPath = "/ipaddress")
@Before(UserInterceptor.class)
public class IpaddressController extends Controller {

    @Inject
    IpaddressService service;

    public void iplist() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);
        String deviceno = getPara("deviceno");
        String poweronoff = getPara("poweronoff");
        String ipaddress = getPara("ipaddress");
        String happendtime = getPara("happendtime");

        setAttr("deviceno", deviceno);
        setAttr("poweronoff", poweronoff);
        setAttr("ipaddress", ipaddress);
        setAttr("happendtime", happendtime);

        setAttr("ipaddressPage", service.search(pageNumber, pageSize, deviceno, poweronoff, ipaddress, happendtime));
        render("ipaddress.html");
    }

    public void searchdata() {
        String deviceno = getPara("deviceno");
        String poweronoff = getPara("poweronoff");
        String ipaddress = getPara("ipaddress");
        String happendtime = getPara("happendtime");

        setAttr("deviceno", deviceno);
        setAttr("poweronoff", poweronoff);
        setAttr("ipaddress", ipaddress);
        setAttr("happendtime", happendtime);
        setAttr("ipaddressPage", service.search(1, 10, deviceno, poweronoff, ipaddress, happendtime));
        render("ipaddress.html");
    }

    public void edit() {
        setAttr("ipaddress", service.findById(getParaToInt()));
        render("edit.html");
    }

    public void update() {
        Ipaddress ipaddress = getBean(Ipaddress.class);
        ipaddress.update();
        redirect("/ipaddress/iplist");
    }

    public void add() {
        render("add.html");
    }

    public void save() {
        try {
            Ipaddress ipaddress = getModel(Ipaddress.class, "ipaddress");

            // 自动填充当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ipaddress.setHappendtime(sdf.format(new Date()));

            if (ipaddress.save()) {
                redirect("/ipaddress/iplist");
            } else {
                setAttr("errorMsg", "保存失败，请检查数据");
                render("add.html");
            }
        } catch (Exception e) {
            setAttr("errorMsg", "保存失败：" + e.getMessage());
            render("add.html");
        }
    }

    public void delete() {
        service.deleteById(getParaToInt());
        redirect("/ipaddress/iplist");
    }
}