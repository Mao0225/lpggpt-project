package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.PressureValueRecords;
import com.sjzu.edu.common.model.User;

/** 2024-3-10 jason
 */
public class PressureValueRecordsService {

    /**  此操作对应的表：t_pressure_value_records
     *
     */
    private PressureValueRecords dao = new PressureValueRecords().dao();

    public Page<PressureValueRecords> paginate(int pageNumber, int pageSize) {
       return dao.paginate(pageNumber, pageSize, "select *", "from t_pressure_value_records order by id desc");

    }
    public Page<PressureValueRecords> search(int pageNumber, int pageSize, String controllerid,String status){
        String sql = "from t_pressure_value_records  WHERE ";
        String sql2 = "select * ";
// 拼接条件
        if (controllerid != null && !controllerid.isEmpty()) {
            sql += "controller_id LIKE '%" + controllerid + "%' AND ";
        }
        if (status != null && !status.isEmpty()) {
            sql += "valve_status LIKE '%"+ status +"%' AND ";
        }

// 去掉最后的 AND
        System.out.println("------------------------------"+sql);
        sql = sql.substring(0, sql.length() - 4);
        sql += " order by id desc";
        System.out.println("finalsql:"+sql2+sql);
        String finalsql = sql2 + sql;
//        System.out.println(dao.find(finalsql));
//        System.out.println(dao.paginate(pageNumber, pageSize, sql2, sql));
       try {
           return dao.paginate(pageNumber, pageSize, sql2, sql);
       }catch (Exception e){
           return dao.paginate(pageNumber, pageSize, sql2,"");

       }
    }
    public PressureValueRecords findById(int id) {
        return dao.findById(id);
    }

}
