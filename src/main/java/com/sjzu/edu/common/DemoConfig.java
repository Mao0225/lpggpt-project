package com.sjzu.edu.common;

import com.jfinal.config.*;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import com.sjzu.edu.api.*;
import com.sjzu.edu.common.model.*;
import com.sjzu.edu.index.*;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 * 
 * API 引导式配置
 */
public class DemoConfig extends JFinalConfig {

	static Prop p;
	static Prop pJiaqi;
	static Prop pLpg;
	
	/**
	 * 启动入口，运行此 main 方法可以启动项目，此 main 方法可以放置在任意的 Class 类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		UndertowServer.start(DemoConfig.class);
	}
	
	/**
	 * PropKit.useFirstFound(...) 使用参数中从左到右最先被找到的配置文件
	 * 从左到右依次去找配置，找到则立即加载并立即返回，后续配置将被忽略
	 */
	static void loadConfig() {
		if (p == null) {
			p = PropKit.useFirstFound("demo-config-pro.txt", "demo-config-dev.txt");
		}
		// 加载额外的数据库配置
		if (pJiaqi == null) {
			pJiaqi = PropKit.use("demo-config-jiaqi.txt");
		}
		if (pLpg == null) {
			pLpg = PropKit.use("demo-config-lpg.txt");
		}
	}
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		loadConfig();
		me.setBaseUploadPath("upload/temp");  // 设置文件上传的基础路径
		me.setMaxPostSize(10 * 1024 * 1024);
		me.setDevMode(p.getBoolean("devMode", false));
		me.setJsonFactory(new MixedJsonFactory());

		/**
		 * 支持 Controller、Interceptor、Validator 之中使用 @Inject 注入业务层，并且自动实现 AOP
		 * 注入动作支持任意深度并自动处理循环注入
		 */
		me.setInjectDependency(true);
		
		// 配置对超类中的属性进行注入
		me.setInjectSuperClass(true);
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		// 使用 jfinal 4.9.03 新增的路由扫描功能
		//手机接口

		me.add("/applogin", ApploginController.class);
		me.add("/appbottle", AppbottleController.class);//获得气瓶信息，获取gas_file信息
		me.add("/appgps", AppgpsController.class);//给手机端，上传gps用的
		me.add("/applpgyunxing", ApplpgyunxingController.class);//这个主要是给大屏幕用的
		me.add("/appbangding", AppbangdingController.class);//这个是绑定称应用程序的
		me.add("/appranqibiao", AppranqibiaoController.class);//这个是给丹东燃气表做的接受燃气表上传信息的
		me.add("/shexiangtou", AppshexiangtouController.class);//这个是管理摄像头信息的，首先是收火警
		me.add("/appranqibiaoanzhuang", AppranqibiaoanzhuangController.class);//这个是给丹东燃气表做的接受燃气表上传信息的
		me.add("/appipaddress", AppipaddressController.class);//这个是给丹东燃气表做的接受燃气表上传信息的
		me.add("/appbaojing", AppshexiangtoubaojingController.class);//手机端获取摄像头报警信息表的
		me.add("/applanya", AppshexiangtoulanyaController.class);//手机端获取摄像头蓝牙这个表的信息
		me.add("/appshexiangtoumessage",AppshexiangtoumessageController.class);
		me.add("/appindex",AppIndexController.class);
		me.add("/appdianzicheng",AppdianzichengController.class);
		me.add("/apporder",ApporderController.class);
		me.add("/appmyrestaurant",AppMyrestaurantController.class);
		me.add("/appdianzibiaoqian",AppDianzibiaoqianController.class);
		me.add("/apptransport",AppTransportController.class);
		me.add("/apprecord",AppRecordController.class);//LPG手机端充装追溯记录
		me.add("/applogin2", Applogin2Controller.class);
		me.add("/appscanqcode", AppscanqcodeController.class);
		me.add("/appgasfilebangding", AppgasfilebangdingController.class);//绑定奉天码镂空码
        me.add("/appxiaohezi", AppxiaoheziController.class);

