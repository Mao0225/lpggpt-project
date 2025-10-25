package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Drivercar;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * BlogService
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class DrivercarService {

    /**
     * 所有的 dao 对象也放在 Service 中，并且声明为 private，避免 sql 满天飞
     * sql 只放在业务层，或者放在外部 sql 模板，用模板引擎管理：
     * 			https://jfinal.com/doc/5-13
     */
    private Drivercar dao = new Drivercar().dao();

    public Page<Drivercar> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from drivercar order by id asc");
    }

    public Page<Drivercar> search(int pageNumber, int pageSize, String drivername, String driverphone, String drivercarno, String carname, String carshengchanriqi) {
        // 1. 重构 SQL 结构：将 WHERE 1=1 移到条件拼接逻辑中，避免末尾多余空格
        String baseSql = "FROM drivercar " +
                "LEFT JOIN ( " +
                "   SELECT t1.drivercarid, t1.deviceid " +
                "   FROM uploaddianzibiaoqian t1 " +
                "   INNER JOIN ( " +
                "       SELECT drivercarid, MIN(id) AS min_id " +
                "       FROM uploaddianzibiaoqian " +
                "       GROUP BY drivercarid " +
                "   ) t2 ON t1.drivercarid = t2.drivercarid AND t1.id = t2.min_id " +
                ") AS sub ON drivercar.id = sub.drivercarid ";

        // 2. 查询字段 SQL（保持不变）
        String selectSql = "SELECT drivercar.*, sub.deviceid AS did ";

        // 3. 动态条件拼接：先加 WHERE 1=1，再拼接其他条件（避免无条件时出现多余 AND）
        StringBuilder condition = new StringBuilder(" WHERE 1 = 1 ");
        if (drivername != null && !drivername.isEmpty()) {
            condition.append(" AND drivercar.drivername LIKE '%").append(escapeLike(drivername)).append("%'");
        }
        if (driverphone != null && !driverphone.isEmpty()) {
            condition.append(" AND drivercar.driverphone LIKE '%").append(escapeLike(driverphone)).append("%'");
        }
        if (drivercarno != null && !drivercarno.isEmpty()) {
            condition.append(" AND drivercar.drivercarno LIKE '%").append(escapeLike(drivercarno)).append("%'");
        }
        if (carname != null && !carname.isEmpty()) {
            condition.append(" AND drivercar.carname LIKE '%").append(escapeLike(carname)).append("%'");
        }
        if (carshengchanriqi != null && !carshengchanriqi.isEmpty()) {
            condition.append(" AND drivercar.carshengchanriqi LIKE '%").append(escapeLike(carshengchanriqi)).append("%'");
        }

        // 4. 拼接最终完整 SQL（用于调试，确认无语法错误）
        String finalSql = selectSql + baseSql + condition.toString().trim(); // trim() 去除首尾空格
        System.out.println("Final SQL: " + finalSql);

        // 5. 关键修复：JFinal paginate 方法的第 3/4 个参数需拼接为完整的 FROM + WHERE 语句（无多余空格）
        String fromWhereSql = baseSql + condition.toString().trim();
        return dao.paginate(pageNumber, pageSize, selectSql, fromWhereSql);
    }

    /**
     * 辅助方法：转义 LIKE 特殊字符（%、_），避免语法错误和查询异常
     */
    private String escapeLike(String str) {
        if (str == null) return "";
        return str.replace("%", "\\%").replace("_", "\\_");
    }


    public Drivercar findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}
