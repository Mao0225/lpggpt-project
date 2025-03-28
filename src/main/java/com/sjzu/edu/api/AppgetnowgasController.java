package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Db;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * AppgetnowgasController //这个是手机端获取司机gps数据的接口
 */
@Path(value = "/", viewPath = "/appgps")
@Clear
public class AppgetnowgasController extends Controller {


	/**
	 * 查询当日加气量的数据
	 */
	public void getnowgasbyday() {
		// 查询当日加气量的数据
		// 测试接口地址：http://localhost:8099/appgetnowgas/getnowgasbyday
		// 云数据库测试接口http://114.115.156.201:8099/appgetnowgas/getnowgasbyday
		addCorsHeaders();
		String sql = "SELECT * FROM othergas WHERE tradedate >= CURDATE() AND tradedate < CURDATE() + INTERVAL 1 DAY ORDER BY tradedate DESC";
		List<Record> nowGasList = Db.use("jiaqi").find(sql);

		// 将查询结果转换为 JSON 格式
		JSONObject result = new JSONObject();
		result.put("success", true);
		result.put("data", nowGasList);

		// 返回 JSON 数据
		renderJson(result);
	}

	//获取过去 30天的加气量
	public void getnowgasbymonth() {
		// 测试接口地址：http://localhost:8099/appgetnowgas/getnowgasbymonth
		// 云数据库测试接口：http://114.115.156.201:8099/appgetnowgas/getnowgasbymonth
		addCorsHeaders();
		String sql = "SELECT " +
				"    DATE_FORMAT(tradedate, '%Y 年 %m 月 %d 日') AS 日期, " +
				"    SUM(nowgas) AS 加气总量 " +
				"FROM othergas " +
				"WHERE tradedate >= CURDATE() - INTERVAL 30 DAY " +
				"GROUP BY DATE_FORMAT(tradedate, '%Y-%m-%d') " +
				"ORDER BY tradedate DESC";

		List<Record> monthlyGasList = Db.use("jiaqi").find(sql);

		// 组装 JSON 结果
		JSONObject result = new JSONObject();
		result.put("success", true);
		result.put("data", monthlyGasList);

		// 返回 JSON 数据
		renderJson(result);
	}

	// 获取过去 12 个月的加气量
	public void getnowgasbyyear() {
		// 测试接口地址：http://localhost:8099/appgetnowgas/getnowgasbyyear
		// 云数据库测试接口：http://114.115.156.201:8099/appgetnowgas/getnowgasbyyear
		addCorsHeaders();

		String sql = "SELECT " +
				"    DATE_FORMAT(tradedate, '%Y 年 %m 月') AS 日期, " +
				"    ROUND(SUM(nowgas), 2) AS 加气量 " +
				"FROM othergas " +
				"WHERE tradedate >= DATE_FORMAT(CURDATE() - INTERVAL 1 YEAR, '%Y-%m-01') " +
				"  AND tradedate < DATE_FORMAT(CURDATE() + INTERVAL 1 MONTH, '%Y-%m-01') " +
				"GROUP BY DATE_FORMAT(tradedate, '%Y-%m') " +
				"ORDER BY tradedate DESC";

		List<Record> yearlyGasList = Db.use("jiaqi").find(sql);

		// 组装 JSON 结果
		JSONObject result = new JSONObject();
		result.put("success", true);
		result.put("data", yearlyGasList);

		// 返回 JSON 数据
		renderJson(result);
	}
	/**
	 * 获取每年的加气量
	 */
	public void getnowgaseachyear() {
		addCorsHeaders();
		String sql = "SELECT " +
				"    YEAR(tradedate) AS 年份, " +
				"    ROUND(SUM(nowgas), 2) AS 加气量 " +
				"FROM othergas " +
				"GROUP BY YEAR(tradedate) " +
				"ORDER BY 年份 DESC";

		List<Record> yearlyGasList = Db.use("jiaqi").find(sql);

		JSONObject result = new JSONObject();
		result.put("success", true);
		result.put("data", yearlyGasList);

		renderJson(result);
	}
	public void addCorsHeaders() {
		// 获取 HttpServletResponse 对象
		HttpServletResponse response = getResponse();
		// 设置允许跨域的来源
		String origin = getHeader("Origin");
		if (StrKit.notBlank(origin)) {
			// 设置允许跨域的方法
			((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
			// 设置允许跨域的头
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
			// 设置允许跨域的最大缓存时间（单位：秒）
			response.setHeader("Access-Control-Max-Age", "3600");
			// 设置允许跨域的来源
			response.setHeader("Access-Control-Allow-Origin", origin);
		}
		renderNull();
	}
}