		//手机接口结束  AppbangdingController
		//me.scan("com.demo.");
		me.add("/", LoginController.class);
		me.add("/index", IndexController.class);
		me.add("/user", UserController.class);
		me.add("/func", FuncController.class);
		me.add("/userFunc", UserFunctionController.class);
		me.add("/caryunxing", CaryunxingController.class);
		me.add("/GasStation", GasStationController.class);
		me.add("/GasStationStaff", GasStationStaffController.class);
		me.add("/dianzicheng", DianzichengController.class);
		me.add("/driver", DriverController.class);
		me.add("/lpgyunxing", LpgyunxingController.class);
		me.add("/jiebangding", JiebangdingController.class);
		me.add("/bangdingren", BangdingrenController.class);
		me.add("/drivercar", DrivercarController.class);
		me.add("/restaurant", RestaurantController.class);
		me.add("/gasFile", GasfileController.class);
		me.add("/shexiangtoufind", ShexiangtoufindController.class);
		me.add("/basshexiangtouinfo", BasshexiangtouinfoController.class);
		me.add("/shexiangtoulanya", ShexiangtoulanyaController.class);
		me.add("/pressuregauge", PressureGaugeRecordsController.class);
		me.add("/kangjiashan", KangjiashanController.class);
		me.add("/kangjiashanothergas", KangjiashanothergasController.class);
		me.add("/pressurevalue", PressureValueRecordsController.class);
		me.add("/gasbottle", GasBottleController.class);
		me.add("/jieping", JiepingController.class);
		me.add("/uploaddianzibiaoqian", UploaddianzibiaoqianController.class);
		me.add("/filrdck",FilrdckController.class);
		me.add("/ranqibiaobangding",RanqibiaobangdingController.class);
		me.add("/custromer", CustomerController.class);
		me.add("/Cequipment", CreatEquipmentController.class);
		me.add("/Eequipment", EquipmentoperateController.class);
		me.add("/Fequipment", FittingController.class);
		me.add("/equipment", EquipmentController.class);
		me.add("/maintenance", MaintenanceController.class);
		me.add("/managerequip",ManageEquipmentController.class);
		me.add("managegun",MgGunController.class);
		me.add("/mmanagegun",MmgGunController.class);
		me.add("/setrans",SetransController.class);
		me.add("/GGasFile",GGasFileController.class);
		me.add("/jiaqizhong",JiaqizhongController.class);
		me.add("/Jjiaqizhong",JjiaqizhongController.class);
		me.add("/unqualified",UnqualifiedController.class);
		me.add("/Uunqualified",UunqualifiedController.class);
		me.add("/GgasStationStaff",GgasStationStaffController.class);
		me.add("/Ffilrdck",FfilrdckController.class);
        me.add("/xiaohezi",XiaoheziController.class);
	}
	
	public void configEngine(Engine me) {
		//me.addSharedFunction("/common/_layout.html");
		me.addSharedFunction("/common/_paginate.html");
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		loadConfig();
		// 原有数据库配置
		DruidPlugin druidPlugin = createDruidPlugin(p);
		me.add(druidPlugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		_MappingKit.mapping(arp);
		me.add(arp);

		// 配置额外的数据源
		Prop pJiaqi = PropKit.use("demo-config-jiaqi.txt");
		DruidPlugin jiaqiDruidPlugin = new DruidPlugin(pJiaqi.get("jdbcUrl"), pJiaqi.get("user"), pJiaqi.get("password"));
		me.add(jiaqiDruidPlugin);
		ActiveRecordPlugin jiaqiArp = new ActiveRecordPlugin("jiaqi", jiaqiDruidPlugin);
	//	_JiaqiMappingKit.mapping(jiaqiArp); // Jiaqi 数据库的模型映射
		me.add(jiaqiArp);

		Prop plpg = PropKit.use("demo-config-lpg.txt");
		DruidPlugin lpgDruidPlugin = new DruidPlugin(plpg.get("jdbcUrl"), plpg.get("user"), plpg.get("password"));
		me.add(lpgDruidPlugin);
		ActiveRecordPlugin lpgArp = new ActiveRecordPlugin("lpg", lpgDruidPlugin);
		_LpgMappingKit.mapping(lpgArp); // lpg 数据库的模型映射
		me.add(lpgArp);
	}

	public static DruidPlugin createDruidPlugin(Prop prop) {
		return new DruidPlugin(prop.get("jdbcUrl"), prop.get("user"), prop.get("password"));
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}
}
