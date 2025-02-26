package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Func;
import com.sjzu.edu.common.model.Userfunction;

import java.util.List;

@Path(value = "/", viewPath = "/userFunc")
public class UserFunctionController extends Controller {

    private Func func= new Func().dao();
    private Userfunction userfunction = new Userfunction().dao();


    @Inject
    UserService userService;
    //点击之后获取userlist 同理用户管理
    public void userList() {
        //从数据库获取user数据
        setAttr("users", userService.paginate(getParaToInt(0, 1), 10));
        render("user.html");//连接的 userFunc 中的 user
    }

    @Inject
    FuncService funcService;
    //点击之后获取funclist 同理功能管理
    public void funclist(){
        //获取用户id
        String userId = getPara("userId");

        //自动拼接参数
        String pageNum = getPara("pageNum");
        if (pageNum==null||pageNum.length()==0){
            pageNum="1";
        }

        // 查找这个用户拥有的功能
        List<Func> functions = func.find("SELECT * FROM func WHERE id in (select functionid from userfunction where userid = ?)", userId);

        //获取functionid
        StringBuilder stringBuilder = new StringBuilder();
        String haveFunc;
        if (functions.size()!=0) {
            for (Func function : functions) {
                stringBuilder.append(function.getId()).append(",");
            }
            haveFunc = stringBuilder.substring(0,stringBuilder.length()-1);
        }else{
            haveFunc = "";
        }

        setAttr("haveFunc",haveFunc);

        setAttr("funcs", funcService.paginate(Integer.parseInt(pageNum), 10));
        setAttr("userId",userId);
        render("func.html");
    }

    @Inject
    UserFunctionService userFunctionService;
    //从前台接收数据 传过来的参数是key-value形式的
    public void sure(){

        //获取funcids
        String funcId = getPara("id");

        //获取userid
        String userId = getPara("userId");

        //获取页面
        String pageNum = getPara("pageNum");

        //新增 从勾选到未勾选 库里没有就新增
        List<Userfunction> userfunctions = userfunction.find("SELECT * FROM userfunction WHERE userid=? AND functionid=? ",userId,funcId );
        if (userfunctions==null||userfunctions.size()==0) {
            Userfunction bean = getBean(Userfunction.class);
            bean.setUserid(Integer.parseInt(userId));
            bean.setFunctionid(Integer.parseInt(funcId));
            bean.save();
        }else {//删除 从未勾选到勾选 库里有就删除
            userfunctions.get(0).delete();
        }
        //重定向到 参考usercontroller 同理点击授权后一系列动作
        redirect("/userFunc/funclist?userId="+userId+"&pageNum="+pageNum);
    }

}


