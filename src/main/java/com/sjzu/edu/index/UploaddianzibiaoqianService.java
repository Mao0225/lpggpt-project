package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Drivercar;
import com.sjzu.edu.common.model.GasBottle;
import com.sjzu.edu.common.model.Uploaddianzibiaoqian;

/** 2024-3-10 jason
 */
public class UploaddianzibiaoqianService {

    /**  此操作对应的表：gas_bottle
     *
     */

    public Page<Uploaddianzibiaoqian> search(int pageNumber, int pageSize, String drivername, String drivercarno, String biaoqian){
        String sql = "from uploaddianzibiaoqian where ";
        String sql2 = "select * ";
        // 拼接条件
        if (drivername != null && !drivername.isEmpty()) {
            sql += "drivername LIKE '%" + drivername + "%' AND ";
        }
        if (drivercarno != null && !drivercarno.isEmpty()) {
            sql += "carno LIKE '%" + drivercarno + "%' AND ";
        }
        if (biaoqian != null && !biaoqian.isEmpty()) {
            sql += "biaoqianlist LIKE '%" + biaoqian + "%' AND ";
        }

        // 去掉最后的 AND
        sql = sql.substring(0, sql.length() - 4);
        String finalsql = sql2 + sql;
        System.out.println("sql:"+sql);
        sql += " order by id desc";
                System.out.println("finalsql:"+finalsql);
        return dao.paginate(pageNumber, pageSize, sql2, sql);
    }


    private Uploaddianzibiaoqian dao = new Uploaddianzibiaoqian().dao();

    public Page<Uploaddianzibiaoqian> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from uploaddianzibiaoqian order by id desc");
    }

    public Uploaddianzibiaoqian findById(int id) {
        return dao.findById(id);
    }

}
