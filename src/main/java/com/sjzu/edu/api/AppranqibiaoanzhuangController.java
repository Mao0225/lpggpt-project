package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *   Jason 2024-03-09
 * AppranqibiaoController//这个燃气表安装和卸载的接口
 */
@Path(value = "/", viewPath = "/appranqibiaoanzhuang")
@Clear
public class AppranqibiaoanzhuangController extends Controller {
	private PressureValueRecords dao = new PressureValueRecords().dao();//燃气表读书上传表，丹东上传
	private PressureGaugeRecords basranqibiaodao = new PressureGaugeRecords().dao();// 燃气表基本信息表
	private Ranqibiaobangding ranqibiaodao = new Ranqibiaobangding().dao();


	public void ranqibiaocaozuo() {
		// jason，操作燃气表的接口，可能是安装，可能是卸载
		//测试接口地址：http://localhost:8099/appranqibiaoanzhuang/ranqibiaocaozuo
		//云数据库测试接口：http://114.115.156.201:8099/appranqibiaoanzhuang/ranqibiaocaozuo
		addCorsHeaders(); // 添加这一行来允许跨域请求
		//参数列表
		Ranqibiaobangding ranqibiao= new Ranqibiaobangding();//燃气绑定表

		String ranqibiaono = getPara("ranqibiaono");//燃气表表号
		System.out.println("ranqibiaono: " + ranqibiaono);

		String qipingno = getPara("qipingno");//气瓶编号
		System.out.println("qipingno: " + qipingno);

		String saomiaoren = getPara("saomiaoren");//操作人
		System.out.println("saomiaoren: " + saomiaoren);

		String flag = getPara("flag"); //10是安装，20是卸载
		System.out.println("flag: " + flag);

		String jingdu = getPara("jingdu"); //经度
		System.out.println("jingdu: " + jingdu);

		String weidu = getPara("weidu"); //纬度
		System.out.println("weidu: " + weidu);

		String address = getPara("address"); //地址
		System.out.println("address: " + address);

		System.out.print("ranqibiaono:"+ranqibiaono);
		PressureValueRecords ranqibiaodushu = dao.findFirst("select * from t_pressure_value_records where controller_id =? order by id desc",ranqibiaono);
		if (ranqibiaodushu == null) {
			// 如果查询为空，返回失败信息
			JSONObject json = new JSONObject();
			json.put("flag", "300");
			json.put("message", "操作失败");
			renderJson(json);
			return;  // 终止程序执行
		}
		String biaodushu = String.valueOf(ranqibiaodushu.getStandardConditionAccumulated());
		String biaodushuriqi = ranqibiaodushu.getMeterReadingDatetime();

		System.out.println("biaodushu: " + biaodushu);
		System.out.println("biaodushuriqi: " + biaodushuriqi);
		//获得当时时间
		LocalDateTime now = LocalDateTime.now();
		// 定义日期时间格式
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// 格式化当前时间为指定格式的字符串
		String saomiaoshijian = now.format(formatter);
		//获得当时时间结束
		ranqibiao.setRanqibiaono(ranqibiaono);
		ranqibiao.setQipingno(qipingno);
		ranqibiao.setSaomiaoren(saomiaoren);
		ranqibiao.setFlag(Integer.valueOf(flag));
		ranqibiao.setJingdu(jingdu);
		ranqibiao.setWeidu(weidu);
		ranqibiao.setAddress(address);
		ranqibiao.setBiaodushu(biaodushu);
		ranqibiao.setBiaodushuriqi(biaodushuriqi);
		ranqibiao.setSaomiaoshijian(saomiaoshijian);
		ranqibiao.save();

		PressureGaugeRecords basranqibiaoToupdate = basranqibiaodao.findFirst("select * from t_pressure_gauge_records where controller_id =? order by id desc",ranqibiaono);
		if (basranqibiaoToupdate == null) {
			// 如果查询为空，返回失败信息
			JSONObject json = new JSONObject();
			json.put("flag", "300");
			json.put("message", "操作失败");
			renderJson(json);
			return;  // 终止程序执行
		}
		//判断上传的燃气表字符串
		String[] parts = qipingno.split("-");
		// 循环遍历打印每个子字符串
		String gasCylinderId = basranqibiaoToupdate.getGasCylinderId(); // 获取gasCylinderId字符串



		if ("10".equals(flag)) {
			// 参数等于 "10"
			System.out.println("参数等于 10");
			for (String part : parts) {
				if (!gasCylinderId.contains(part)) {
					// 在 part 前面添加 "-"，然后与 gasCylinderId 进行拼接
					gasCylinderId += "-" + part;
					gasCylinderId = gasCylinderId.replace("--", "-");

				}
			}
		} else if ("20".equals(flag)) {
			// 参数等于 "20"
			System.out.println("参数等于 20");
			for (String part : parts) {
				// 如果 gasCylinderId 包含当前的 part，则将其从 gasCylinderId 中删除
				if (gasCylinderId.contains(part)) {
					// 使用 replace 方法将当前的 part 替换为空字符串，实现删除
					gasCylinderId = gasCylinderId.replace(part, "");
					gasCylinderId = gasCylinderId.replace("--", "-");

				}
			}
		} else {
			// 参数既不是 "10" 也不是 "20"
			System.out.println("参数既不是 10 也不是 20");
		}
		basranqibiaoToupdate.setGasCylinderId(gasCylinderId);
		boolean result = basranqibiaoToupdate.update();

		JSONObject json = new JSONObject();
		if (result)  {
			json.put("flag", "200");
			json.put("message", "操作成功");
		} else {
			json.put("flag", "300");
			json.put("message", "操作失败");
		}
		renderJson(json);
	}

