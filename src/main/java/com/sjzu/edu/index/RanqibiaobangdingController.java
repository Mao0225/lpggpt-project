package com.sjzu.edu.index;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Ranqibiaobangding;

import java.util.ArrayList;
import java.util.List;

/**
 * RanqibiaobangdingController
 */
@Path(value = "/", viewPath = "/ranqibiaobangding")
@Before(UserInterceptor.class)
public class RanqibiaobangdingController extends Controller {
    @Inject
    RanqibiaobangdingService service;

    private static final String REDIRECT_LIST = "/ranqibiaobangding/ranqibiaobangdinglist";
    private static final String ADD_VIEW = "add.html";
    private static final String ERROR_VIEW = "error.html"; // 假设有一个错误页面

    public void ranqibiaobangdinglist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第 1 页
        int pageSize = getParaToInt("size", 10); // 默认每页 10 条记录

        // 获取搜索参数
        String ranqibiaono = getPara("ranqibiaono");
        String qipingno = getPara("qipingno");
        String saomiaoshijian = getPara("saomiaoshijian");
        String saomiaoren = getPara("saomiaoren");
        String biaodushu = getPara("biaodushu");
        String biaodushuriqi = getPara("biaodushuriqi");
        String jingdu = getPara("jingdu");
        String weidu = getPara("weidu");
        String address = getPara("address");

        // 设置搜索参数到视图
        setAttr("ranqibiaono", ranqibiaono);
        setAttr("qipingno", qipingno);
        setAttr("saomiaoshijian", saomiaoshijian);
        setAttr("saomiaoren", saomiaoren);
        setAttr("biaodushu", biaodushu);
        setAttr("biaodushuriqi", biaodushuriqi);
        setAttr("jingdu", jingdu);
        setAttr("weidu", weidu);
        setAttr("address", address);

        // 动态构建 SELECT 和 FROM 子句
        StringBuilder sqlSelect = new StringBuilder("SELECT * ");
        StringBuilder sqlFrom = new StringBuilder("FROM ranqibiaobangding WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // 根据搜索条件动态构建 WHERE 子句
        if (ranqibiaono != null && !ranqibiaono.isEmpty()) {
            sqlFrom.append(" AND ranqibiaono LIKE ?");
            params.add("%" + ranqibiaono + "%");
        }
        if (qipingno != null && !qipingno.isEmpty()) {
            sqlFrom.append(" AND qipingno LIKE ?");
            params.add("%" + qipingno + "%");
        }
        if (saomiaoshijian != null && !saomiaoshijian.isEmpty()) {
            sqlFrom.append(" AND saomiaoshijian LIKE ?");
            params.add("%" + saomiaoshijian + "%");
        }
        if (saomiaoren != null && !saomiaoren.isEmpty()) {
            sqlFrom.append(" AND saomiaoren LIKE ?");
            params.add("%" + saomiaoren + "%");
        }
        if (biaodushu != null && !biaodushu.isEmpty()) {
            sqlFrom.append(" AND biaodushu LIKE ?");
            params.add("%" + biaodushu + "%");
        }
        if (biaodushuriqi != null && !biaodushuriqi.isEmpty()) {
            sqlFrom.append(" AND biaodushuriqi LIKE ?");
            params.add("%" + biaodushuriqi + "%");
        }
        if (jingdu != null && !jingdu.isEmpty()) {
            sqlFrom.append(" AND jingdu LIKE ?");
            params.add("%" + jingdu + "%");
        }
        if (weidu != null && !weidu.isEmpty()) {
            sqlFrom.append(" AND weidu LIKE ?");
            params.add("%" + weidu + "%");
        }
        if (address != null && !address.isEmpty()) {
            sqlFrom.append(" AND address LIKE ?");
            params.add("%" + address + "%");
        }

        // 添加排序条件
        sqlFrom.append(" ORDER BY id DESC");

        // 执行分页查询
        Page<Record> page = Db.paginate(pageNumber, pageSize, sqlSelect.toString(), sqlFrom.toString(), params.toArray());

        // 设置查询结果到视图
        setAttr("ranqibiaobangding", page);

        // 渲染视图
        render("ranqibiaobangding.html");
    }


    public void searchdata() {
        ranqibiaobangdinglist(); // 重用上述方法
    }

    public void edit() {
        Integer id = getParaToInt();
        if (id!= null) {
            Ranqibiaobangding ranqibiaobangding = service.findById(id);
            if (ranqibiaobangding!= null) {
                setAttr("ranqibiaobangding", ranqibiaobangding);
                render("edit.html"); // 假设有一个编辑页面
            } else {
                renderError(404); // 找不到资源
            }
        } else {
            renderError(400); // 错误的请求
        }
    }

    public void update() {
        Ranqibiaobangding ranqibiaobangding = getModel(Ranqibiaobangding.class, "ranqibiaobangding");
        if (ranqibiaobangding.update()) {
            redirect(REDIRECT_LIST);
        } else {
            setAttr("errorMsg", "更新失败，请重试。");
            render(ERROR_VIEW);
        }
    }

    public void add() {
        render(ADD_VIEW);
    }

    public void save() {
        Ranqibiaobangding ranqibiaobangding = getModel(Ranqibiaobangding.class, "ranqibiaobangding");
        if (ranqibiaobangding.save()) {
            redirect(REDIRECT_LIST);
        } else {
            setAttr("errorMsg", "保存失败，请重试。");
            render(ERROR_VIEW);
        }
    }

    public void delete() {
        Integer id = getParaToInt();
        if (id!= null) {
            service.deleteById(id);
        }
        redirect(REDIRECT_LIST);
    }
}