package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Drivergpsinfo;

import java.util.List;

/**
 * 这个是查看车辆运行的界面
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/user")
public class CaryunxingController extends Controller {
	@Inject
	CaryunxingService service;

	public void yunxinglist() {
		System.out.println(service.paginate(getParaToInt(0, 1), 10).toString());
		setAttr("yunxinglist", service.paginate(getParaToInt(0, 1), 10));
		render("caryunxing.html");
	}

	public void yunxingkanban() {
		System.out.println(service.paginate(getParaToInt(0, 1), 50).toString());
		String carno = getPara("carno");
		int pageNumber = getParaToInt("page", 1); // 默认为第1页
		int pageSize = getParaToInt("size", 50);
		setAttr("carno", carno);
		setAttr("yunxinglist", service.search(pageNumber, pageSize, carno));
		render("caryunxingkanban.html");
	}

	// 新增方法：获取指定车牌在过去24小时的经纬度数据
	public void getVehicleLocations() {
		String carno = getPara("carno");
		String timestamp = getPara("timestamp");
		List<Drivergpsinfo> locations = service.findLocationsByCarnoAndTime(carno, timestamp);
		renderJson(locations); // 返回 JSON 数据
	}
}


