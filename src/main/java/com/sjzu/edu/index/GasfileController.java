package com.sjzu.edu.index;


import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;


import com.sjzu.edu.common.model.GasFile;
import com.sjzu.edu.common.model.GasStation;
import com.sjzu.edu.common.model.Restaurant;

import java.util.List;


@Path(value = "/", viewPath = "/gasFile")
public class GasfileController extends Controller {

    @Inject
    GasfileService service;
    private GasStation gasstation= new GasStation().dao();

    public void gasFilelist() {
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        String gasNumber = getPara("gasNumber");
        String fentiancode = getPara("fentiancode");
        String filling_specification = getPara("filling_specification");
        System.out.println("gasNumber:"+gasNumber);
        setAttr("gasNumber",gasNumber);
        setAttr("fentiancode",fentiancode);
        setAttr("filling_specification",filling_specification);
        setAttr("gasFiles", service.search(pageNumber, 10,gasNumber,fentiancode,filling_specification));
        // 渲染搜索结果页面
        render("gasFiles.html");
    }

    public void edit() {
        List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
        setAttr("gasstations", gasstations);

        setAttr("gas_file", service.findById(getParaToInt()));
    }

    public void add() {
        List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
        setAttr("gasstations", gasstations);
        // 新增：将gasstations转为JSON字符串并设置到属性中
        setAttr("gasstationsJson", com.jfinal.kit.JsonKit.toJson(gasstations));
        setAttr("gas_file", new GasFile());
        System.out.println("gasstations.size()"+gasstations.size());
        render("add.html");
    }

    // ========== 改造save方法：增加重复校验 ==========
    public void save() {
        try {
            // 1. 获取前端提交的气瓶档案数据
            GasFile gasfile = getModel(GasFile.class, "gas_file");
            String gasNumber = gasfile.getGasNumber();
            int stationid = gasfile.getStationid();
            System.out.println("接收新增气瓶档案：气瓶编号=" + gasNumber);

            // 2. 校验气瓶编号是否重复
            boolean isDuplicate = service.isGasNumberDuplicate(gasNumber,stationid);
            if (isDuplicate) {
                // 2.1 重复：返回错误提示（跳转回新增页面并携带错误信息）
                System.err.println("新增失败：气瓶编号[" + gasNumber + "]已存在！");
                // 回显新增页面的加气站列表（保持页面数据）
                List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
                setAttr("gasstations", gasstations);
                // 携带错误信息和已填写的表单数据（回显）
                setAttr("errorMsg", "新增失败：气瓶编号「" + gasNumber + "」已存在，请更换！");
                setAttr("gas_file", gasfile); // 回显用户已填写的内容
                render("add.html"); // 渲染新增页面
                return;
            }

            // 3. 不重复：正常保存并跳转列表页
            System.out.println("气瓶编号[" + gasNumber + "]未重复，执行保存");
            gasfile.save();
            redirect("/gasFile/gasFilelist"); // 保存成功跳转到列表页
        } catch (Exception e) {
            // 异常处理：打印日志并返回错误提示
            System.err.println("新增气瓶档案异常：" + e.getMessage());
            e.printStackTrace();
            // 回显新增页面
            List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
            setAttr("gasstations", gasstations);
            setAttr("errorMsg", "新增失败：系统异常，请稍后重试！");
            render("add.html");
        }
    }

    // GasfileController新增方法
    public void checkGasNumber() {
        String gasNumber = getPara("gasNumber");
        String stationid = getPara("stationId");
        int stationId = (stationid == null || stationid.trim().isEmpty()) ? 0 : Integer.parseInt(stationid);
        boolean isDuplicate = service.isGasNumberDuplicate(gasNumber,stationId);
        renderText(isDuplicate ? "duplicate" : "ok");
    }

