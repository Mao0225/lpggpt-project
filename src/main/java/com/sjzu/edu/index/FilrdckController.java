package com.sjzu.edu.index;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.FillRecordCheck1;
import com.sjzu.edu.common.model.GasStation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Path(value = "/", viewPath = "/filrdck")
public class FilrdckController extends Controller {

    @Inject
    FilrecordckServive service;

    private GasStation stationDao = new GasStation().dao();

    // 列表查询
    public void filrdcklist() {
        int pageNumber = getParaToInt("pageNum", 1);
        int pageSize = getParaToInt("size", 10);

        String finditemStr = getPara("finditem");
        Timestamp finditem = null;

        String gastion = getPara("gastion_select");
        String gasname = getPara("gasname");

        // 日期解析
        if (finditemStr != null && !finditemStr.isEmpty()) {
            try {
                LocalDate parsedDate = LocalDate.parse(finditemStr);
                finditem = Timestamp.valueOf(parsedDate.atStartOfDay());
            } catch (DateTimeParseException e) {
                setAttr("errorMessage", "日期格式错误，请检查输入。");
                render("errorPage.html");
                return;
            }
        }

        // 查询数据
        Page<FillRecordCheck1> result = service.search(pageNumber, pageSize, finditem, gastion, gasname);
        if (result == null) {
            result = new Page<>(new ArrayList<>(), 0, pageNumber, pageSize, 0);
        }

        // 查询站点列表
        List<GasStation> stations = stationDao.find("SELECT * FROM gas_station");
        if (stations == null) stations = new ArrayList<>();

        List<GasStation> gastiond = service.pagdetail();
        if (gastiond == null) gastiond = new ArrayList<>();

        setAttr("filrdck", result);
        setAttr("stations", stations);
        setAttr("gastiond", gastiond);
        setAttr("finditem", finditemStr);
        setAttr("gastion", gastion);
        setAttr("gasname", gasname);

        render("filrdck.html");
    }

    // 编辑
    public void edit() {
        setAttr("filrdck", service.findById(getParaToInt()));
    }

    // 新增
    public void add() {
        render("add.html");
    }

    // 更新
    public void update() {
        getModel(FillRecordCheck1.class).update();
        redirect("/filrdck/filrdcklist");
    }

    // 保存
    public void save() {
        getModel(FillRecordCheck1.class).save();
        redirect("/filrdck/filrdcklist");
    }

    // 删除
    public void delete() {
        service.deleteById(getParaToInt());
        redirect("/filrdck/filrdcklist");
    }


}
