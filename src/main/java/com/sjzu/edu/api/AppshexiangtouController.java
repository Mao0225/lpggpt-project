package com.sjzu.edu.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.Jiebangding;
import com.sjzu.edu.common.model.Jieping;
import com.sjzu.edu.common.model.Shexiangtou;
import com.sjzu.edu.common.model.Shexiangtoulanya;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *   Jason 2024-03-09
 * AppbottleController//这个是手机端获取司机gps数据的接口
 */
@Path(value = "/", viewPath = "/shexiangtou")
 @Clear
public class AppshexiangtouController extends Controller {
	private Shexiangtou dao = new Shexiangtou().dao();

		public void shexiangtou() {
			String[] shexiangtounos = getParaValues("shexiangtouno[]"); // 改变代码来接受一个字符串数组
			int offset = getParaToInt("offset", 0);  // 默认起始位置为0
			int count = getParaToInt("count", 10);  // 默认每页结果数量为100

			List<Shexiangtou> shexiangtouList = new ArrayList<>();
			Long total = 0L;

			for(String shexiangtouno: shexiangtounos){
				// 获取唯一 ID 总数
				total += Db.queryLong("SELECT COUNT(DISTINCT id) FROM shexiangtou WHERE shexiangtouno = ?", shexiangtouno);
				shexiangtouList.addAll(dao.find("SELECT shexiangtouno, Alarmpic, alarmmes, happendtime, id FROM shexiangtou WHERE shexiangtouno = ? ORDER BY id desc LIMIT ? OFFSET ?", shexiangtouno, count, offset));
			}
		JSONObject json = new JSONObject();

		if (!shexiangtouList.isEmpty()) {
			json.put("flag", "200");
			JSONArray jsonArray = new JSONArray();

			for (Shexiangtou shexiangtou : shexiangtouList) {
				jsonArray.add(shexiangtou);
			}

			json.put("shexiangtouList", jsonArray);
		} else {
			json.put("flag", "300");
		}

		// 创建一个Map来包含shexiangtouList，total和page
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("shexiangtouList", shexiangtouList);
		resultMap.put("total", total);
		int pageCount = total > 0 ? (int)Math.ceil((double)total/count) : 0;
		resultMap.put("pageCount", pageCount);

		// 将Map对象转化为JSON对象输出
		renderJson(resultMap);
	}
	//王念写的
	public void saveshexiangtoubaojing() {
		// jason，2023-03-20，接受摄像头上传的照片和报警信息
		//测试接口地址：http://localhost:8099/shexiangtou/saveshexiangtoubaojing 参数：qipingbianma  FT00000010
		//云数据库测试接口：http://114.115.156.201:8099/shexiangtou/saveshexiangtoubaojing


		System.out.println("开始接收摄像头发射的数据...");

		// 获取请求参数

		addCorsHeaders();  // 允许跨域请求
		JSONObject json = new JSONObject();

		// 处理文件上传
		UploadFile file = getFile("photo");
		String shexiangtouno = getPara("shexiangtouno");
		//System.out.println("shexiangtouno: " + shexiangtouno);

		String baojingtype = getPara("baojingtype");
		//System.out.println("baojingtype: " + baojingtype);

		String happendtime = getPara("happendtime");
		//System.out.println("happendtime: " + happendtime);

		String alarmmes = getPara("alarmmes");
		String memo = getPara("memo");
		//System.out.println("happendtime: " + happendtime);
		if (file != null) {
			System.out.println("接收到摄像头文件: " + file.getFileName());
		} else {
			System.out.println("未接收到摄像头文件");
		}
		if (file != null && shexiangtouno != null && baojingtype != null && happendtime != null) {
			// 获取文件名并保存文件到/upload目录下（JFinal已自动完成文件保存工作）
			//http://114.115.156.201:8099/upload/temp/2024-03-21_20-45-55.jpg
			System.out.println("test888");

			String fileName = file.getFileName();
			System.out.println("fileName:"+fileName);

			String filePath = file.getUploadPath() + File.separator + fileName;
			System.out.println("filePath:"+filePath);

			// 使用模型保存信息到数据库
			Shexiangtou shexiangtou = new Shexiangtou();
			shexiangtou.set("shexiangtouno", shexiangtouno)
					.set("happendtime", happendtime)
					.set("alarmtype", baojingtype)
					.set("alarmmes", alarmmes)
					.set("memo", memo)
					.set("Alarmpic", fileName);  // 保存文件名
			System.out.println("test3");

			// 保存模型到数据库
			boolean saveResult = shexiangtou.save();

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
			json.put("message", "参数错误或文件上传失败");
		}

		// 返回响应
		renderJson(json.toString());
	}

