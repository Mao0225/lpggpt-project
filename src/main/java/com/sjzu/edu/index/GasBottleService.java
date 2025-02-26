package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasBottle;

import java.util.ArrayList;
import java.util.List;

public class GasBottleService {

    private GasBottle dao = new GasBottle().dao();

    // 原有的分页方法
    public Page<GasBottle> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from gas_bottle order by id desc");
    }

    // 新增的带条件分页查询方法
    public Page<GasBottle> search(int pageNumber, int pageSize, String bottleId, String transState, String carId, String startTime, String endTime, String tel) {
        String addSql = "";

        // 构建动态查询条件
        if (bottleId != null && !bottleId.isEmpty()) {
            addSql += "WHERE bottle_id LIKE '%" + bottleId + "%' ";
        }
        if (transState != null && !transState.isEmpty()) {
            addSql += (addSql.isEmpty() ? "WHERE " : "AND ") + "trans_state LIKE '%" + transState + "%' ";
        }
        if (carId != null && !carId.isEmpty()) {
            addSql += (addSql.isEmpty() ? "WHERE " : "AND ") + "car_id LIKE '%" + carId + "%' ";
        }
        if (tel != null && !tel.isEmpty()) {
            addSql += (addSql.isEmpty() ? "WHERE " : "AND ") + "tel LIKE '%" + tel + "%' ";
        }
        if (startTime != null && !startTime.isEmpty()) {
            addSql += (addSql.isEmpty() ? "WHERE " : "AND ") + "start_time >= '" + startTime + " 00:00:00' ";
        }
        if (endTime != null && !endTime.isEmpty()) {
            addSql += (addSql.isEmpty() ? "WHERE " : "AND ") + "start_time <= '" + endTime + " 23:59:59' ";
        }

        String sql = "FROM gas_bottle " + addSql + "ORDER BY id DESC";

        return dao.paginate(pageNumber, pageSize, "SELECT *", sql);
    }
}