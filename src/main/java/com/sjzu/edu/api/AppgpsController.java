package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.HttpKit;
import com.sjzu.edu.common.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *   Jason 2024-03-09
 * AppbottleController//这个是手机端获取司机gps数据的接口
 */
@Path(value = "/", viewPath = "/appgps")
 @Clear
public class AppgpsController extends Controller {
//测试接口地址：http://localhost/appgps/savegps  参数：drivergpsinfo对象
	//云数据库测试接口

	private GasBottle dao = new GasBottle().dao();

	public void savegps() {
	// 获取请求体中的JSON字符串
	int drivercarid = Integer.parseInt(getPara("drivercarid"));
	String drivername = getPara("drivername");
	String carno = getPara("carno");
	String driverphone = getPara("driverphone");
	String address = getPara("address");
	String jingdu = getPara("jingdu");
	String weidu = getPara("weidu");

	// 创建Drivergpsinfo对象，并设置属性
	Drivergpsinfo drivergpsinfo = new Drivergpsinfo();
	drivergpsinfo.setDrivercarid(drivercarid);
	drivergpsinfo.setDrivername(drivername);
	drivergpsinfo.setCarno(carno);
	drivergpsinfo.setDriverphone(driverphone);
	drivergpsinfo.setAddress(address);
	drivergpsinfo.setJingdu(jingdu); // 假设Drivergpsinfo有这样的setter方法
	drivergpsinfo.setWeidu(weidu);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date now = new Date();
	String formattedDate = sdf.format(now);
	drivergpsinfo.setUpdatetime(formattedDate);
	// 保存到数据库
	drivergpsinfo.save();

	// 返回JSON响应
	JSONObject json = new JSONObject();
	json.put("flag", "200");
	renderJson(json);
}

	public void starttrans() {
	//开始运输
//测试接口地址：http://localhost:8099/appgps/starttrans
		//int drivercarid = Integer.parseInt(getPara("drivercarid"));
		String bottle_id = getPara("bottleId");
		String transstaff = getPara("transstaff");
		System.out.println("transstaff:"+transstaff);
		String transstate = "运输中";
		String car_id = getPara("carId");
		String tel = getPara("tel");


		String jingDu = getPara("jingdu");
		String weiDu = getPara("weidu");
		String address = getPara("address");

		// 创建Drivergpsinfo对象，并设置属性
		GasBottle gasBottle = new GasBottle();
		gasBottle.setBottleId(bottle_id);
		gasBottle.setTransStaff(transstaff);
		gasBottle.setTransState(transstate);
		gasBottle.setCarId(car_id);
		gasBottle.setTel(tel);
		gasBottle.setJingDu(jingDu);
		gasBottle.setWeiDu(weiDu);
		gasBottle.setAddress(address);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String formattedDate = sdf.format(now);
		gasBottle.setStartTime(formattedDate);
		// 保存到数据库
		gasBottle.save();
		System.out.println(gasBottle.toString());

		// 返回JSON响应
		JSONObject json = new JSONObject();
		json.put("flag", "200");
		json.put("mes", "上车成功");

		renderJson(json);
	}
	public void endtrans() {
		/* ---------- 1. 取参 ---------- */
		String bottleId   = getPara("bottleId");
		String jingDu     = getPara("jieshujingdu");
		String weiDu      = getPara("jieshuweidu");
		String address    = getPara("jieshuaddress");

		System.out.println("[ENDTRANS] bottleId=" + bottleId);
		System.out.println("[ENDTRANS] jieshujingdu=" + jingDu);
		System.out.println("[ENDTRANS] jieshuweidu=" + weiDu);
		System.out.println("[ENDTRANS] jieshuaddress=" + address);

		/* ---------- 2. 查记录 ---------- */
		GasBottle g = dao.findFirst(
				"SELECT * FROM gas_bottle " +
						"WHERE bottle_id = ? AND trans_state = '运输中' " +
						"ORDER BY id DESC LIMIT 1",
				bottleId
		);
		if (g == null) {
			System.out.println("[ENDTRANS] 未找到运输中的记录");
			renderJson(new JSONObject().fluentPut("flag", "400")
					.fluentPut("mes", "未找到运输中的记录"));
			return;
		}

		/* ---------- 3. 打印更新前 ---------- */
		System.out.println("[ENDTRANS] 更新前：trans_state=" + g.getTransState() +
				", jieshujingdu=" + g.getJieshujingdu() +
				", jieshuweidu=" + g.getJieshuweidu() +
				", jieshuaddress=" + g.getJieshuaddress());

		/* ---------- 4. 赋值 ---------- */
		g.setTransState("结束运输");
		g.setJieshujingdu(jingDu);
		g.setJieshuweidu(weiDu);
		g.setJieshuaddress(address);
		g.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		/* ---------- 5. 打印更新后 ---------- */
		System.out.println("[ENDTRANS] 更新后：trans_state=" + g.getTransState() +
				", jieshujingdu=" + g.getJieshujingdu() +
				", jieshuweidu=" + g.getJieshuweidu() +
				", jieshuaddress=" + g.getJieshuaddress());

		/* ---------- 6. 持久化 ---------- */
		boolean ok = g.update();
		System.out.println("[ENDTRANS] g.update() 返回值=" + ok);

		if (!ok) {
			renderJson(new JSONObject().fluentPut("flag", "500")
					.fluentPut("mes", "数据库更新失败"));
			return;
		}

		/* ---------- 7. 返回 ---------- */
		renderJson(new JSONObject().fluentPut("flag", "200")
				.fluentPut("mes", "下车成功"));
	}
}