	public void saveshexiangtoujieping() {
		// jason，2023-03-20，接受摄像头上传的截屏信息
		//测试接口地址：http://localhost:8099/shexiangtou/saveshexiangtoujieping
		//云数据库测试接口：http://114.115.156.201:8099/shexiangtou/saveshexiangtoujieping

		System.out.println("开始接收摄像头的截屏数据...");

		addCorsHeaders();  // 允许跨域请求
		JSONObject json = new JSONObject();

		try {
			// 处理文件上传
			UploadFile file = getFile("photo");
			String shexiangtouno = getPara("shexiangtouno");
			System.out.println("shexiangtouno: " + shexiangtouno);

			String happendTime = getPara("happendtime");
			System.out.println("happendtime: " + happendTime);

			if (file != null) {
				System.out.println("接收到摄像头截屏文件: " + file.getFileName());

				String fileName = file.getFileName();
				String originalPath = file.getUploadPath();
				String savePath = originalPath + File.separator + "jieping"; // 新的保存路径

				File originalFile = new File(originalPath + File.separator + fileName);
				File newFile = new File(savePath + File.separator + fileName);

				new File(savePath).mkdirs(); // 确保目标目录存在

				// 将文件从原始位置移动到新位置
				Files.move(Paths.get(originalFile.getAbsolutePath()), Paths.get(newFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
				System.out.println("文件已移动到: " + newFile.getAbsolutePath());

				// 使用模型保存信息到数据库
				Jieping jieping = new Jieping();
				jieping.set("shexiangtouno", shexiangtouno)
						.set("happendtime", happendTime)
						.set("photo", fileName);  // 保存文件的新位置
				System.out.println("准备保存截屏数据");

				// 保存模型到数据库
				boolean saveResult = jieping.save();
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
				System.out.println("未接收到摄像头截屏文件");
				json.put("flag", 400);
				json.put("message", "参数错误或文件上传失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("flag", 500);
			json.put("message", "服务器内部错误");
		}

		// 返回响应
		renderJson(json.toString());
	}

	public void saveshexiangtoulanya() {
		System.out.println("开始接收摄像头发射的蓝牙通信数据...");
//存储摄像头发射的蓝牙信息
		// 允许跨域请求
		addCorsHeaders();

		// 读取请求体中的JSON数据
		String requestBody = getRawData();
		JSONObject jsonRequest = JSON.parseObject(requestBody);

		// 从JSON对象中获取参数
		String shexiangtouno = jsonRequest.getString("shexiangtouno");
		String lanyamessage = jsonRequest.getString("lanyamessage");
		String happendtime = jsonRequest.getString("happendtime");
		String ipaddress = jsonRequest.getString("ipaddress");

		System.out.println("ipaddress: " + ipaddress);

		System.out.println("shexiangtouno: " + shexiangtouno);
		System.out.println("lanyamessage: " + lanyamessage);
		System.out.println("happendtime: " + happendtime);

		JSONObject jsonResponse = new JSONObject();

		if (shexiangtouno != null) {
			System.out.println("开始存储蓝牙数据");

			// 使用模型保存信息到数据库
			Shexiangtoulanya shexiangtoulanya = new Shexiangtoulanya();
			shexiangtoulanya.set("shexiangtouno", shexiangtouno)
					.set("lanyamessage", lanyamessage)
					.set("memo", ipaddress)
					.set("happendtime", happendtime);

			// 保存模型到数据库
			boolean saveResult = shexiangtoulanya.save();

			if (saveResult) {
				jsonResponse.put("flag", 200);
				jsonResponse.put("message", "成功");
			} else {
				jsonResponse.put("flag", 500);
				jsonResponse.put("message", "数据保存失败");
			}
		} else {
			jsonResponse.put("flag", 400);
			jsonResponse.put("message", "参数错误");
		}

		// 返回响应
		renderJson(jsonResponse.toString());
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



