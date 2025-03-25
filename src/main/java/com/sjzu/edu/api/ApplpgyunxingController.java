package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Drivergpsinfo;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static cn.hutool.extra.servlet.ServletUtil.setHeader;

/**
 *   Jason 2024-03-09
 * AppbottleController//这个是手机端获取司机gps数据的接口
 */
@Path(value = "/", viewPath = "/applpgyunxing")
@Clear
public class ApplpgyunxingController extends Controller {

	//下面的方法是饭店端获取报警信息的，手机端：我的报警信息
	public void getbaojingforkehu() {
		// 添加跨域请求头
		// 使用 "lpg" 数据源，这个是获取最新的50个t_iot_sync_records_v2表里的数据，包括报警信息，秤的读数
		//测试接口地址：http://localhost:8099/applpgyunxing/getbaojingforkehu
		//云数据库测试接口：http://114.115.156.201:8099/applpgyunxing/getbaojingforkehu

		addCorsHeaders();

		// 获取分页参数，默认值为第一页，每页10条
		int pageNumber = getParaToInt("page", 1);
		int pageSize = getParaToInt("size", 10);
		// 获取设备名参数
		String devicename = getPara("devicename", "");

		// 构建查询语句，过滤Alarm不为0并匹配devicename
		String select = "SELECT t.devicename, t.Alarm, t.created_time, " +
				"CASE t.Alarm " +
				"WHEN 0 THEN '正常' " +
				"WHEN 1 THEN '操作间报警' " +
				"WHEN 2 THEN '气瓶间报警' " +
				"WHEN 3 THEN '摄像头报警' " +
				"WHEN 4 THEN '烟感报警' " +
				"WHEN 5 THEN '防拆卸报警' " +
				"ELSE t.Alarm " +
				"END AS Alarm, t.PowerStatus ";
		String sqlExceptSelect = "FROM t_iot_sync_records_v2 t " +
				"WHERE t.Alarm IS NOT NULL AND t.Alarm != 0 AND t.devicename = ? " +
				"ORDER BY t.id DESC";

		// 执行分页查询
		Page<Record> page = Db.use("lpg").paginate(pageNumber, pageSize, select, sqlExceptSelect, devicename);

		// 构建返回的JSON对象
		JSONObject json = new JSONObject();
		if (page != null && page.getList().size() > 0) {
			json.put("flag", "200");
			json.put("baojinglist", page);
		} else {
			json.put("flag", "300");
		}

		// 返回JSON格式数据
		renderJson(json);
	}
	//下面这个方法是用于大屏的
	public void getbaojinginfo() {
		// 使用 "lpg" 数据源，这个是获取最新的50个t_iot_sync_records_v2表里的数据，包括报警信息，秤的读数
		//测试接口地址：http://localhost:8099/applpgyunxing/getbaojinginfo
		//云数据库测试接口：http://114.115.156.201:8099/applpgyunxing/getbaojinginfo
		addCorsHeaders(); // 添加这一行来允许跨域请求

		String sqls="SELECT \n" +
				"    t.devicename,t.Alarm,\n" +
				"    t.created_time,\n" +
				"    CASE t.Alarm\n" +
				"        WHEN 0 THEN '正常'\n" +
				"        WHEN 1 THEN '操作间报警'\n" +
				"        WHEN 2 THEN '气瓶间报警'\n" +
				"        WHEN 3 THEN '摄像头报警'\n" +
				"        WHEN 4 THEN '烟感报警'\n" +
				"        WHEN 5 THEN '防拆卸报警'\n" +
				"        ELSE t.Alarm\n" +
				"    END AS Alarm,\n" +
				"    t.PowerStatus \n" +
				"FROM \n" +
				"    t_iot_sync_records_v2 t \n" +
				"WHERE \n" +
				"    t.Alarm IS NOT NULL \n" +
				"ORDER BY \n" +
				"    t.id DESC \n" +
				"LIMIT \n" +
				"    50;\n";
		List<Record> records = Db.use("lpg").find(sqls);
		JSONObject json = new JSONObject();
		if (records != null) {
			json.put("flag","200" );
			json.put("yunxinglist",records );
		}else{
			json.put("flag","300" );
		}
		renderJson(json);
	}
	public void getdatafordapinglgq() {
		//刘国奇修改的，因为装了新的小盒子，然后新的大屏接口，很多数据都是最新的。
		// 使用 "lpg" 数据源，这个是获取最新的20个t_iot_sync_records_v2表里的数据，获取气瓶间报警信息
		//测试接口地址：http://localhost:8099/applpgyunxing/getdatafordapinglgq
		//云数据库测试接口：http://114.115.156.201:8099/applpgyunxing/getdatafordapinglgq
		addCorsHeaders(); // 添加这一行来允许跨域请求
		//	获取gas_file总数，就是气瓶总数
		String sqlqipingzongshu = "SELECT count(*) from gas_file";
		Integer qipingzongshu = Db.use().queryNumber(sqlqipingzongshu).intValue();
 //	获取当日运输的气罐数
		String sqlyunshuzongshu = "SELECT COUNT(*) \n" +
				"FROM gas_bottle \n" +
				"WHERE DATE(start_time) = CURDATE()";
		Integer dangriyunshuzongshu = Db.use().queryNumber(sqlyunshuzongshu).intValue();

		//获取当日小盒子报警信息
		String sqlbaojing = "SELECT count(*) \n" +
				"FROM t_iot_sync_rds_records_v3 \n" +
				"WHERE alarm IS NOT NULL \n" +
				"  AND alarm <> 0\n" +
				"  AND DATE(FROM_UNIXTIME(created_time / 1000)) = CURDATE()";
		Integer dangribaojing = Db.use("lpg").queryNumber(sqlbaojing).intValue();
		//获取当日充装
		String sqldangrichongzhuang="SELECT COUNT(*) \n" +
				"FROM fill_record_check1 \n" +
				"WHERE DATE(fill_time) = CURDATE()";
		Integer dangrichongzhuang = Db.use().queryNumber(sqldangrichongzhuang).intValue();

		//获取报警信息列表
		String sqlrecordbaojing="SELECT id, devicename, FROM_UNIXTIME(created_time / 1000) as created_time,alarm  \n" +
				" from t_iot_sync_rds_records_v3 where alarm IS NOT NULL ORDER BY id desc limit 6\n";
		List<Record> recordsbaojing = Db.use("lpg").find(sqlrecordbaojing);
		//获取充装列表
		String sqlrecordschongzhuang="SELECT fill_record_check1.gas_number,fill_record_check1.now_gas, fill_record_check1.fill_time, gas_station.station_name\n" +
				"FROM fill_record_check1 \n" +
				"LEFT JOIN gas_station on fill_record_check1.gasstation = gas_station.station_id\n" +
				"WHERE DATE(fill_time) = CURDATE()\n" +
				"ORDER BY fill_record_check1.id DESC limit 5\n";
		List<Record> recordschongzhuang = Db.use().find(sqlrecordschongzhuang);
		//获取摄像头轮播图片
		String sqlrecordsshexiangtou="SELECT Alarmpic,happendtime from shexiangtou ORDER BY id desc limit 20";
		List<Record> recordsshexiangtou = Db.use().find(sqlrecordsshexiangtou);

		//获取安装记录
		String sqlrecordsanzhuang="SELECT \n" +
				"    CONCAT_WS(',', \n" +
				"        COALESCE(bse_xiaohezi.gas_number1, ''),\n" +
				"        COALESCE(bse_xiaohezi.gas_number2, ''),\n" +
				"        COALESCE(bse_xiaohezi.gas_number3, ''),\n" +
				"        COALESCE(bse_xiaohezi.gas_number4, '')\n" +
				"    ) AS gas_number,\n" +
				"    bse_xiaohezi.creattime,\n" +
				"    restaurant.name \n" +
				"FROM \n" +
				"    bse_xiaohezi \n" +
				"LEFT JOIN \n" +
				"    restaurant \n" +
				"ON \n" +
				"    bse_xiaohezi.xiaohezi_number = restaurant.xiaohezi\n" +
				"ORDER BY \n" +
				"    bse_xiaohezi.id DESC \n" +
				"LIMIT 5";
		List<Record> sqlrecordanzhuang = Db.use().find(sqlrecordsanzhuang);

//jason
		JSONObject json = new JSONObject();

		if (recordsbaojing != null || recordschongzhuang != null) {
			json.put("flag","200" );
			if (recordsbaojing != null) {
				json.put("recordsbaojing", recordsbaojing);
			}
			if (recordschongzhuang != null) {
				json.put("recordschongzhuang", recordschongzhuang);
			}
			if (recordsshexiangtou != null) {
				json.put("recordsshexiangtou", recordsshexiangtou);
			}
			if (sqlrecordanzhuang != null) {
				json.put("sqlrecordanzhuang", sqlrecordanzhuang);
			}
			json.put("qipingzongshu", qipingzongshu);
			json.put("dangriyunshuzongshu", dangriyunshuzongshu);
			json.put("dangribaojing", dangribaojing);
			json.put("dangrichongzhuang", dangrichongzhuang);
		}else{
			json.put("flag","300" );
		}
		renderJson(json);
	}
	public void getdatafordaping() {
		// 使用 "lpg" 数据源，这个是获取最新的20个t_iot_sync_records_v2表里的数据，获取气瓶间报警信息
		//测试接口地址：http://localhost:8099/applpgyunxing/getdatafordaping
		//云数据库测试接口：http://114.115.156.201:8099/applpgyunxing/getdatafordaping
		addCorsHeaders(); // 添加这一行来允许跨域请求
		//获取气瓶报警信息
		String sqlqiping="SELECT devicename, created_time,alarm \n" +
				"from t_iot_sync_records_v2 where Alarm= 2 ORDER BY id desc limit 3";
		List<Record> recordsqiping = Db.use("lpg").find(sqlqiping);
		//获取摄像头报警
		String sqlshexiangtou="SELECT devicename, created_time,alarm \n" +
				"from t_iot_sync_records_v2 where Alarm= 3 ORDER BY id desc limit 3";
		List<Record> recordsshexiang = Db.use("lpg").find(sqlshexiangtou);
		//获取防拆卸报警
		String sqlfangchaixieu="SELECT devicename, created_time,alarm \n" +
				"from t_iot_sync_records_v2 where Alarm= 5 ORDER BY id desc limit 3";
		List<Record> recordsfangchaixie = Db.use("lpg").find(sqlfangchaixieu);
		//获取上传数据的秤的个数，也是使用总数，查询阿里云上传的报警数量，有多少个秤就有多少个数据
		String sqlchengshiyongzongshu = "SELECT SUM(cnt) AS total_distinct_devices\n" +
				"FROM (\n" +
				"    SELECT COUNT(DISTINCT devicename) AS cnt\n" +
				"    FROM t_iot_sync_records_v2\n" +
				") AS subquery;\n";
		Integer chengshiyongzongshu = Db.use("lpg").queryNumber(sqlchengshiyongzongshu).intValue();
		//获取加气记录总数，裴总阿里云上数据auto_gas_filling_record表总数量
		String sqljiaqijilu = "SELECT count(*) from auto_gas_filling_record";
		Integer jiaqijilu = Db.use("jiaqi").queryNumber(sqljiaqijilu).intValue();

		//获取阿里云上的秤上传的读数，只读取最新1条
		String sqlchengdushu ="select id,devicename,Value1_1,Value1_2,Value1_3,Value2_1,Value2_2,Value2_3,Value3_1,Value3_2,Value3_3,Value4_1,Value4_2,Value4_3\n" +
				" from t_iot_sync_records_v2 where Alarm is null ORDER BY id desc LIMIT 1";
		List<Record> lastchengdushu = Db.use("lpg").find(sqlchengdushu);
		//	获取运输的气罐数
		String sqlyunshuzongshu = "SELECT count(*) from gas_bottle";
		Integer yunshuzongshu = Db.use().queryNumber(sqlyunshuzongshu).intValue();

		//	获取gas_file总数，就是气瓶总数
		String sqlqipingzongshu = "SELECT count(*) from gas_file";
		Integer qipingzongshu = Db.use().queryNumber(sqlqipingzongshu).intValue();

		JSONObject json = new JSONObject();
		if (recordsqiping != null || recordsshexiang != null|| recordsfangchaixie != null) {
			json.put("flag","200" );
			if (recordsqiping != null) {
				json.put("qipingjianbaojing", recordsqiping);
			}
			if (recordsshexiang != null) {
				json.put("shexiangjianbaojing", recordsshexiang);
			}
			if (recordsfangchaixie != null) {
				json.put("fangchaixiejianbaojing", recordsfangchaixie);
			}
			json.put("chengshiyongzongshu", chengshiyongzongshu);
			json.put("chongzhuangzongshu", jiaqijilu);
			json.put("lastchengdushu", lastchengdushu);
			json.put("yunshuzongshu", yunshuzongshu);
			json.put("qipingzongshu", qipingzongshu);
		}else{
			json.put("flag","300" );
		}
		renderJson(json);
	}
	public void getdatafordapinglist() {
		// 使用 "lpg" 数据源，大瓶第二个页面。这个是获取最新的20个t_iot_sync_records_v2表里的数据，获取气瓶间报警信息
		//测试接口地址：http://localhost:8099/applpgyunxing/getdatafordapinglist
		//云数据库测试接口：http://114.115.156.201:8099/applpgyunxing/getdatafordapinglist
		addCorsHeaders(); // 添加这一行来允许跨域请求

		List<Record> records = Db.use("lpg").find("select * from t_iot_sync_records_v2  where alarm is null order by id desc limit 10");
		List<Record> dianzichengrecords = Db.use().find("select d.*,r.address from dianzicheng d LEFT JOIN restaurant r ON d.shiyongfangphoneid = r.id");
//把dianzichengrecords的属性加到每一个records对象
		//System.out.println("records"+records.size());
		//System.out.println("dianzichengrecords对象"+dianzichengrecords.size());
		String chengbianhao = "";
		for (Record record : records) {
			// 获取当前记录的 devicename
			String devicename = record.getStr("devicename");
			//System.out.println("records devicename"+ devicename);
			if (record.getStr("Value1_1") !=null){chengbianhao = "1";}
			if (record.getStr("Value2_1") !=null){chengbianhao = "2";}
			if (record.getStr("Value3_1") !=null){chengbianhao = "3";}
			if (record.getStr("Value4_1") !=null){chengbianhao = "4";}
			//System.out.println("chengbianhao:"+chengbianhao);
			// 在 dianzichengrecords 中查找对应的记录
			for (Record dianzichengRecord : dianzichengrecords) {
				String bianma = dianzichengRecord.getStr("bianma");
				String chengnumber = dianzichengRecord.getStr("yuliu1");

				// 如果找到了对应的记录，则将其属性值设置到当前 records 对象中
				if (devicename.equals(bianma) && chengbianhao.equals(chengnumber)) {
					// 假设需要设置的属性名为 "property"
					//System.out.println("dianzichengRecord chengnumber"+ chengnumber);

					record.set("shiyongfandianmingzi", dianzichengRecord.get("shiyongfandianmingzi"));
					record.set("address", dianzichengRecord.get("address"));
					record.set("shiyongfangphone", dianzichengRecord.get("shiyongfangphone"));
					record.set("shiyongfandianmingzi", dianzichengRecord.get("shiyongfandianmingzi"));
					//电子秤操作
					record.set("dakaifamen", dianzichengRecord.get("dakaifamen"));
					record.set("guanbifamen", dianzichengRecord.get("guanbifamen"));
					record.set("bangding", dianzichengRecord.get("bangdingjiekou"));
					record.set("jiebangding", dianzichengRecord.get("jiebangdingjiekou"));
					record.set("chengbianhao", dianzichengRecord.get("yuliu1"));//用电子秤表的yuliu1字段存储第几号秤
					//System.out.println("相同的秤编码");
					// 如果只需要设置一个属性，可以在这里 break，否则继续查找其他属性
				}
			}
		}
		//把dianzichengrecords的属性加到每一个records对象结束

		JSONObject json = new JSONObject();
		if (records != null) {
			json.put("flag","200" );
			json.put("records", records);

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



