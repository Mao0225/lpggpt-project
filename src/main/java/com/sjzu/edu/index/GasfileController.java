package com.sjzu.edu.index;


import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;

import com.sjzu.edu.common.model.GasFile;
import com.sjzu.edu.common.model.GasStation;
import com.sjzu.edu.common.model.Restaurant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Path(value = "/", viewPath = "/gasFile")
public class GasfileController extends Controller {

    @Inject
    GasfileService service;
    private GasStation gasstation= new GasStation().dao();

    public void gasFilelist() {
        int pageNumber = getParaToInt("page", 1);
        String gasNumber = getPara("gasNumber");
        String fentiancode = getPara("fentiancode");
        String filling_specification = getPara("filling_specification");
        String expiredStatus = getPara("expiredStatus", "all");
        // 新增：获取上传状态参数
        String uploadStatus = getPara("uploadStatus", "all");

        // 新增：获取当前日期并传递到前端
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        setAttr("currentDate", sdf.format(new Date()));

        System.out.println("gasNumber:"+gasNumber);
        setAttr("gasNumber",gasNumber);
        setAttr("fentiancode",fentiancode);
        setAttr("filling_specification",filling_specification);
        setAttr("expiredStatus", expiredStatus);
        // 新增：传递上传状态参数
        setAttr("uploadStatus", uploadStatus);
        // 传递所有参数到service
        setAttr("gasFiles", service.search(pageNumber, 10, gasNumber, fentiancode, filling_specification, expiredStatus, uploadStatus));
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
        setAttr("gasstationsJson", com.jfinal.kit.JsonKit.toJson(gasstations));
        setAttr("gas_file", new GasFile());
        System.out.println("gasstations.size()"+gasstations.size());
        render("add.html");
    }

    public void save() {
        try {
            GasFile gasfile = getModel(GasFile.class, "gas_file");
            String gasNumber = gasfile.getGasNumber();
            int stationid = gasfile.getStationid();
            System.out.println("接收新增气瓶档案：气瓶编号=" + gasNumber);

            boolean isDuplicate = service.isGasNumberDuplicate(gasNumber,stationid);
            if (isDuplicate) {
                System.err.println("新增失败：气瓶编号[" + gasNumber + "]已存在！");
                List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
                setAttr("gasstations", gasstations);
                setAttr("errorMsg", "新增失败：气瓶编号「" + gasNumber + "」已存在，请更换！");
                setAttr("gas_file", gasfile);
                render("add.html");
                return;
            }

            System.out.println("气瓶编号[" + gasNumber + "]未重复，执行保存");
            gasfile.save();
            redirect("/gasFile/gasFilelist");
        } catch (Exception e) {
            System.err.println("新增气瓶档案异常：" + e.getMessage());
            e.printStackTrace();
            List<GasStation> gasstations = gasstation.find("SELECT id, station_name FROM gas_station");
            setAttr("gasstations", gasstations);
            setAttr("errorMsg", "新增失败：系统异常，请稍后重试！");
            render("add.html");
        }
    }

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
        System.out.println("=====================================");
        System.out.println("========== 开始执行删除操作 ==========");
        System.out.println("=====================================");

