package com.sjzu.edu.index;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderInfoController extends Controller {
    OrderInfoService service = new OrderInfoService();
   private Restaurant restaurant = new Restaurant().dao();
   private GasStation gastation = new GasStation().dao();
   private BillInfo  billdao= new BillInfo().dao();
   private Drivercar drivercar = new Drivercar().dao();
    public void orderlist(){
        int PageNumber = getParaToInt("page", 1);
        int PageSize = getParaToInt("size", 10);
        String time = getPara("time");
        String station = getPara("station");
        String restaurantId = getPara("companyId");
        System.out.println("restaurantId: "+restaurantId);
        setAttr("time",time);
        setAttr("restaurantId",restaurantId);
        System.out.println("station: "+station);
        setAttr("station",station);
        Page<BasBill> orderpage =service.paginate(PageNumber, PageSize, time, station,restaurantId);
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        List<GasStation> gasstations = gastation.find("SELECT * FROM gas_station");
        setAttr("gasstations", gasstations);
        setAttr("orderpage", orderpage);
        setAttr("restaurants", restaurants);
        render("orderlist.html");
    }
public void edit(){
        Integer id = getParaToInt("id");
        Integer bid = getParaToInt("bid");
        System.out.println("id: "+id);
        System.out.println("bid: "+bid);
        BasBill order = service.findByid(id);
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        setAttr("gastations", gastations);
        setAttr("bid", bid);
        setAttr("order", order);
        render("edit.html");
}
    public void update() {
        // 使用事务确保两个表的更新操作原子性
        boolean success = Db.tx(() -> {
            try {
                // 1. 获取并更新BasBill记录
                BasBill basBill = getModel(BasBill.class, "order");

                // 2. 通过前端传入的bid（即BillInfo表的主键id）获取BillInfo记录
                // 注意：这里使用get("bid")而不是getBid()方法，因为框架自动转换表单字段到模型属性
                 Integer bid = getParaToInt("order.bid");
                 String driver = getPara("order.driver");
                 System.out.println("bid: "+bid);
                System.out.println("driver: "+driver);
                if (bid == null) {
                    throw new RuntimeException("缺失关联订单ID（bid）");
                }

                // 3. 根据bid查询BillInfo表记录
                BillInfo billInfo = billdao.findById(bid);
                if (billInfo == null) {
                    throw new RuntimeException("未找到对应的订单信息，bid：" + bid);
                }

                // 4. 同步字段到BillInfo表（根据业务需求选择需要同步的字段）
                // 假设前端表单传递了price和jiedanshijian字段
                billInfo.set("price", basBill.get("price"))
                        .set("jiedanshijian", basBill.get("jiedanshijian"))
                        .set("jiedanstatus", basBill.get("jiedanstatus"))
                        .set("driver", basBill.get("driver"));
                // 5. 执行双重更新（建议先更新主表再更新关联表）
                boolean updateBasBill = basBill.update();   // 更新bas_bill表
                boolean updateBillInfo = billInfo.update(); // 更新bill_info表

                // 返回联合操作结果
                return updateBasBill && updateBillInfo;
            } catch (Exception e) {
                // 记录详细的错误日志
                LogKit.error("订单更新失败：" + e.getMessage(), e);
                // 返回false会自动触发事务回滚
                return false;
            }
        });

        // 根据事务执行结果返回响应
        if (success) {
            // 成功重定向到列表页
            redirect("/orderinfo/orderlist");
        } else {
            // 失败返回JSON响应
            renderJson(new JSONObject()
                    .fluentPut("code", 500)
                    .fluentPut("msg", "更新失败，请检查数据有效性")
                    .fluentPut("detail", "可能原因：1.订单不存在 2.字段格式错误 3.数据库连接异常"));
        }
    }
public void delete(){
       service.deleteByid(getParaToInt());
       redirect("/orderinfo/orderlist");
}
public void submit(){
        Integer id = getParaToInt("id");
        Integer bid = getParaToInt("bid");
    // 生成当前时间字符串，格式为：YYYY-MM-DDTHH:mm
      String currentTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
       set("currentTime", currentTime); // 传递到模板
       List<Drivercar> drivers = drivercar.find("SELECT * FROM drivercar");
        System.out.println("bid: "+bid);
        setAttr("id", id);
        setAttr("bid", bid);
        setAttr("drivers",drivers);
        render("submit.html");
}
//public void save(){
//        BasBill order = getModel(BasBill.class,"order");
//        if(order.save())
//        {
//            redirect("/orderinfo/orderlist");
//        }
//}

}
