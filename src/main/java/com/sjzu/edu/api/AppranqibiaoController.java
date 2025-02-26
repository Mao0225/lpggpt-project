package com.sjzu.edu.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.PressureValueRecords;

import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 *   Jason 2024-03-09
 * AppranqibiaoController//这个和丹东表生产厂家对接的接口
 */
@Path(value = "/", viewPath = "/appranqibiao")
 @Clear
public class AppranqibiaoController extends Controller {

	public void saveranqibiaodata() {
		// jason，根据扫描的二维码，获取这个设备下有几个秤
		//测试接口地址：http://localhost:8099/appranqibiao/saveranqibiaodata
		//云数据库测试接口：http://114.115.156.201:8099/appranqibiao/saveranqibiaodata
		addCorsHeaders(); // 添加这一行来允许跨域请求
		//参数列表
		String controller_id = getPara("controller_id");//压力表上传的控制器号，唯一标识，可做外键
		Float standard_condition_accumulated = Float.valueOf(getPara("standard_condition_accumulated"));//浮点，非空，压力表读数，压力表上传的标况累计量
		Float working_condition_accumulated = Float.valueOf(getPara("working_condition_accumulated"));//浮点，非空，压力表上传的工况累计量
		String valve_status = getPara("valve_status");//压力表上传的阀门状态
		Float battery_voltage = Float.valueOf(getPara("battery_voltage"));//浮点，非空，压力表上传的电池电压
		String in_meter_datetime = getPara("in_meter_datetime"); //压力表上传的表内时间，String类型
		String meter_reading_datetime = getPara("meter_reading_datetime"); //压力表上传的抄表时间，String类型
		Float temperature = Float.valueOf(getPara("temperature"));//浮点，非空，压力表上传的温度
		Float pressure = Float.valueOf(getPara("pressure"));//浮点，非空，压力表上传的压力
		String meter_operating_status = getPara("meter_operating_status");//压力表上传的表内运行状态
		String signallevel = getPara("signallevel");//压力表上传的信号强度
		String terminalreporttype = getPara("terminalreporttype");//原因标志位
		String httemparr = getPara("httemparr");//声速
		String shengyujine = getPara("shengyujine");//剩余金额









		LocalDateTime currentTime = LocalDateTime.now();
		// 格式化当前时间为字符串
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = formatter.format(currentTime);

		PressureValueRecords pre= new PressureValueRecords();
			pre.setControllerId(controller_id);
			pre.setStandardConditionAccumulated(standard_condition_accumulated);
			pre.setWorkingConditionAccumulated(working_condition_accumulated);
			pre.setValveStatus(valve_status);
			pre.setBatteryVoltage(battery_voltage);
			pre.setInMeterDatetime(in_meter_datetime);
			pre.setMeterReadingDatetime(meter_reading_datetime);
			pre.setTemperature(temperature);
			pre.setPressure(pressure);
			pre.setSignallevel(signallevel);
			pre.setTerminalreporttype(terminalreporttype);
			pre.setHttemparr(httemparr);
			pre.setShengyujine(shengyujine);
		pre.setMeterOperatingStatus(meter_operating_status);
			pre.setCreatedTime(formattedDateTime);
		boolean result = pre.save();
		// 构造响应 JSON
		JSONObject json = new JSONObject();
		if (result) {
			json.put("flag", "200");
			json.put("message", "保存成功");
		} else {
			json.put("flag", "300");
			json.put("message", "保存失败");
		}
		renderJson(json);
	}

		public void getAlldata() {
			// jason，根据扫描的二维码，获取这个设备下有几个秤
			//测试接口地址：http://localhost:8099/appranqibiao/getAlldata
			//云数据库测试接口：http://114.115.156.201:8099/appranqibiao/getAlldata
			addCorsHeaders(); // 添加这一行来允许跨域请求
			//参数列表
			// 构造响应 JSON
			List<Record> records = Db.use().find("select * from t_pressure_value_records order by id desc");

			JSONObject json = new JSONObject();
			if (records != null)  {
				json.put("flag", "200");
				json.put("message", "查找成功");
				json.put("records", records);
			} else {
				json.put("flag", "300");
				json.put("message", "查询失败");
			}
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



