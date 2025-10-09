package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.GasStation;
import com.sjzu.edu.common.model.IotSyncRdsRecordsV3;
import com.sjzu.edu.common.model.Restaurant;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class XhzinfoController  extends Controller {
    XhzinfoService service = new XhzinfoService();
    private Restaurant restaurant = new Restaurant().dao();
    public void xhzlist() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);
        String restaurantId = getPara("restaurantId"); // 前端参数名保持一致
        Integer alarm = getParaToInt("alarm");

        System.out.println("alarm: " + alarm);
        System.out.println("restaurantId: " + restaurantId);

        // 根据restaurantId查询关联的xiaoheziid
        String xiaoheziid = null;
        if (restaurantId != null && !restaurantId.isEmpty()) {
            Restaurant restaurantObj = restaurant.findById(restaurantId);
            if (restaurantObj != null) {
                xiaoheziid = restaurantObj.getXiaohezi();
                System.out.println("查询到的xiaoheziid: " + xiaoheziid);
            } else {
                System.out.println("未找到id为" + restaurantId + "的餐厅记录");
            }
        }

        // 查询分页数据（Service层已完成时间转换）
        Page<Record> recordPage = service.paginate(pageNumber, pageSize, xiaoheziid, alarm);

        // 传递参数到页面（直接使用Service处理后的结果）
        setAttr("restaurantId", restaurantId);
        setAttr("alarm", alarm);
        setAttr("recordlist", recordPage); // 包含Service转换后的formatted_created_time
        setAttr("restaurants", restaurant.find("SELECT * FROM restaurant"));
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

