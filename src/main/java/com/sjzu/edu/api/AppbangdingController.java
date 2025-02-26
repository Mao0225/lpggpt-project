package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.GasFile;
import com.sjzu.edu.common.model.Jiebangding;
import com.sjzu.edu.common.model.PressureValueRecords;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *   Jason 2024-03-09
 * AppbottleController//这个是处理电子秤绑定的接口
 */
@Path(value = "/", viewPath = "/applpgyunxing")
 @Clear
public class AppbangdingController extends Controller {
	private Jiebangding dao = new Jiebangding().dao();

	public void getchengliebiao() {
		// jason，根据扫描的二维码，获取这个设备下有几个秤
		//测试接口地址：http://localhost:8099/appbangding/getchengliebiao  参数：devicename  FT00000010
		//云数据库测试接口：http://114.115.156.201:8099/appbangding/getchengliebiao
		addCorsHeaders(); // 添加这一行来允许跨域请求
		String devicename = getPara("devicename");//传递的参数


		List<Record> records = Db.use().find("select * from dianzicheng where bianma = ?", devicename);
		JSONObject json = new JSONObject();
		if (records != null) {
			json.put("flag","200" );
			json.put("dianzichenglist",records );
		}else{
			json.put("flag","300" );
		}
		renderJson(json);
	}
	// querenbangdin 接口 jason，2023-03-19，手机端绑定的确认接口，此时秤已经接到绑定命令，这个接口等待秤上传新的数据
	public void querenbangdin() { //
		// jason，2023-03-19，手机端绑定的确认接口，此时秤已经接到绑定命令，这个接口等待秤上传新的数据
		//测试接口地址：http://localhost:8099/appbangding/querenbangdin 参数：qipingbianma  FT00000010
		//云数据库测试接口：http://114.115.156.201:8099/appbangding/querenbangdin
		addCorsHeaders(); // 添加这一行来允许跨域请求
		JSONObject json = new JSONObject();

		String timeStamp = getPara("timeStamp");//传递的参数
		String qipingbianma = getPara("qipingbianma");//传递的参数,气瓶编码
		String chengbianma = getPara("chengbianma");//传递的参数 秤编码
		String whichcheng = getPara("whichcheng");//传递的参数
		String bangdingrendianhua = getPara("bangdingrendianhua");//传递的参数
		String bangdingren = getPara("bangdingren");//传递的参数
 		String address= getPara("address");//传递的参数
		String newwhichcheng ="latest_Value"+whichcheng+"_1";
		String latest_created_time ="latest_Value"+whichcheng+"_1_created_time";
		String newlatest_created_time = null;//这个是存储从数据库里读出来的 最后更新时间
		String bdqcszl = "latest_Value"+whichcheng+"_1";//绑定前初始重量
		String bdqsyzl= "latest_Value"+whichcheng+"_3";//绑定使用重量
		String bdqdqzl = "latest_Value"+whichcheng+"_2";//绑定当前重量
		String sqlvalue="value"+whichcheng+"_1";
		System.out.println("test latest_created_time:"+latest_created_time);
		String columnName = sqlvalue; // 确保这个变量来源是安全的，或者是硬编码的值
		String sql = "SELECT * FROM t_iot_sync_records_v2 WHERE devicename = ? AND " + columnName + " IS NOT NULL ORDER BY id DESC LIMIT 1";
		List<Record> records = Db.use("lpg").find(sql, chengbianma);

		for (Record record : records) {
			newlatest_created_time = record.getStr("created_time");
			System.out.println("数据库返回newlatest_created_time"+newlatest_created_time);
			bdqcszl = record.getStr("Value1_1");
			bdqsyzl = record.getStr("Value1_3");
			bdqdqzl = record.getStr("Value1_2");

		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date date1 = sdf.parse(timeStamp);
			Date date2 = sdf.parse(newlatest_created_time);
			System.out.println("请求绑定时间："+timeStamp);
			System.out.println("最后上传数据时间："+latest_created_time);

			// 比较两个日期
			if(date1.after(date2)){
				System.out.println("早于绑定申请时间，此时说明秤没有在请求绑定后上传数据，绑定无效");
				json.put("flag","400" );
				json.put("message","没有数据上传，请等待" );
			} else if(date1.before(date2)){
				System.out.println("晚于绑定申请时间，此时绑定请求有效");
				json.put("flag","200" );

				Jiebangding jiebangding= new Jiebangding();
				jiebangding.setQipingbianma(qipingbianma);
				jiebangding.setChengbianma(chengbianma);
				jiebangding.setBangdingshijian(timeStamp);
				jiebangding.setBangdingrendianhua(bangdingrendianhua);
				jiebangding.setStatus(10);
				jiebangding.setAddress(address);
				jiebangding.setBangdingren(bangdingren);
				jiebangding.setBdqcszl(bdqcszl);
				jiebangding.setBdqdqzl(bdqdqzl);
				jiebangding.setBdqsyzl(bdqsyzl);
				boolean result = jiebangding.save();
				if (result) {
					json.put("message","绑定成功" );
				} else {
					json.put("message","绑定失败" );
				}



			} else {
				System.out.println("绑定时间相等");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (records != null) {
			json.put("flag","200" );
		}else{
			json.put("flag","300" );
		}
		renderJson(json);
	}
	// querenjiebangdin 解绑定接口 jason，2023-03-19，手机端绑定的确认接口，此时秤已经接到绑定命令，这个接口等待秤上传新的数据

	public void querenjiebangdin() { //
		// jason，2023-03-19，手机端绑定的确认接口，此时秤已经接到绑定命令，这个接口等待秤上传新的数据
		//测试接口地址：http://localhost:8099/appbangding/querenjiebangdin 参数：qipingbianma  FT00000010
		//云数据库测试接口：http://114.115.156.201:8099/appbangding/querenjiebangdin
		addCorsHeaders(); // 添加这一行来允许跨域请求
		JSONObject json = new JSONObject();

		String timeStamp = getPara("timeStamp");//传递的参数
		String qipingbianma = getPara("qipingbianma");//传递的参数,气瓶编码
		String chengbianma = getPara("chengbianma");//传递的参数 秤编码
		String whichcheng = getPara("whichcheng");//传递的参数
		String jiebangdingrendianhua = getPara("jiebangdingrendianhua");//传递的参数
		String jiebangdingren = getPara("jiebangdingren");//传递的参数
		String newwhichcheng ="latest_Value"+whichcheng+"_1";
		String latest_created_time ="created_time";
		String jbdhcszl = "latest_Value"+whichcheng+"_1";//绑定前初始重量
		String jbdhsyzl= "latest_Value"+whichcheng+"_3";//绑定使用重量
		String jbdhdqzl = "latest_Value"+whichcheng+"_2";//绑定当前重量
		System.out.println("test latest_created_time:"+latest_created_time);
		String sqlvalue="Value"+whichcheng+"_1";

		String columnName = sqlvalue; // 确保这个变量来源是安全的，或者是硬编码的值
		String sql = "SELECT * FROM t_iot_sync_records_v2 WHERE devicename = ? AND " + columnName + " IS NOT NULL ORDER BY id DESC LIMIT 1";
		List<Record> records = Db.use("lpg").find(sql, chengbianma);
		System.out.println("records size:"+records.size());

		for (Record record : records) {
			latest_created_time = record.getStr(latest_created_time);
			jbdhcszl = record.getStr("Value1_1");
			jbdhsyzl = record.getStr("Value1_3");
			jbdhdqzl = record.getStr("Value1_2");
			System.out.println("jbdhcszl:"+jbdhcszl);
			System.out.println("jbdhsyzl:"+jbdhsyzl);
			System.out.println("jbdhdqzl:"+jbdhdqzl);

		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date date1 = sdf.parse(timeStamp);
			Date date2 = sdf.parse(latest_created_time);
			System.out.println("请求解绑定时间："+timeStamp);
			System.out.println("最后上传数据时间："+latest_created_time);

			// 比较两个日期
			if(date1.after(date2)){
				System.out.println("早于解绑定申请时间，此时说明秤没有在请求绑定后上传数据，绑定无效");
				json.put("flag","400" );
				json.put("message","没有数据上传，请等待" );
			} else if(date1.before(date2)){
				System.out.println("晚于解绑定申请时间，此时绑定请求有效");
				json.put("flag","200" );
				//存储数据过程开始

				Jiebangding jiebangdingToUpdate = dao.findFirst("SELECT * FROM jiebangding WHERE chengbianma = ? AND status = ?", chengbianma, 10);
				if (jiebangdingToUpdate != null) {
					// 对找到的记录进行更新
					jiebangdingToUpdate.setJiebangdingshijian(timeStamp);
					jiebangdingToUpdate.setJiebangdingrendianhua(jiebangdingrendianhua);
					jiebangdingToUpdate.setStatus(20);
					jiebangdingToUpdate.setBangdingren(jiebangdingren);
					jiebangdingToUpdate.setJbdhcszl(jbdhcszl);
					jiebangdingToUpdate.setJbdhsyzl(jbdhsyzl);
					jiebangdingToUpdate.setJbdhsyzl(jbdhdqzl);
					boolean result = jiebangdingToUpdate.update();


					if (result) {
						json.put("message","绑定成功" );
 					} else {
						json.put("message","绑定失败" );
					}
				} else {
					System.out.println("没有找到符合条件的记录");
					json.put("message","没有找到符合记录的数据" );

				}
//存储数据过程结束



			} else {
				System.out.println("绑定时间相等");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (records != null) {
			json.put("flag","200" );
		}else{
			json.put("flag","300" );
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



