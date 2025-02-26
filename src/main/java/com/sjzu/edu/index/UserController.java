package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.*;

import java.util.List;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 * 
 * IndexController
 */
@Path(value = "/", viewPath = "/user")
@Before(UserInterceptor.class)
public class UserController extends Controller {
	//private User dao = new User().dao();
	@Inject
    UserService service;
	private GasStation gasstation = new GasStation().dao();
	/**
	 * 显示功能列表页面       MVC  将来面试，都会问到的
	 */
	public void userlist() {
		try {
			System.out.println("userlist function");
			System.out.println(service.paginate(getParaToInt(0, 1), 10).toString());
			int pageNumber = getParaToInt("page", 1); // 默认为第1页
			setAttr("users", service.paginate(pageNumber, 10));
			render("users.html");
		} catch (Exception e) {
			e.printStackTrace();
			renderError(500);
		}
	}

	/**
	 * 编辑功能页面
	 */
	public void edit() {
		try {
			setAttr("user", service.findById(getParaToInt()));
			int id = getParaToInt();
			User function = service.findById(id);
			List<GasStation> gasstations = gasstation.find("SELECT * FROM gas_station");
			setAttr("function", function);
			setAttr("gasstations", gasstations);
			render("edit.html");
		} catch (Exception e) {
			e.printStackTrace();
			renderError(500);
		}
	}

	/**
	 * 更新功能
	 */
	public void update() {
		User function = getModel(User.class, "user");
		if(function.update()) {
			redirect("/user/userlist");
		} else {
		}
	}
	/**
	 * 添加功能页面
	 */
	public void add() {
		List<GasStation> gasstations = gasstation.find("SELECT * FROM gas_station");
		setAttr("gasstations", gasstations);
		render("add.html");
	}

	/**
	 * 保存功能
	 */
	public void save() {
		User user = getModel(User.class, "user");
		if (user.save()) {
			// 保存成功，可以重定向到列表页面或者显示成功消息
			redirect("/user/userlist");
		} else {
		}
	}
	/**
	 * 删除功能
	 */
	public void delete() {
		System.out.println("delete function");
		service.deleteById(getParaToInt());
		redirect("/user/userlist");
	}
}