    public void update() {
        GasFile gasFile = getModel(GasFile.class, "gas_file");
        gasFile.update();
        redirect("/gasFile/gasFilelist");
    }
    public void delete() {
        // ========== 1. 打印删除操作开始标识 ==========
        System.out.println("=====================================");
        System.out.println("========== 开始执行删除操作 ==========");
        System.out.println("=====================================");

        try {
            // ========== 2. 打印请求基础信息 ==========
            System.out.println("\n【1. 请求基础信息】");
            System.out.println("请求URL: " + getRequest().getRequestURL());
            System.out.println("请求方法: " + getRequest().getMethod());
            System.out.println("完整Query参数: " + (getRequest().getQueryString() == null ? "无" : getRequest().getQueryString()));
            System.out.println("请求路径参数列表: " + getParaNames());

            // ========== 3. 打印并校验删除ID ==========
            System.out.println("\n【2. 删除ID相关信息】");
            // 从路径参数获取ID（/delete/8784 中的8784）
            Integer id = getParaToInt(0);
            System.out.println("从路径参数[0]获取的删除ID: " + id);
            System.out.println("ID数据类型: " + (id == null ? "null" : id.getClass().getName()));

            // 兜底检查：尝试从参数名"id"获取（防止路径参数获取失败）
            Integer idFromParam = getParaToInt("id");
            System.out.println("从参数名'id'获取的ID: " + idFromParam);

            // ID有效性校验
            if (id == null || id <= 0) {
                System.err.println("【错误】删除ID为空或无效！当前ID值: " + id);
                renderError(400, "删除ID不能为空且必须为正整数！");
                return;
            }
            System.out.println("ID校验通过，为有效正整数: " + id);

            // ========== 4. 打印搜索参数 ==========
            System.out.println("\n【3. 搜索参数信息】");
            String gasNumber = getPara("gasNumber", "");
            String fentiancode = getPara("fentiancode", "");
            String filling_specification = getPara("filling_specification", "");

            System.out.println("气瓶编号(gasNumber): '" + gasNumber + "'");
            System.out.println("奉天码(fentiancode): '" + fentiancode + "'");
            System.out.println("充装规格(filling_specification): '" + filling_specification + "'");
            System.out.println("所有请求参数Map: " + getParaMap());

            // ========== 5. 执行删除操作 ==========
            System.out.println("\n【4. 执行删除操作】");
            System.out.println("准备删除ID为 " + id + " 的气瓶档案记录");

            boolean deleteSuccess = false;
            try {
                // 优先调用service层删除（恢复原有业务逻辑）
                service.delete(id);
                deleteSuccess = true;

                // 调试打印：删除成功确认
                System.out.println("✅ Service层执行delete(" + id + ")成功");
                System.out.println("ID=" + id + " 的记录已从数据库删除");
            } catch (Exception e) {
                System.err.println("❌ Service层执行delete(" + id + ")失败！");
                System.err.println("异常类型: " + e.getClass().getName());
                System.err.println("异常消息: " + e.getMessage());
                System.err.println("异常完整堆栈: ");
                e.printStackTrace(System.out); // 打印完整堆栈到控制台
                throw e; // 抛出异常触发外层catch
            }

            // ========== 6. 构造并打印重定向URL ==========
            System.out.println("\n【5. 重定向信息】");
            String encodedGasNumber = java.net.URLEncoder.encode(gasNumber, "UTF-8");
            String encodedFentiancode = java.net.URLEncoder.encode(fentiancode, "UTF-8");
            String encodedFillingSpec = java.net.URLEncoder.encode(filling_specification, "UTF-8");

            String redirectUrl = "/gasFile/gasFilelist"
                    + "?gasNumber=" + encodedGasNumber
                    + "&fentiancode=" + encodedFentiancode
                    + "&filling_specification=" + encodedFillingSpec;

            System.out.println("编码前搜索参数: gasNumber=" + gasNumber + ", fentiancode=" + fentiancode + ", filling_specification=" + filling_specification);
            System.out.println("编码后重定向URL: " + redirectUrl);

            // 执行重定向
            redirect(redirectUrl);
            System.out.println("✅ 重定向已触发，目标URL: " + redirectUrl);

        } catch (Exception e) {
            // ========== 7. 全局异常捕获与打印 ==========
            System.err.println("\n【6. 全局异常捕获】");
            System.err.println("❌ 删除操作发生全局异常！");
            System.err.println("异常类型: " + e.getClass().getName());
            System.err.println("异常消息: " + e.getMessage());
            System.err.println("异常完整堆栈: ");
            e.printStackTrace(System.out);

            // 打印异常根因（递归获取最底层异常）
            Throwable rootCause = e;
            int causeLevel = 0;
            while (rootCause != null) {
                System.err.println("\n异常根因[" + causeLevel + "]:");
                System.err.println("  类型: " + rootCause.getClass().getName());
                System.err.println("  消息: " + rootCause.getMessage());
                rootCause = rootCause.getCause();
                causeLevel++;
            }

            // 返回500错误
            renderError(500, "删除失败：" + e.getMessage());
        } finally {
            // ========== 8. 操作结束标识 ==========
            System.out.println("\n=====================================");
            System.out.println("========== 删除操作执行结束 ==========");
            System.out.println("=====================================\n");
        }
    }

    public void search() {
        // String gasNumber = getPara("gasNumber");
        // 获取分页参数，如果不存在则使用默认值
        int pageNumber = getParaToInt("page", 1); // 默认为第1页
        String gasNumber = getPara("gasNumber");
        String valve_body_code = getPara("valve_body_code");
        String filling_specification = getPara("filling_specification");
        System.out.println("gasNumber:"+gasNumber);
        setAttr("gasNumber",gasNumber);
        setAttr("valve_body_code",valve_body_code);
        setAttr("filling_specification",filling_specification);
        setAttr("gasFiles", service.search(pageNumber, 10,gasNumber,valve_body_code,filling_specification));
        // 渲染搜索结果页面
        render("gasFiles.html");


    }

}





