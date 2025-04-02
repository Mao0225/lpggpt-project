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
    public Page<Drivercar> search(int pageNumber, int pageSize, String drivername, String driverphone, String drivercarno, String carname, String carshengchanriqi){
        String baseSql = "FROM drivercar " +
                "LEFT JOIN ( " +
                "   SELECT *, ROW_NUMBER() OVER (PARTITION BY drivercarid ORDER BY id DESC) AS rn " +
                "   FROM uploaddianzibiaoqian " +
                ") AS sub ON drivercar.id = sub.drivercarid AND sub.rn = 1 " +
                "WHERE 1 = 1 ";

        String selectSql = "SELECT drivercar.*, sub.deviceid AS did ";

        // 动态拼接查询条件
        StringBuilder condition = new StringBuilder();

        if (drivername != null && !drivername.isEmpty()) {
            condition.append(" AND drivername LIKE '%").append(drivername).append("%'");
        }
        if (driverphone != null && !driverphone.isEmpty()) {
            condition.append(" AND driverphone LIKE '%").append(driverphone).append("%'");
        }
        if (drivercarno != null && !drivercarno.isEmpty()) {
            condition.append(" AND drivercarno LIKE '%").append(drivercarno).append("%'");
        }
        if (carname != null && !carname.isEmpty()) {
            condition.append(" AND carname LIKE '%").append(carname).append("%'");
        }
        if (carshengchanriqi != null && !carshengchanriqi.isEmpty()) {
            condition.append(" AND carshengchanriqi LIKE '%").append(carshengchanriqi).append("%'");
        }

        String finalSql = selectSql + baseSql + condition;
        System.out.println("Final SQL: " + finalSql);

        return dao.paginate(pageNumber, pageSize, selectSql, baseSql + condition);
    }


    public Drivercar findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}
