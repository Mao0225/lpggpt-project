package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasStation;
import com.sjzu.edu.common.model.ManageXiaohezi;
import com.sjzu.edu.common.model.Restaurant;

import java.util.List;

public class XiaoheziController extends Controller {
    XiaoheziService service = new XiaoheziService();
    private Restaurant restaurant = new Restaurant().dao();
    public void xiaohezilist() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);
        String xiaohezino = getPara("xiaohezino");
        System.out.println("xiaohezinno: " + xiaohezino);
        String Restaurant = getPara("restaurant");
        String craeteTime = getPara("createTime");
        System.out.println("restaurant: " + Restaurant);
        setAttr("xiaohezino", xiaohezino);
        setAttr("restaurant", Restaurant);
        setAttr("craeteTime", craeteTime);
        Page<ManageXiaohezi> xiaoheziPage = service.paginate(pageNumber, pageSize, xiaohezino, Restaurant, craeteTime);
        setAttr("xiaohezilist", xiaoheziPage);
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        setAttr("restaurants", restaurants);
        render("xiaohezi.html");
    }
      public void add() {
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        setAttr("restaurants", restaurants);
        render("add.html");
      }
    public void save() {
        // 使用事务确保两个操作原子性
        Db.tx(() -> {
            // 1. 保存小盒子信息
            ManageXiaohezi mx = getModel(ManageXiaohezi.class, "xiaohezi");
            if (!mx.save()) {
                return false; // 保存失败则回滚
            }

            // 2. 更新餐厅表的小盒子字段
            Integer restaurantId = Integer.valueOf(mx.getRestaurantid());
            if (restaurantId == null || restaurantId <= 0) {
                return false; // 无效的餐厅ID
            }

            Restaurant restaurants = restaurant.findById(restaurantId);
            if (restaurants == null) {
                return false; // 找不到对应餐厅
            }

            // 将小盒子编号设置到餐厅记录
            restaurants.setXiaohezi(mx.getXiaoheziNo());
            return restaurants.update();
        });

        redirect("/xiaohezi/xiaohezilist");
    }
      public void edit() {
        Integer id = getParaToInt("id");
        setAttr("xiaohezi",service.findById(id));
        setAttr("id", id);
        ManageXiaohezi  xiaohezi = (ManageXiaohezi) service.findById(id);
          // 增加空值判断
          if (xiaohezi == null) {
              System.out.println("xiaohezi is null");
              renderError(404); // 或者跳转到错误页面
              return;
          }
        List<Restaurant> restaurants = restaurant.find("SELECT * FROM restaurant");
        setAttr("restaurants", restaurants);
        render("edit.html");
      }
      public void update(){
          ManageXiaohezi mx = getModel(ManageXiaohezi.class,"xiaohezi");
          if(mx.update()) {
              redirect("/xiaohezi/xiaohezilist");
          }
      }

      public void delete() {
        Integer id = getParaToInt("id");
        service.deleteById(id);
        redirect("/xiaohezi/xiaohezilist");
      }
    }


