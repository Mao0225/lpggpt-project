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
        // 将接收的参数名从companyId改为restaurantId，与前端保持一致
        String restaurantId=getPara("restaurantId");
        Integer alarm = getParaToInt("alarm");
        System.out.println("alarm: "+alarm);
        System.out.println("restaurantId: "+restaurantId);

        // 根据restaurantId查询对应的xiaohezi
        String xiaoheziid = null;
        if (restaurantId != null && !restaurantId.isEmpty()) {
            // 查询restaurant表中对应id的记录的xiaohezi字段
            Restaurant restaurantObj = restaurant.findById(restaurantId);
            if (restaurantObj != null) {
                xiaoheziid = restaurantObj.getXiaohezi();
                System.out.println("查询到的xiaoheziid: " + xiaoheziid);
            } else {
                System.out.println("未找到id为" + restaurantId + "的restaurant记录");
            }
        }

        // 传递到页面的参数名保持restaurantId（前端显示用）
        setAttr("restaurantId",restaurantId);
        setAttr("alarm",alarm);
        // 调用service时使用查询到的xiaoheziid
        Page<Record> recordPage = service.paginate(pageNumber, pageSize, xiaoheziid, alarm);
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

