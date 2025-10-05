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
		String qieduanfa = getPara("qieduanfa");
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

		// 构建PLC信号数据模型
		Plcxinhao plcxinhao = new Plcxinhao();
		plcxinhao.setQipingjianbaojing(qipingjianbaojing);
		plcxinhao.setChufangbaojing(chufangbaojing);
		plcxinhao.setYanganbaojing(yanganbaojing);
		plcxinhao.setQieduanfa(qieduanfa);
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