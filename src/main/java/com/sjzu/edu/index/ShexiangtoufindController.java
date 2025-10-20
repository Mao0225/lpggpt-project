package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Shexiangtou;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/shexiangtoufind")
public class ShexiangtoufindController extends Controller {
    @Inject
    RestaurantService service;
    private Shexiangtou dao = new Shexiangtou().dao();

    public void shexiangtoulist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10); // 默认每页10条记录
        String shexiangtouno = getPara("shexiangtouno"); // 获取参数
        String searchDate = getPara("searchDate");
        String searchTime = getPara("searchTime");

        // 构建查询语句
        String select = "SELECT shexiangtou.*, basshexiangtouinfo.restaurantname, basshexiangtouinfo.restaurantid, basshexiangtouinfo.restaurantphone, basshexiangtouinfo.restaurantaddress";
        String from = "FROM shexiangtou LEFT JOIN basshexiangtouinfo ON shexiangtou.shexiangtouno = basshexiangtouinfo.shexiangtoubianhao";

        //构建WHERE条件
        StringBuilder where = new StringBuilder();
        boolean hasCondition = false;
        // 添加条件
        if (shexiangtouno != null && !shexiangtouno.trim().isEmpty()) {
            where.append("WHERE shexiangtou.shexiangtouno LIKE '%").append(shexiangtouno).append("%'");
            hasCondition = true;
        }
        //添加日期条件
        if (searchDate != null && !searchDate.trim().isEmpty()) {
            if (hasCondition) {
                where.append(" AND DATE(shexiangtou.happendtime) = '").append(searchDate).append("'");
            } else {
                where.append(" WHERE DATE(shexiangtou.happendtime) = '").append(searchDate).append("'");
            }
            hasCondition = true;
        }
        // 添加时间条件（新增）
        if (searchTime != null && !searchTime.trim().isEmpty()) {
            if (where.length()>0) {
                where.append(" AND ");
            } else {
                where.append(" WHERE ");
                hasCondition = true;
            }
            where.append("TIME(shexiangtou.happendtime)>= '").append(searchTime).append("%'");
        }

        if(where.length() > 0){
            from += " " + where.toString();
        }
        from += " ORDER BY shexiangtou.id DESC";

        // 设置页面属性
        setAttr("shexiangtouno", shexiangtouno != null ? shexiangtouno : "");
        setAttr("searchDate", searchDate != null ? searchDate : "");
        setAttr("searchTime", searchTime != null ? searchTime : "");
         // 调试信息
        System.out.println("=== SQL调试信息 ===");
        System.out.println("摄像头编号: " + shexiangtouno);
        System.out.println("搜索日期: " + searchDate);
        System.out.println("搜索时间: " + searchTime);
        System.out.println("完整SQL: " + select + " " + from);
        System.out.println("=================");

        try {
            setAttr("shexiangtou", dao.paginate(pageNumber, pageSize, select, from));
            render("shexiangtous.html");
        } catch (Exception e) {
            e.printStackTrace();
            // 如果出错，返回空数据
            setAttr("shexiangtou", dao.paginate(pageNumber, pageSize, select, from));
            render("shexiangtous.html");
        }
    }
}