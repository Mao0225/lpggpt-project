package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasBottle;

@Path(value = "/", viewPath = "/user")
public class GasBottleController extends Controller {
    @Inject
    GasBottleService service;

    public void gasbottlelist() {
        // 从请求中获取参数
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        int pageSize = getParaToInt("size", 10);
        String bottleId = getPara("bottleid");
        String transState = getPara("transstate");
        String carId = getPara("carid");
        String startTime = getPara("starttime");
        String endTime = getPara("endtime");
        String tel = getPara("tel");
        setAttr("bottleid",bottleId);
        setAttr("transstate",transState);
        setAttr("carid",carId);
        setAttr("starttime",startTime);
        setAttr("endtime",endTime);
        setAttr("tel",tel);

        // 调用服务层的方法进行搜索
        Page<GasBottle> gasBottlePage = service.search(pageNumber, pageSize, bottleId, transState, carId, startTime, endTime, tel);

        // 将结果设置为属性
        setAttr("gasbottlelist", gasBottlePage);
        // 渲染页面
        render("gasbottles.html");
    }
}


