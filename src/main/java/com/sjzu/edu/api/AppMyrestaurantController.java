package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.GasFile;
import com.sjzu.edu.common.model.Restaurant;
import com.sjzu.edu.common.model.User;
import com.sjzu.edu.common.model.base.BaseGasFile;

import java.util.Map;

/**
 *   Jason 2024-03-09
 * AppbottleController//这个是手机端根据气瓶编码获取气瓶信息的接口
 */
@Path(value = "/", viewPath = "/appmyrestaurant")
@Clear
public class AppMyrestaurantController extends Controller {
    private Restaurant dao = new Restaurant().dao();

    public void myrestaurant() {
        String telephone = getPara("telephone");

        // 查询数据库中是否存在匹配的用户记录


        Restaurant restaurant =dao.findFirst("SELECT * FROM restaurant WHERE telephone = ?", telephone);
        //System.out.println(gasFile.toString());
        JSONObject json = new JSONObject();
        if (restaurant != null) {
            json.put("flag","200" );
            json.put("restaurant",restaurant );
        }else{
            json.put("flag","300" );
        }

        renderJson(json);


    }
    public void update() {

            // 获取电话号码
            String telephone = getPara("telephone");

            // 获取前端传过来的json对象，包含了需要更新的字段和值
            String updateJson = getPara("restaurant");

            // 把json字符串转换成json对象
            JSONObject updateData = JSONObject.parseObject(updateJson);

            // 保存新的电话号码并从更新数据中移除
            String newTelephone = updateData.get("telephone").toString();
            updateData.remove("telephone");

            // 使用旧的电话号码查找并更新记录
            Restaurant restaurantToUpdate = dao.findFirst("SELECT * FROM restaurant WHERE telephone = ?", telephone);

            if (restaurantToUpdate != null) {
                for (Map.Entry<String, Object> entry : updateData.entrySet()) {
                    restaurantToUpdate.set(entry.getKey(), entry.getValue());
                }

                // 单独更新电话字段
                restaurantToUpdate.set("telephone", newTelephone);

                // 更新数据库
                restaurantToUpdate.update();

                // 返回更新成功的响应
                JSONObject json = new JSONObject();
                json.put("flag", "200");
                json.put("message", "Update successful");
                renderJson(json);
            } else {
                // 餐馆不存在，返回错误响应
                JSONObject json = new JSONObject();
                json.put("flag", "300");
                json.put("message", "No such restaurant");
                renderJson(json);
            }
        }
    }




