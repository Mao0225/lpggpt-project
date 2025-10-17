package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Plcxinhao;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PLC信号数据接口控制器
 */
@Path(value = "/", viewPath = "/appplcxinhao")
@Clear
public class AppplcxinhaoController extends Controller {

	/**
	 * 保存手机端上传的PLC信号数据
	 * 测试接口示例: http://localhost:8099/appplcxinhao/saveplcinxhao
	 */
	public void saveplcinxhao() {
		addCorsHeaders(); // 允许跨域请求

		// 获取请求参数
		String qipingjianbaojing = getPara("qipingjianbaojing");
		String chufangbaojing = getPara("chufangbaojing");
		String yanganbaojing = getPara("yanganbaojing");
		String qieduanfa1 = getPara("qieduanfa1");
		String qieduanfa2 = getPara("qieduanfa2");
		String fengji = getPara("fengji");
		String tongdaoone = getPara("tongdaoone");
		String tongdaotwo = getPara("tongdaotwo");
		String tongdaothree = getPara("tongdaothree");
		String tongdaofour = getPara("tongdaofour");
		Integer flag = getParaToInt("flag");
		Integer type = getParaToInt("type");
		String memo = getPara("memo");
		String plcno = getPara("plcno");
		String rfidno = getPara("rfidno");
		String uploadtime = getPara("uploadtime");

		String keranqitibaojingzhi600 = getPara("keranqitibaojingzhi600");
		String keranqitibaojingzhi610 = getPara("keranqitibaojingzhi610");

		// 构建PLC信号数据模型
		Plcxinhao plcxinhao = new Plcxinhao();
		plcxinhao.setQipingjianbaojing(qipingjianbaojing);
		plcxinhao.setChufangbaojing(chufangbaojing);
		plcxinhao.setYanganbaojing(yanganbaojing);
		plcxinhao.setQieduanfa1(qieduanfa1);
		plcxinhao.setQieduanfa2(qieduanfa2);
		plcxinhao.setFengji(fengji);
		plcxinhao.setTongdaoone(tongdaoone);
		plcxinhao.setTongdaotwo(tongdaotwo);
		plcxinhao.setTongdaothree(tongdaothree);
		plcxinhao.setTongdaofour(tongdaofour);
		plcxinhao.setFlag(flag);
		plcxinhao.setType(type);
		plcxinhao.setMemo(memo);
		plcxinhao.setPlcno(plcno);
		plcxinhao.setRfidno(rfidno);
		plcxinhao.setUploadtime(uploadtime);
		plcxinhao.setKeranqitibaojingzhi600(keranqitibaojingzhi600);
		plcxinhao.setKeranqitibaojingzhi610(keranqitibaojingzhi610);

		// 保存数据
		boolean result = plcxinhao.save();

		// 构建响应JSON
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

	/**
	 * 分页显示PLC信号数据
	 * 测试接口示例: http://localhost:8099/appplcxinhao/showdatabypage?pageNum=1&pageSize=10
	 */
	public void showdatabypage() {
		addCorsHeaders(); // 允许跨域请求

		// 获取分页参数，默认第一页，每页10条
		int pageNum = getParaToInt("pageNum", 1);
		int pageSize = getParaToInt("pageSize", 10);

		// 执行分页查询（按id倒序，最新数据在前）
		Page<Record> page = Db.use().paginate(
				pageNum,
				pageSize,
				"select *",
				"from plcxinhao order by id desc"
		);

		// 构建响应JSON
		JSONObject json = new JSONObject();
		if (page != null) {
			json.put("flag", "200");
			json.put("message", "查询成功");
			json.put("totalPage", page.getTotalPage()); // 总页数
			json.put("totalRow", page.getTotalRow());   // 总记录数
			json.put("currentPage", page.getPageNumber()); // 当前页码
			json.put("pageSize", page.getPageSize());   // 每页条数
			json.put("records", page.getList());        // 当前页数据列表
		} else {
			json.put("flag", "300");
			json.put("message", "查询失败");
		}
		renderJson(json);
	}


	/**
	 * 根据plcno获取plckongzhi表的一条数据
	 * 测试接口示例: http://localhost:8099/appplcxinhao/getkongzhiinfobyno?plcno=xxx
	 */
	public void getkongzhiinfobyno() {
		addCorsHeaders(); // 允许跨域请求

		// 获取客户端传入的plcno参数
		String plcno = getPara("plcno");

		// 构建响应JSON对象
		JSONObject json = new JSONObject();

		// 参数校验
		if (StrKit.isBlank(plcno)) {
			json.put("flag", "400");
			json.put("message", "plcno参数不能为空");
			renderJson(json);
			return;
		}

		// 查询plckongzhi表中对应plcno的记录（获取原始数据）
		Record originalRecord = Db.use().findFirst(
				"select * from plckongzhi where plcno = ? limit 1",
				plcno
		);

		// 构建响应结果
		if (originalRecord != null) {
			// 1. 立即返回原始数据给客户端
			json.put("flag", "200");
			json.put("message", "查询成功");
			json.put("data", originalRecord);
			renderJson(json);

			// 2. 启动异步线程，在后台执行数据库更新操作（不阻塞响应）
			new Thread(() -> {
				// 复制原始记录用于修改（避免影响已返回的对象）
				Record updateRecord = new Record();
				updateRecord.setColumns(originalRecord);

				boolean needUpdate = false;

				// 处理qieduanfakongzhi字段：值为1则改为0
				String qieduanfa1 = updateRecord.getStr("qieduanfa1kongzhi");
				if ("1".equals(qieduanfa1)) {
					updateRecord.set("qieduanfa1kongzhi", "0");
					needUpdate = true;
				}
				String qieduanfa2 = updateRecord.getStr("qieduanfa2kongzhi");
				if ("1".equals(qieduanfa2)) {
					updateRecord.set("qieduanfa2kongzhi", "0");
					needUpdate = true;
				}
				// 处理fengjikongzhi字段：值为1则改为0
				String fengji = updateRecord.getStr("fengjikongzhi");
				if ("1".equals(fengji)) {
					updateRecord.set("fengjikongzhi", "0");
					needUpdate = true;
				}

				// 执行数据库更新
				if (needUpdate) {
					Db.use().update("plckongzhi", updateRecord);
					// 可选：添加日志记录更新结果
					System.out.println("后台已更新plcno=" + plcno + "的记录");
				}
			}).start(); // 启动线程执行异步更新

		} else {
			json.put("flag", "300");
			json.put("message", "未找到对应plcno的记录");
			json.put("data", null);
			renderJson(json);
		}
	}


	/**
	 * 设置跨域响应头
	 */
	public void addCorsHeaders() {
		HttpServletResponse response = getResponse();
		String origin = getHeader("Origin");
		if (StrKit.notBlank(origin)) {
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Origin", origin);
		}
		renderNull();
	}
}