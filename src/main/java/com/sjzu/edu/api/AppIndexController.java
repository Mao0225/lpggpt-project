package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.sjzu.edu.common.model.Dianzicheng;
import com.sjzu.edu.common.model.IotSyncRdsRecordsV2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/appindex")
@Clear
public class AppIndexController extends Controller {
    private Dianzicheng dao = new Dianzicheng().dao();

    public void index() {
        String shiyongfangphoneid = getPara("shiyongfangphoneid");

        // 查询数据库中所有符合条件的记录
        List<Dianzicheng> dianzichengList = dao.find("SELECT bianma,yuliu1 FROM dianzicheng WHERE shiyongfangphoneid = ?", shiyongfangphoneid);

        JSONObject json = new JSONObject();
        // 创建Map容器用于记录yuliu1的出现情况
        Map<String, Integer> yuliu1Count = new HashMap<>();
        int totalCount = 0; // 用于记录yuliu1总的出现次数

        if (!dianzichengList.isEmpty()) {
            json.put("flag", "200");
            JSONArray jsonArray = new JSONArray();
            for (Dianzicheng dianzicheng : dianzichengList) {
                JSONObject itemJson = new JSONObject();
                itemJson.put("index", dianzicheng);
                String yuliu1Value = dianzicheng.getYuliu1();
                itemJson.put("yuliu1", yuliu1Value);
                itemJson.put("bianma", dianzicheng.getBianma());
                jsonArray.add(itemJson);

                // 统计yuliu1出现的次数
                yuliu1Count.put(yuliu1Value, yuliu1Count.getOrDefault(yuliu1Value, 0) + 1);
                totalCount++;
            }
            json.put("items", jsonArray);

            // 将统计结果添加到返回值中
            json.put("yuliu1Count", yuliu1Count);
            json.put("totalCount", totalCount);
        } else {
            json.put("flag", "300");
        }

        renderJson(json);
    }
}
