package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.FillRecordCheck1;
import com.sjzu.edu.common.model.GasStation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FfilrdckController extends Controller {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    @Inject
    FfilrecordckServive service;



    public void filrdcklist() {
        int pageNumber = getParaToInt("pageNum", 1); // 默认为第 1 页
        int pageSize = getParaToInt("size", 10); // 默认每页 10 条记录

        // 获取前端传递的时间范围
        String finditemStartStr = getPara("finditem_start");
        String finditemEndStr = getPara("finditem_end");
        Timestamp finditemStart = null;
        Timestamp finditemEnd = null;

        String gastion = getPara("gastion");
        System.out.println(gastion);
        String gasname = getPara("gasname");
        String companyid = getSessionAttr("companyid");
        System.out.println(companyid);
        // 解析日期时间字符串（时间范围）
        try {
            if (finditemStartStr != null && !finditemStartStr.isEmpty()) {
                LocalDate parsed = LocalDate.parse(finditemStartStr);
                finditemStart = Timestamp.valueOf(parsed.atStartOfDay());
            }
            if (finditemEndStr != null && !finditemEndStr.isEmpty()) {
                LocalDate parsed = LocalDate.parse(finditemEndStr);
                finditemEnd = Timestamp.valueOf(parsed.atStartOfDay());
            }
        } catch (DateTimeParseException e) {
            System.err.println("Invalid datetime format: " + finditemStartStr + " / " + finditemEndStr);
            setAttr("errorMessage", "日期格式错误，请检查输入。");
            render("errorPage.html");
            return;
        }

        // 调用 service 进行查询
        Page<FillRecordCheck1> result = service.search(pageNumber, pageSize, finditemStart, finditemEnd, gastion, gasname, companyid);
        System.out.println("获取到的充装信息为"+result.getList());
        List<GasStation> resultd = service.pagdetail(companyid);
        setAttr("finditem_start", finditemStartStr);
        setAttr("finditem_end", finditemEndStr);
        setAttr("gastion", gastion);
        setAttr("gasname", gasname);
        setAttr("filrdck", result);
        setAttr("gastiond", resultd);

        // 渲染页面
        render("filrdck.html");
    }



    public void edit() {
        String acItemName = getPara();
        System.out.println(acItemName);
        setAttr("filrdck", service.findById(getParaToInt()));

    }
    public void add() {

        render("add.html");

    }
    public void update() {

        getBean(FillRecordCheck1.class).update();
        redirect("/filrdck/filrdcklist");
    }
    public void save() {

        System.out.println(FillRecordCheck1.class);
        getBean(FillRecordCheck1.class).save();
        redirect("/filrdck/filrdcklist");
    }
    public void delete() {
        System.out.println("delete function");
        service.deleteById(getParaToInt());
        redirect("/filrdck/filrdcklist");
    }


}
