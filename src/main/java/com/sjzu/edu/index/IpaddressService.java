package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Ipaddress;

public class IpaddressService {

    private Ipaddress dao = new Ipaddress().dao();

    public Page<Ipaddress> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from ipaddress order by id asc");
    }

    public Page<Ipaddress> search(int pageNumber, int pageSize, String deviceno, String poweronoff,
                                  String ipaddress, String happendtime) {
        String sql = "from ipaddress WHERE ";
        String sql2 = "select * ";

        if (deviceno != null && !deviceno.isEmpty()) {
            sql += "deviceno LIKE '%" + deviceno + "%' AND ";
        }
        if (poweronoff != null && !poweronoff.isEmpty()) {
            sql += "poweronoff LIKE '%" + poweronoff + "%' AND ";
        }
        if (ipaddress != null && !ipaddress.isEmpty()) {
            sql += "ipaddress LIKE '%" + ipaddress + "%' AND ";
        }
        if (happendtime != null && !happendtime.isEmpty()) {
            sql += "happendtime LIKE '%" + happendtime + "%' AND ";
        }

        if (sql.endsWith("WHERE ")) {
            sql = sql.substring(0, sql.length() - 6);
        } else {
            sql = sql.substring(0, sql.length() - 4);
        }

        return dao.paginate(pageNumber, pageSize, sql2, sql);
    }

    public Ipaddress findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}