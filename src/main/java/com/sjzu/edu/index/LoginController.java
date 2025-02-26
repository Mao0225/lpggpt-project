package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.core.Path;

/**
 * IndexController
 */
public class LoginController extends Controller {

	/**
	 * 渲染 index.html 页面
	 */
	public void index() {
		render("index/login.html");
	}
}