        try {
            System.out.println("\n【1. 请求基础信息】");
            System.out.println("请求URL: " + getRequest().getRequestURL());
            System.out.println("请求方法: " + getRequest().getMethod());
            System.out.println("完整Query参数: " + (getRequest().getQueryString() == null ? "无" : getRequest().getQueryString()));
            System.out.println("请求路径参数列表: " + getParaNames());

            System.out.println("\n【2. 删除ID相关信息】");
            Integer id = getParaToInt(0);
            System.out.println("从路径参数[0]获取的删除ID: " + id);
            System.out.println("ID数据类型: " + (id == null ? "null" : id.getClass().getName()));

            Integer idFromParam = getParaToInt("id");
            System.out.println("从参数名'id'获取的ID: " + idFromParam);

            if (id == null || id <= 0) {
                System.err.println("【错误】删除ID为空或无效！当前ID值: " + id);
                renderError(400, "删除ID不能为空且必须为正整数！");
                return;
            }
            System.out.println("ID校验通过，为有效正整数: " + id);

            System.out.println("\n【3. 搜索参数信息】");
            String gasNumber = getPara("gasNumber", "");
            String fentiancode = getPara("fentiancode", "");
            String filling_specification = getPara("filling_specification", "");
            String expiredStatus = getPara("expiredStatus", "all");
            // 新增：获取上传状态参数
            String uploadStatus = getPara("uploadStatus", "all");

            System.out.println("气瓶编号(gasNumber): '" + gasNumber + "'");
            System.out.println("奉天码(fentiancode): '" + fentiancode + "'");
            System.out.println("充装规格(filling_specification): '" + filling_specification + "'");
            System.out.println("过期状态(expiredStatus): '" + expiredStatus + "'");
            System.out.println("上传状态(uploadStatus): '" + uploadStatus + "'");
            System.out.println("所有请求参数Map: " + getParaMap());

            System.out.println("\n【4. 执行删除操作】");
            System.out.println("准备删除ID为 " + id + " 的气瓶档案记录");

            boolean deleteSuccess = false;
            try {
                service.delete(id);
                deleteSuccess = true;
                System.out.println("✅ Service层执行delete(" + id + ")成功");
                System.out.println("ID=" + id + " 的记录已从数据库删除");
            } catch (Exception e) {
                System.err.println("❌ Service层执行delete(" + id + ")失败！");
                System.err.println("异常类型: " + e.getClass().getName());
                System.err.println("异常消息: " + e.getMessage());
                System.err.println("异常完整堆栈: ");
                e.printStackTrace(System.out);
                throw e;
            }

            System.out.println("\n【5. 重定向信息】");
            String encodedGasNumber = java.net.URLEncoder.encode(gasNumber, "UTF-8");
            String encodedFentiancode = java.net.URLEncoder.encode(fentiancode, "UTF-8");
            String encodedFillingSpec = java.net.URLEncoder.encode(filling_specification, "UTF-8");
            String encodedExpiredStatus = java.net.URLEncoder.encode(expiredStatus, "UTF-8");
            // 新增：编码上传状态参数
            String encodedUploadStatus = java.net.URLEncoder.encode(uploadStatus, "UTF-8");

            // 重定向URL包含所有参数
            String redirectUrl = "/gasFile/gasFilelist"
                    + "?gasNumber=" + encodedGasNumber
                    + "&fentiancode=" + encodedFentiancode
                    + "&filling_specification=" + encodedFillingSpec
                    + "&expiredStatus=" + encodedExpiredStatus
                    + "&uploadStatus=" + encodedUploadStatus;

            System.out.println("编码前搜索参数: gasNumber=" + gasNumber + ", fentiancode=" + fentiancode + ", filling_specification=" + filling_specification + ", expiredStatus=" + expiredStatus + ", uploadStatus=" + uploadStatus);
            System.out.println("编码后重定向URL: " + redirectUrl);

            redirect(redirectUrl);
            System.out.println("✅ 重定向已触发，目标URL: " + redirectUrl);

        } catch (Exception e) {
            System.err.println("\n【6. 全局异常捕获】");
            System.err.println("❌ 删除操作发生全局异常！");
            System.err.println("异常类型: " + e.getClass().getName());
            System.err.println("异常消息: " + e.getMessage());
            System.err.println("异常完整堆栈: ");
            e.printStackTrace(System.out);

            Throwable rootCause = e;
            int causeLevel = 0;
            while (rootCause != null) {
                System.err.println("\n异常根因[" + causeLevel + "]:");
                System.err.println("  类型: " + rootCause.getClass().getName());
                System.err.println("  消息: " + rootCause.getMessage());
                rootCause = rootCause.getCause();
                causeLevel++;
            }

            renderError(500, "删除失败：" + e.getMessage());
        } finally {
            System.out.println("\n=====================================");
            System.out.println("========== 删除操作执行结束 ==========");
            System.out.println("=====================================\n");
        }
    }

    public void search() {
        int pageNumber = getParaToInt("page", 1);
        String gasNumber = getPara("gasNumber");
        String valve_body_code = getPara("valve_body_code");
        String filling_specification = getPara("filling_specification");
        String expiredStatus = getPara("expiredStatus", "all");
        // 新增：获取上传状态参数
        String uploadStatus = getPara("uploadStatus", "all");

        // 新增：获取当前日期并传递到前端
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        setAttr("currentDate", sdf.format(new Date()));

        System.out.println("gasNumber:"+gasNumber);
        setAttr("gasNumber",gasNumber);
        setAttr("valve_body_code",valve_body_code);
        setAttr("filling_specification",filling_specification);
        setAttr("expiredStatus", expiredStatus);
        // 新增：传递上传状态参数
        setAttr("uploadStatus", uploadStatus);
        setAttr("gasFiles", service.search(pageNumber, 10, gasNumber, valve_body_code, filling_specification, expiredStatus, uploadStatus));
        render("gasFiles.html");
    }

}