package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Custromer;

public class CustomerService {
    /**
     * 所有的 dao 对象也放在 Service 中，并且声明为 private，避免 sql 满天飞
     * sql 只放在业务层，或者放在外部 sql 模板，用模板引擎管理：
     * 			https://jfinal.com/doc/5-13
     */
    private Custromer dao = new Custromer().dao();

    public Page<Custromer> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from custromer order by id asc");
    }
    public Page<Custromer> search(int pageNumber, int pageSize,String station,String saddress,String customer,String caddress,String telephone,Integer stationid){
        String sql = "from custromer WHERE stationid = "+ stationid +" AND ";
        String sql2 = "select * ";
// 拼接条件
        if (station != null && !station.isEmpty()) {
            sql += "station LIKE '%" + station + "%' AND ";
        }
        if (saddress != null && !saddress.isEmpty()) {
            sql += "saddress LIKE '%" + saddress + "%' AND ";
        }
        if (customer != null && !customer.isEmpty()) {
            sql += "customer LIKE '%" + customer + "%' AND ";
        }
        if (caddress != null && !caddress.isEmpty()) {
            sql += "caddress LIKE '%" + caddress + "%' AND ";
        }
        if (telephone != null && !telephone.isEmpty()) {
            sql += "telephone LIKE '%" + telephone + "%' AND ";
        }


// 去掉最后的 AND
        System.out.println("------------------------------"+sql);
        sql = sql.substring(0, sql.length() - 4);
        System.out.println("finalsql:"+sql2+sql);
        String finalsql = sql2 + sql;
        System.out.println(dao.find(finalsql));
        System.out.println(dao.paginate(pageNumber, pageSize, sql2, sql));
        return dao.paginate(pageNumber, pageSize, sql2, sql);
    }
    public Page<Custromer> supersearch(int pageNumber, int pageSize,String station,String saddress,String customer,String caddress,String telephone,Integer stationid){
        String sql = "from custromer WHERE 1 = 1 AND ";
        String sql2 = "select * ";
// 拼接条件
        if (station != null && !station.isEmpty()) {
            sql += "station LIKE '%" + station + "%' AND ";
        }
        if (saddress != null && !saddress.isEmpty()) {
            sql += "saddress LIKE '%" + saddress + "%' AND ";
        }
        if (customer != null && !customer.isEmpty()) {
            sql += "customer LIKE '%" + customer + "%' AND ";
        }
        if (caddress != null && !caddress.isEmpty()) {
            sql += "caddress LIKE '%" + caddress + "%' AND ";
        }
        if (telephone != null && !telephone.isEmpty()) {
            sql += "telephone LIKE '%" + telephone + "%' AND ";
        }


// 去掉最后的 AND
        System.out.println("------------------------------"+sql);
        sql = sql.substring(0, sql.length() - 4);
        System.out.println("finalsql:"+sql2+sql);
        String finalsql = sql2 + sql;
        System.out.println(dao.find(finalsql));
        System.out.println(dao.paginate(pageNumber, pageSize, sql2, sql));
        return dao.paginate(pageNumber, pageSize, sql2, sql);
    }

    public Custromer findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}
