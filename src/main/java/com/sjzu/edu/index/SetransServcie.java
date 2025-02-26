package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Transport;

public class SetransServcie {
    Transport dao = new Transport().dao();
    public Page<Transport> paginate(int pageNumber, int pageSize, String name, String upTime, String downTime) {
        // 构建 SQL 查询语句，添加 downcar 表的连接
        String addsql = "FROM transport t " +
                "LEFT JOIN gas_station gs ON (t.stationid = gs.id) " +
                "LEFT JOIN downcar dc ON (t.bottleid = dc.bottleid) ";

        System.out.println("name: " + name);
        int cout = 0;

        if (name != null && !name.isEmpty()) {
            System.out.println("name: " + name);
            addsql += (cout == 0 ? " WHERE " : " AND ") + "gs.station_name LIKE '%" + name + "%' ";
            cout++;
        }

        if (upTime != null && !upTime.isEmpty()) {
            addsql += (cout == 0 ? " WHERE " : " AND ") + "t.uptime >= '" + upTime + " 00:00:00' AND t.uptime < '" + upTime + " 23:59:59'";
            cout++;
        }

        if (downTime != null && !downTime.isEmpty()) {
            addsql += (cout == 0 ? " WHERE " : " AND ") + "dc.downtime >= '" + downTime + " 00:00:00' AND dc.downtime < '" + downTime + " 23:59:59'";
            cout++;
        }

        addsql += " ORDER BY t.id DESC ";
        System.out.println("addsql: " + addsql.toString());

        // 执行分页查询
        return dao.paginate(pageNumber, pageSize, "SELECT t.*, dc.*,t.customer as tcustomer,dc.customer as dcustomer,t.address as taddress, dc.address as daddress,t.stationname as tstationname,dc.stationname as dstationname", addsql);
    }
    public void deleteById(Integer id) {
        dao.deleteById(id);
    }

}
