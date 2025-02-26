package com.sjzu.edu.index;

import org.springframework.beans.BeanUtils;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Func;
import com.sjzu.edu.common.model.FuncParents;
import com.sjzu.edu.common.model.User;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IndexController
 */


@Path(value = "/", viewPath = "/index")
//@Before(IndexInterceptor.class)
public class IndexController extends Controller {
	private User dao = new User().dao();//查找登陆账号
	private Func fundao = new Func().dao();//这个用来查找登陆账号具有的功能
	public  Integer stationid;

	private FuncParents funcToFuncParents(Func func) {
//		System.out.println("Copying properties from Func object: " + func); // 打印原始Func对象
		FuncParents fp = new FuncParents();
		BeanUtils.copyProperties(func, fp);
//		System.out.println("Properties copied to FuncParents object: " + fp); // 打印复制后的FuncParents对象
		return fp;
	}

	private void findSubCategory(List<Func> funcList, List<FuncParents> funcParentsList) {
		// 遍历一级
		for (FuncParents fp : funcParentsList) {
			List<Func> fcList = new ArrayList<>();
			// 查找子级
			for (Func func : funcList) {
				// 判断当前目录是否是子父级关系
				if (fp.getId().equals(func.getFunParentid())) {
					fcList.add(func);
				}
			}
			// 最后把查到的子级保存到一级目录中
			fp.setChildFunctions(fcList);
//			System.out.println(fp.getChildFunctions());
		}
	}

	public void index() {
		String username = getPara("username");//传递的参数
		String password = getPara("password");//传递的参数

		// 查询数据库中是否存在匹配的用户记录
		User user = dao.findFirst("SELECT * FROM user WHERE username = ? AND password = ?", username, password);
		// 创建查询对象
		try {
			stationid = user.getStationid();
			setSessionAttr("user",user);

		} catch (NullPointerException e) {
			// 当user为null或者user.getStationid()返回null时，捕获空指针异常并将stationid赋值为1000
			stationid = 1000;
		}
		setSessionAttr("stationid", stationid);
		username=getPara("username");
		setSessionAttr("username", username);
// 执行查询并获取结果
		//List<Function> functions = fundao.findAll();
		//查询所有功能

		if (user != null) {
			System.out.println("user 对象："+user.toString());//打印出来登陆的对象信息

			// 用户名和密码匹配，保存用户信息到 session 中
			HttpSession session = getSession();
			session.setAttribute("currentUser", user);

			// 查找这个用户拥有的功能，如果这个用户登陆成功
			List<Func> functions = fundao.find("SELECT * FROM func WHERE id in (select functionid from userfunction where userid = ?)", user.getId());
			List<Func> yijifunctions = fundao.find("SELECT * from func f where f.id in(\n" +
					"SELECT DISTINCT fun_parentid  from func p where p.id in (SELECT functionid from userfunction where userid =?) \n" +
					") and f.flag =20\n", user.getId());

			List<FuncParents> parentFunctions = new ArrayList<>();
			parentFunctions= yijifunctions.stream()
					.filter(e ->e.getFlag().equals(20))
					.map(this::funcToFuncParents)
					.collect(Collectors.toList());
//			System.out.println("父功能");
//			parentFunctions.stream().forEach(System.out::println);

			List<Func> sonFunctions = functions.stream()
					.filter(e ->e.getFlag().equals(10))
					.sorted(Comparator.comparing(Func::getId).reversed())
					.collect(Collectors.toList());
//			System.out.println("子功能");
//			sonFunctions.stream().forEach(System.out::println);

			findSubCategory(sonFunctions,parentFunctions);
//			for(FuncParents fp : parentFunctions){
//				System.out.println(fp.getFunName()+":");
//				System.out.println(fp.getChildFunctions());
//			}


			setAttr("parentFunctions", parentFunctions);
			// 跳转到 index.html 页面
			render("index.html");
		} else {
			// 用户名和密码不匹配，跳转到 login.html 页面
			redirect("login.html");
		}
	}
}
