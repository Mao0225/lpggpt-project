package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.AutoGasFillingWaitRecord;

public class JjiaqizhongController extends Controller {
    JjiaqizhongService service = new JjiaqizhongService();

    /**
     * 处理分页查询请求，将查询结果传递到前端页面
     */
    public void Jjiaqilist() {
        // 获取当前页码，默认为第 1 页
        int pageNumber = getParaToInt("page", 1);
        // 获取每页显示的记录数，默认为 10 条
        int pageSize = getParaToInt("size", 10);
        String license_plate = getPara("license_plate");
      Integer gun_no = getParaToInt("gun_no");
        // 获取状态参数
        String companyid = getSessionAttr("companyid"); // 从会话中获取 companyid
        // 将参数回显到前端页面
        setAttr("license_plate", license_plate);
        setAttr("gun_no", gun_no);
        setAttr("companyid", companyid);
        // 调用 Service 层的分页查询方法
        Page<AutoGasFillingWaitRecord> recordPage = service.paginate(pageNumber, pageSize,license_plate, gun_no,companyid);
        // 将查询结果传递到前端页面
        setAttr("JrecordList", recordPage);

        // 打印查询结果，方便调试
        System.out.println("JrecordPage: " + recordPage);

        // 渲染前端页面
        render("Jjiaqizhong.html");
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
        redirect("/Jjiaqizhong/Jjiaqilist");
    }
}
