package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Plcxinhao;

public class PlcxinhaoController extends Controller {
    // 初始化Service
    PlcxinhaoService service = new PlcxinhaoService();

    /**
     * 显示PLC信号列表页面（带分页和筛选）
     */
    public void list() {
        // 获取分页参数
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", 10);

        // 获取查询参数
        String plcno = getPara("plcno");
        String alarmType =getPara("alarmType");

        // 保存参数到页面，用于回显
        setAttr("plcno", plcno);
        setAttr("alarmType",alarmType);

        // 关键修改：接收多表查询结果（Page<Record>）
        Page<Record> plcPage = service.paginate(pageNumber, pageSize, plcno,alarmType);
        setAttr("plcList", plcPage);

        // 渲染页面
        render("plcxinhao_list.html");
    }

    /**
     * 删除记录
     */
    public void delete() {
        Integer id = getParaToInt("id");
        if (id != null) {
            service.deleteById(id);
        }
        // 重定向回列表页，保持筛选条件
        String plcno = getPara("plcno");
        String alarmType = getPara("alarmType");
        String redirectUrl = "/plcxinhao/list?page=1";
        if(plcno !=null && !plcno.isEmpty()){
            redirectUrl +=  "&plcno=" + plcno;
        }
        if(alarmType !=null && !alarmType.isEmpty()){
            redirectUrl +="&alarmType=" +alarmType;
        }
        redirect(redirectUrl);
    }

    /**
     * 查看详情
     */
    public void detail() {
        Integer id = getParaToInt("id");
        Plcxinhao plc = service.findById(id);
        setAttr("plc", plc);
        render("plcxinhao_detail.html");
    }
}