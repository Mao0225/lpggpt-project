package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.core.Path;

/**
 * IndexController
 */
public class LoginController extends Controller {

	/**
	 * 渲染登录页
	 */
	public void index() {
		render("index/login.html");
	}

	/**
	 * 退出登录：清除 session 并跳转到登录页
	 */
	public void logout() {
		javax.servlet.http.HttpSession session = getSession(false);
		if (session != null) {
			session.invalidate();
		}
		redirect("/");
	}
}
