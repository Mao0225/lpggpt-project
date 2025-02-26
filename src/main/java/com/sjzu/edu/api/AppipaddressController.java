package com.sjzu.edu.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 *   Jason 2024-03-09
 * AppbottleController//这个是手机端获取司机gps数据的接口
 */
@Path(value = "/", viewPath = "/applpgyunxing")
 @Clear
public class AppipaddressController extends Controller {
	private Ipaddress ipadressdao = new Ipaddress().dao();// 燃气表基本信息表

	public void savedeviceaddress() {
		// jason，2023-03-20，接受摄像头上传的照片和报警信息
		//测试接口地址：http://localhost:8099/appipaddress/savedeviceaddress 参数：qipingbianma  FT00000010
		//云数据库测试接口：http://114.115.156.201:8099/appipaddress/savedeviceaddress



		// 获取请求参数

		addCorsHeaders();  // 允许跨域请求
		JSONObject json = new JSONObject();

		// 处理文件上传
 		String deviceno = getPara("deviceno");
		System.out.println("deviceno: " + deviceno);

		String ipaddress = getPara("ipaddress");

		String happendtime = getPara("happendtime");
		System.out.println("happendtime: " + happendtime);
		Ipaddress ipaddressToupdate = ipadressdao.findFirst("select * from ipaddress where deviceno =? ",deviceno);

		if (ipaddressToupdate != null) {
			 System.out.println("ipaddressToupdate:"+ipaddressToupdate.getDeviceno());
			 String poweronoff = ipaddressToupdate.getPoweronoff();
			 json.put("poweronoff", poweronoff);
			 if (poweronoff.equals("20")){
				 poweronoff ="10";
			 }
			// 使用模型保存信息到数据库
			ipaddressToupdate.set("ipaddress", ipaddress)
					.set("happendtime", happendtime)  // 保存文件名
					.set("poweronoff", poweronoff);  // 保存文件名

			// 保存模型到数据库
			boolean saveResult = ipaddressToupdate.update();


			if (saveResult) {
				// 构造成功的响应
				json.put("flag", 200);
				json.put("message", "成功");
			} else {
				// 数据保存失败
				json.put("flag", 500);
				json.put("message", "数据保存失败");
			}
		} else {
			// 参数错误或文件上传失败
			json.put("flag", 400);
			json.put("message", "参数错误，或者没有该摄像头");
		}

		// 返回响应
		renderJson(json.toString());
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



