package com.sjzu.edu.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;

import com.sjzu.edu.common.model.Basshexiangtouinfo;

import java.util.List;
import java.util.HashSet;
import java.util.Set;


/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * IndexController
 */
@Path(value = "/", viewPath = "/appshexiangtoumessage")
@Clear
public class AppshexiangtoumessageController extends Controller {
    private Basshexiangtouinfo dao = new Basshexiangtouinfo().dao();

    public void basshexiangtouinfo() {
        String restaurantphone = getPara("restaurantphone");
        System.out.println("hello jason");
        System.out.println("restaurantphone："+restaurantphone);
        // 查询数据库中是否存在匹配的用户记录
        List<Basshexiangtouinfo> basshexiangtouinfo = dao.find("SELECT * FROM basshexiangtouinfo WHERE restaurantphone = ?", restaurantphone);
        System.out.println("摄像头数量："+basshexiangtouinfo.size());
        JSONObject json = new JSONObject();
        if (!basshexiangtouinfo.isEmpty()) {
            json.put("flag", "200");
            json.put("shexiangtoumessage", basshexiangtouinfo);

            // 创建一个HashSet来存储唯一的shexiangtoubianhao
            Set<String> uniqueShexiangtoubianhao = new HashSet<>();
            for (Basshexiangtouinfo info : basshexiangtouinfo) {
                // 检查 shexiangtoubianhao 是否为空
                if (info.getShexiangtoubianhao() != null && !info.getShexiangtoubianhao().isEmpty()) {
                    uniqueShexiangtoubianhao.add(info.getShexiangtoubianhao());
                }
            }
            // 将数量添加到返回的JSON中
            System.out.println("摄像头数量："+uniqueShexiangtoubianhao.size());
            json.put("shexiangtoubianhaoCount", uniqueShexiangtoubianhao.size());

        } else {
            json.put("flag", "300");
        }

        renderJson(json);
    }
}
