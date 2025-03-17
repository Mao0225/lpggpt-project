package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.GasStation;
import com.sjzu.edu.common.model.IotSyncRdsRecordsV3;
import com.sjzu.edu.common.model.Restaurant;

import java.util.List;

public class XhzinfoController  extends Controller {
    XhzinfoService service = new XhzinfoService();
    private Restaurant restaurant = new Restaurant().dao();
    public void xhzlist(){
        int pageNumber=getParaToInt("page",1);
        int pageSize=getParaToInt("size",10);
        String xiaohezi=getPara("companyId");
        System.out.println("xiaohezi: "+xiaohezi);
        setAttr("xiaohezi",xiaohezi);
        Page<Record> recordPage = service.paginate(pageNumber, pageSize,xiaohezi);
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        setAttr("recordlist",recordPage);
        setAttr("restaurants",restaurants);
        render("xhzlist.html");
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

