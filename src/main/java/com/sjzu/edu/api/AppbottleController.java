package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.GasFile;
import com.sjzu.edu.common.model.User;
import com.sjzu.edu.common.model.base.BaseGasFile;

/**
 *   Jason 2024-03-09
 * AppbottleController//这个是手机端根据气瓶编码获取气瓶信息的接口
 */
@Path(value = "/", viewPath = "/appbottle")
 @Clear
public class AppbottleController extends Controller {
	private GasFile dao = new GasFile().dao();
	//测试接口地址：http://localhost:8099/appbottle/getBottleinfo  参数：bottlenumber：889998654360
	//测试接口地址：http://114.115.156.201:8099/appbottle/getBottleinfo  参数：bottlenumber：889998654360

	public void getBottleinfo() {
		String bottlenumber = getPara("bottlenumber");
		System.out.println("test bottlenumber:"+bottlenumber);
		// 查询数据库中是否存在匹配的用户记录


		GasFile gasFile =dao.findFirst("SELECT * FROM gas_file WHERE gas_number = ?", bottlenumber);
		//System.out.println(gasFile.toString());
		JSONObject json = new JSONObject();
		if (gasFile != null) {
			json.put("flag","200" );
			json.put("gasFile",gasFile );
		}else{
			json.put("flag","300" );
		}

		renderJson(json);


	}
}



