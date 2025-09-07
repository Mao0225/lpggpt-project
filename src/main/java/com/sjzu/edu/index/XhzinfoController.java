package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.GasStation;
import com.sjzu.edu.common.model.IotSyncRdsRecordsV3;
import com.sjzu.edu.common.model.Restaurant;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class XhzinfoController  extends Controller {
    XhzinfoService service = new XhzinfoService();
    private Restaurant restaurant = new Restaurant().dao();
    public void xhzlist(){
        int pageNumber=getParaToInt("page",1);
        int pageSize=getParaToInt("size",10);

        String companyId = getPara("companyId", "");
        String xiaohezi = getPara("xiaohezi", "");
        String alarmStr = getPara("alarm", "");
        Integer alarm = null;
        if (alarmStr != null && !alarmStr.trim().isEmpty()) {
            try {
                alarm = Integer.valueOf(alarmStr);
            } catch (NumberFormatException e) {
                System.out.println("alarm 参数不是数字: " + alarmStr);
            }
        }
        setAttr("companyId", companyId);
        setAttr("xiaohezi", xiaohezi);
        setAttr("alarm", alarmStr);

// 调用 service
        Page<Record> recordPage = service.paginate(pageNumber, pageSize, xiaohezi, alarm);

        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        setAttr("recordlist",recordPage);
        setAttr("restaurants",restaurants);
        render("xhzlist.html");
    }
    public String convertTimestamp(String timestampStr) {
        try {
            long timestamp = Long.parseLong(timestampStr);
            Instant instant = Instant.ofEpochMilli(timestamp);
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Shanghai")); // 指定时区
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return zonedDateTime.format(formatter);
        } catch (NumberFormatException e) {
            return "无效时间戳";
        }
    }

    public void delete() {
        // 获取要删除记录的 id
        Integer id = getParaToInt("id");
        // 打印要删除的记录 id，方便调试
        System.out.println("id: " + id);

        // 调用 Service 层的删除方法
        service.deleteById(id);

        // 重定向到列表页面
        redirect("/xhzinfo/xhzlist");
    }
}

