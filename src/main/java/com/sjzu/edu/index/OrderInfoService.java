package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.BasBill;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoService {
    BasBill dao = new BasBill().dao();
    public Page<BasBill> paginate(int pageNumber, int pageSize, String time, String station, String restaurantId) {
        // 基础SQL
        StringBuilder baseSql = new StringBuilder("FROM bas_bill");
        List<Object> params = new ArrayList<>();

        // 构建WHERE条件
        List<String> conditions = new ArrayList<>();

        if (time != null && !time.isEmpty()) {
            conditions.add("writetime BETWEEN ? AND ?");
            params.add(time + " 00:00:00");
            params.add(time + " 23:59:59");
        }

        if (station != null && !station.isEmpty()) {
            conditions.add("station_id =?");
            params.add(station);
        }

        if (restaurantId != null && !restaurantId.isEmpty()) {
            conditions.add("restaurantid = ?");
            params.add(restaurantId);
        }

        // 拼接WHERE条件
        if (!conditions.isEmpty()) {
            baseSql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        // 添加排序
        baseSql.append(" ORDER BY id DESC");

        // 使用参数化查询
        String select = "SELECT * ";
        return dao.paginate(pageNumber, pageSize, select, baseSql.toString(), params.toArray());
    }
 public BasBill findByid(int id) {
     System.out.println("service层的findByid接收到的参数: "+id);
      return dao.findById(id);
 }
 public void deleteByid(int id) {
      dao.deleteById(id);
 }
}

