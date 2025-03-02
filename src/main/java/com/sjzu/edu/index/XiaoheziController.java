package com.sjzu.edu.index;

import com.jfinal.core.Controller;
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
      public void save(){
        ManageXiaohezi mx = getModel(ManageXiaohezi.class,"xiaohezi");
        if(mx.save()) {
            redirect("/xiaohezi/xiaohezilist");
        }
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


