package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.AutoGasFillingWaitRecord;
import com.sjzu.edu.common.model.FillRecordCheck1;
import com.sjzu.edu.common.model.GasStation;

import java.util.List;

public class UnqualifiedController extends Controller {
    UnqualifiedService service = new UnqualifiedService();
    private GasStation gastation = new GasStation().dao();
    /**
     * 处理分页查询请求，将查询结果传递到前端页面
     */
    public void unqualifiedlist() {
        // 获取当前页码，默认为第 1 页
        int pageNumber = getParaToInt("page", 1);
        // 获取每页显示的记录数，默认为 10 条
        int pageSize = getParaToInt("size", 10);
        // 获取开始时间参数
        String bottle_no = getPara("gas_number");
        // 获取结束时间参数
        String fill_time = getPara("fill_time");
        // 获取状态参数
        String gun_no = getPara("gun_no");

        String companyId=getPara("companyId");
        System.out.println("companyId； "+companyId);
        // 将参数回显到前端页面
        setAttr("bottle_no", bottle_no);
        setAttr("fill_time", fill_time);
        setAttr("gun_no", gun_no);
        setAttr("companyId", companyId);
        // 调用 Service 层的分页查询方法
        Page<FillRecordCheck1> recordPage = service.paginate(pageNumber, pageSize,bottle_no,fill_time,gun_no,companyId);
        List<GasStation> gastations = gastation.find("SELECT * FROM gas_station");
        // 将查询结果传递到前端页面
        setAttr("recordList", recordPage);
        setAttr("gastations", gastations);
        // 打印查询结果，方便调试
        System.out.println("recordPage: " + recordPage);

        // 渲染前端页面
        render("unqualified.html");
    }

    /**
     * 处理删除记录的请求
     */
    public void delete() {
        // 获取要删除记录的 id
        Integer id = getParaToInt("id");
        // 打印要删除的记录 id，方便调试
        System.out.println("id: " + id);

        // 调用 Service 层的删除方法
        service.deleteById(id);

        // 重定向到列表页面
        redirect("/unqualified/unqualifiedlist");
    }
}
