package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.PressureGaugeRecords;
import com.sjzu.edu.common.model.Restaurant;
import com.sjzu.edu.common.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 * 
 * IndexController
 */
@Path(value = "/", viewPath = "/applogin")
//@Before(IndexInterceptor.class)
@Clear
public class ApploginController extends Controller {
	private User dao = new User().dao();
	private Restaurant restaurantdao = new Restaurant().dao();
	private PressureGaugeRecords pressuregaugedao = new PressureGaugeRecords().dao();

	//测试接口地址：http://localhost:8099/applogin/login  参数：username：13591402086  password：1
	public void login() {
		String username = getPara("username");
		String password = getPara("password");
		System.out.println("hello jason");
		// 查询数据库中是否存在匹配的用户记录


		//User user = User.dao.findFirst("SELECT * FROM user WHERE username = ? AND password = ?", username, password);
		User user =dao.findFirst("SELECT * FROM user WHERE username = ? AND password = ?", username, password);
		//System.out.println(user.toString());
		JSONObject json = new JSONObject();
		if (user != null) {
			json.put("flag","200" );
			json.put("users",user );
		}else{
			json.put("flag","300" );
		}

		renderJson(json);
	}
	public void restaurantlogin() {
		//http://localhost:8099/applogin/restaurantlogin
		// 获取前端传递的参数
		String telephone = getPara("telephone");
		String pwd = getPara("pwd");
		System.out.println("hello jason" + telephone + ":" + pwd);

		// 查询数据库中是否存在匹配的用户记录
		Restaurant restaurant = restaurantdao.findFirst("SELECT * FROM restaurant WHERE telephone = ? AND pwd = ?", telephone, pwd);

		JSONObject json = new JSONObject();
		if (restaurant != null) {
			// 查询成功
			json.put("flag", "200");
			json.put("users", restaurant);

			// 获取restaurant的id
			Integer restaurantId = restaurant.getId();

			// 查询basshexiangtouinfo表中restaurantid与restaurantId相等的数据的shexiangtoubianhao
			List<Record> shexiangtouList = Db.use().find("SELECT shexiangtoubianhao FROM basshexiangtouinfo WHERE restaurantid = ?", restaurantId);

			// 提取shexiangtoubianhao值
			List<String> shexiangtoubianhaoList = new ArrayList<>();
			for (Record record : shexiangtouList) {
				shexiangtoubianhaoList.add(record.getStr("shexiangtoubianhao"));
			}

			// 将shexiangtoubianhaoList放入json对象
			json.put("shexiangtoulist", shexiangtoubianhaoList);

			// 查询ranqibiao
			String ranqibiao = restaurant.getChengno();
			System.out.println("hello jason2");
			PressureGaugeRecords pressuregauge = pressuregaugedao.findFirst("SELECT * FROM t_pressure_gauge_records WHERE controller_id = ?", ranqibiao);

			// 将ranqibiao和pressuregauge放入json对象
			json.put("ranqibiao", ranqibiao);
			json.put("pressuregauge", pressuregauge);
			json.put("login", restaurant.getId());
		} else {
			// 查询失败
			json.put("flag", "300");
		}

		// 返回JSON数据
		renderJson(json);
	}
	public void signup() {
		String restaurantno = getPara("restaurantno");
		String name = getPara("name");
		String address = getPara("address");
		String leader = getPara("leader");
		String telephone = getPara("telephone");
		String pwd = getPara("pwd");

		Restaurant restaurant = new Restaurant();
		restaurant.set("restaurantno", restaurantno);
		restaurant.set("name", name);
		restaurant.set("address", address);
		restaurant.set("leader", leader);
		restaurant.set("telephone", telephone);
		restaurant.set("pwd", pwd);
		restaurant.save();

		JSONObject json = new JSONObject();
		json.put("flag", "200");
		json.put("users", restaurant);
		renderJson(json);
	}

}