	//手机端获取操作列表


	public void getanzhuanginfo() {
		// 获取分页参数，默认为第1页，每页10条
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("size", 10);

		// 获取设备名参数
		String saomiaoren = getPara("saomiaoren", "");
		String startdate = getPara("startdate", "");
		String enddate = getPara("enddate", "");
		String fandianname = getPara("fandianname", "");
		//System.out.println("jason ceshi:" + saomiaoren + startdate + enddate + fandianname);

		// 检查 saomiaoren 参数是否为空
		if (saomiaoren == null || saomiaoren.isEmpty()) {
			renderJson("error", "saomiaoren 参数不能为空");
			return;
		}

		// 构建查询语句
		String select = "select r.*, t.name";
		StringBuilder sqlExceptSelect = new StringBuilder("FROM ranqibiaobangding r " +
				"LEFT JOIN restaurant t on r.ranqibiaono = t.chengno " +
				"WHERE r.saomiaoren = ? ");

		// 用于存储查询参数
		List<Object> params = new ArrayList<>();
		params.add(saomiaoren);

		// 如果 startdate 和 enddate 不为空，则加上时间范围条件
		if (!startdate.isEmpty()) {
			sqlExceptSelect.append("AND r.saomiaoshijian > ? ");
			params.add(startdate);  // 添加 startdate 参数
		}

		if (!enddate.isEmpty()) {
			sqlExceptSelect.append("AND r.saomiaoshijian < ? ");
			params.add(enddate);  // 添加 enddate 参数
		}

		// 始终执行模糊查询的条件 (fandianname)
		sqlExceptSelect.append("AND t.name LIKE ? ");
		params.add("%" + fandianname + "%");

		// 添加排序条件
		sqlExceptSelect.append("ORDER BY r.id DESC");

		// 执行分页查询
		Page<Record> page = Db.use().paginate(pageNumber, pageSize, select, sqlExceptSelect.toString(), params.toArray());

		// 构建返回的JSON对象
		JSONObject json = new JSONObject();
		if (page != null && page.getList().size() > 0) {
			json.put("flag", "200");
			json.put("bangdinglist", page.getList());
		} else {
			json.put("flag", "300");
			json.put("message", "未找到相关安装信息");
		}

		// 返回JSON格式数据
		renderJson(json);
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



