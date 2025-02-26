package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;

import java.util.List;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/lpgyunxing")
//@Before(UserInterceptor.class)
public class LpgyunxingController extends Controller {
	@Inject
	LpgyunxingService service;

	public void yunxinglist() {
		Integer pageNumber = getParaToInt("page",1);
		System.out.println("testjason");
		String restaurantname = getPara("restaurantname");
		Integer alarm = getParaToInt("alarm");
		setAttr("restaurantname",restaurantname);
		System.out.println("dasdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+restaurantname);
		setAttr("alarm",alarm);
		System.out.println("dasdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1111");
		setAttr("yunxinglist", service.search(pageNumber,20,restaurantname,alarm));
		System.out.println("testjason1");
		render("yunxinglist.html");
	}
}
