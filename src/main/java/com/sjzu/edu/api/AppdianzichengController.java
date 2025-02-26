package com.sjzu.edu.api;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasBottle;
import com.sjzu.edu.common.model.IotSyncRdsRecordsV2;
import com.sjzu.edu.common.model.PressureGaugeRecords;
import com.sjzu.edu.common.model.PressureValueRecords;


import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Path(value = "/", viewPath = "/appdianzicheng")
@Clear
public class AppdianzichengController extends Controller {
    private IotSyncRdsRecordsV2 dao = new IotSyncRdsRecordsV2().dao();
    private PressureValueRecords dao1 = new PressureValueRecords().dao();

    private  PressureGaugeRecords dao2 = new PressureGaugeRecords().dao();



    public void dianzicheng() {
        String devicename = getPara("devicename");
        String yuliu1 = getPara("yuliu1");

        // 检查并去除"undefined"
        devicename = "undefined".equals(devicename) ? null : devicename;
        yuliu1 = "undefined".equals(yuliu1) ? null : yuliu1;

//        System.out.println("devicename: " + devicename);
//        System.out.println("yuliu1: " + yuliu1);


        // 检查是否为空
        if (devicename == null || yuliu1 == null) {
            renderJson(new JSONObject().fluentPut("error", "Received null or 'undefined' parameters"));
            return;
        }

        String sql = String.format("SELECT ValveStatus%s,Value%s_1,Value%s_2,Value%s_3 FROM t_iot_sync_rds_records_v2 WHERE ValveStatus%s IS NOT  NULL ",yuliu1,yuliu1,yuliu1,yuliu1,yuliu1);
        String sql2 =String.format("AND devicename = '%s' ORDER BY id DESC LIMIT 1",devicename);
        String finalsql = sql + sql2;


        List<IotSyncRdsRecordsV2> IotSyncRdsRecordsV2List = dao.find(finalsql);


        JSONObject json = new JSONObject();
        if (IotSyncRdsRecordsV2List != null && !IotSyncRdsRecordsV2List.isEmpty()) {
            json.put("flag", "200");
            json.put("dianzicheng", IotSyncRdsRecordsV2List);
        } else {
            json.put("flag", "300");
        }

        renderJson(json);
    }
    public void GetBiaoId(){
        String affiliation_user_id = getPara("affiliation_user_id");

        String sql = "SELECT * FROM t_pressure_gauge_records WHERE affiliation_user_id = ?";

        List<PressureGaugeRecords> PressureGaugeRecords = dao2.find(sql,affiliation_user_id);
        JSONObject json = new JSONObject();
        if (PressureGaugeRecords != null && !PressureGaugeRecords.isEmpty()) {
            int distinctControllerIdCount = PressureGaugeRecords.size();  //统计结果集中的不同的controller_id的数量
            json.put("flag", "200");
            json.put("biao", PressureGaugeRecords);
            json.put("distinctControllerIdCount", distinctControllerIdCount);
        } else {
            json.put("flag", "300");
        }

        renderJson(json);
    }
    public void biao() {
        String controller_id = getPara("controller_id");

        // 检查并去除"undefined"
        controller_id = "undefined".equals(controller_id) ? null : controller_id;

        // 检查是否为空
        if (controller_id == null) {
            renderJson(new JSONObject().fluentPut("error", "Controller ID is null or 'undefined'"));
            return;
        }

        // 使用参数化查询来避免SQL注入
        String sql = "SELECT controller_id, standard_condition_accumulated,valve_status, battery_voltage, temperature, pressure FROM t_pressure_value_records WHERE controller_id = ? ORDER BY ID DESC LIMIT 1" ;

        // 执行查询
        List<PressureValueRecords> pressureValueRecords = dao1.find(sql, controller_id);

        JSONObject json = new JSONObject();
        if (pressureValueRecords != null && !pressureValueRecords.isEmpty()) {
            PressureValueRecords record = pressureValueRecords.get(0);
            json.put("flag", "200");
            json.put("controller_id", record.getControllerId());

            json.put("standard_condition_accumulated", record.getStandardConditionAccumulated());
            json.put("valve_status", record.getValveStatus());
            json.put("battery_voltage", record.getBatteryVoltage());
            json.put("temperature", record.getTemperature());
            json.put("pressure", record.getPressure());
        } else {
            json.put("flag", "300");
        }

        renderJson(json);
    }


}

