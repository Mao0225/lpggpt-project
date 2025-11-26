package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasFile;

public class GGasFileService {

    private GasFile dao = new GasFile().dao();


    public void delete(int id) {
        dao.deleteById(id);
    }

    public GasFile findById(Integer id) {
        return dao.findById(id);
    }

    public Page<GasFile> paginate(int pageNumber, int pageSize) {
        String sql = "FROM gas_file ORDER BY id ASC";
        return dao.paginate(pageNumber, pageSize, "SELECT *", sql);
    }

    public Page<GasFile> search(int pageNumber, int pageSize, String searchKey, String valve_body_code, String filling_specification, int stationid) {
        StringBuilder addsql = new StringBuilder("WHERE 1=1"); // 初始化为 true 条件，方便拼接多个条件
        System.out.println("stationid:"+stationid);
        // 添加 stationid 条件
        addsql.append(" AND stationid = ").append(stationid);

        if (searchKey != null && !searchKey.isEmpty()) {
            addsql.append(" AND gas_number LIKE '%").append(searchKey).append("%'");
        }
        if (valve_body_code != null && !valve_body_code.isEmpty()) {
            addsql.append(" AND fengtiangasno LIKE '%").append(valve_body_code).append("%'");
        }
        if (filling_specification != null && !filling_specification.isEmpty()) {
            addsql.append(" AND filling_specification LIKE '%").append(filling_specification).append("%'");
        }

        String sql = "FROM gas_file " + addsql.toString() + " ORDER BY id DESC";

        return dao.paginate(pageNumber, pageSize, "SELECT *", sql);
    }

    public boolean isGasNumberDuplicate(String gasNumber, int stationid) {
        // 1. 空值/无效值校验：气瓶编号为空 或 加气站ID无效（<=0），直接返回true（前端需配合提示）
        if (gasNumber == null || gasNumber.trim().isEmpty()) {
            System.out.println("校验失败：气瓶编号为空");
            return true;
        }
        if (stationid <= 0) {
            System.out.println("校验失败：加气站ID无效，stationid=" + stationid);
            return true;
        }

        // 2. 防SQL注入：参数化查询，增加异常处理
        String sql = "SELECT COUNT(*) FROM gas_file WHERE gas_number = ? AND stationid = ?";
        Long count = 0L;
        try {
            count = Db.queryLong(sql, gasNumber.trim(), stationid);
            // 3. 完善日志：包含气瓶编号+加气站ID，便于排查
            System.out.printf("校验气瓶编号[%s]（加气站ID：%d）重复度：数据库中存在%d条记录%n",
                    gasNumber.trim(), stationid, count);
        } catch (Exception e) {
            // 捕获SQL执行异常，打印日志并返回"重复"（避免程序崩溃，前端可重试）
            System.err.printf("校验气瓶编号[%s]（加气站ID：%d）时出错：%s%n",
                    gasNumber.trim(), stationid, e.getMessage());
            e.printStackTrace();
            return true; // 异常时视为重复，前端提示"校验失败，请重试"
        }

        return count > 0;
    }
}
