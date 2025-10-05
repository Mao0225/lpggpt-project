package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Plckongzhi;

public class PlckongzhiController extends Controller {
    private PlckongzhiService service = new PlckongzhiService();

    /**
     * 列表页展示，支持分页和plcno查询
     */
    public void index() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);
        String plcno = getPara("plcno");

        // 保存查询参数用于分页跳转
        setAttr("plcno", plcno);

        // 获取分页数据
        Page<Plckongzhi> plcPage = service.paginate(pageNumber, pageSize, plcno);
        setAttr("plcList", plcPage);

        render("plckongzhi.html");
    }

    /**
     * 跳转到添加页面
     */
    public void add() {
        render("plckongzhi_add.html");
    }

    /**
     * 保存新增数据
     */
    public void save() {
        Plckongzhi plc = getModel(Plckongzhi.class, "plc");
        if (plc.save()) {
            setAttr("msg", "添加成功");
        } else {
            setAttr("msg", "添加失败");
        }
        redirect("/plckongzhi");
    }

    /**
     * 跳转到编辑页面
     */
    public void edit() {
        Integer id = getParaToInt("id");
        if (id == null) {
            renderError(404);
            return;
        }
        Plckongzhi plc = service.findById(id);
        if (plc == null) {
            renderError(404);
            return;
        }
        setAttr("plc", plc);
        render("plckongzhi_edit.html");
    }

    /**
     * 更新数据
     */
    public void update() {
        Plckongzhi plc = getModel(Plckongzhi.class, "plc");
        if (plc.update()) {
            setAttr("msg", "更新成功");
        } else {
            setAttr("msg", "更新失败");
        }
        redirect("/plckongzhi");
    }

    /**
     * 删除数据
     */
    public void delete() {
        Integer id = getParaToInt("id");
        if (id != null) {
            service.deleteById(id);
        }
        // 保持删除后的查询状态
        String plcno = getPara("plcno");
        String redirectUrl = "/plckongzhi";
        if (StrKit.notBlank(plcno)) {
            redirectUrl += "?plcno=" + plcno;
        }
        redirect(redirectUrl);
    }
}