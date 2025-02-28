package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasStationStaff;

public class GgasStationStaffService {
    private GasStationStaff dao = new GasStationStaff().dao();

    public Page<GasStationStaff> paginate(int pageNumber, int pageSize, String staff_name, String station_name, String stationid) {
        StringBuilder addsql = new StringBuilder("FROM gas_station right join gas_station_staff ON (gas_station.station_id = gas_station_staff.station_id) WHERE 1 = 1");

        int conditionCount = 0;

        // 处理 staff_name 参数
        if (staff_name != null && !staff_name.isEmpty()) {
            addsql.append(" AND staff_name LIKE '%").append(staff_name).append("%'");
            conditionCount++;
        }

        // 处理 station_name 参数
        if (station_name != null && !station_name.isEmpty()) {
            addsql.append(" AND station_name LIKE '%").append(station_name).append("%'");
            conditionCount++;
        }

        // 处理 stationid 参数
        if (stationid != null) {
            System.out.println(("stationid:" + stationid));
            // 为字符串类型的值添加引号
            addsql.append(" AND gas_station_staff.station_id = '").append(stationid).append("'");
            conditionCount++;
        }

        // 按需要添加排序，这里按 id 降序排列作为示例
        addsql.append(" ORDER BY gas_station_staff.id DESC");

        System.out.println(addsql);

        return dao.paginate(pageNumber, pageSize, "SELECT station_name,gas_station_staff.*", addsql.toString());
    }


    public GasStationStaff findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}