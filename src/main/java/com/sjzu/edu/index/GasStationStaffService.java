package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Drivercar;
import com.sjzu.edu.common.model.GasStationStaff;

public class GasStationStaffService {
    private GasStationStaff dao = new GasStationStaff().dao();

    public Page<GasStationStaff> paginate(int pageNumber, int pageSize,String staff_name,String station_name) {
        String addsql = "FROM gas_station right join gas_station_staff ON (gas_station.station_id = gas_station_staff.station_id) where ";
        int cout = 0;
        if (staff_name != null && !staff_name.isEmpty()) {
            addsql += "staff_name LIKE '%" + staff_name + "%' AND ";
            cout++;
        }
        if (station_name != null && !station_name.isEmpty()) {
            addsql += "station_name LIKE '%" + station_name + "%' AND ";
            cout++;
        }
        // 去掉最后的 AND
        if(cout==0){
            addsql = addsql.substring(0, addsql.length()-7);
        }else if(cout==1){
            addsql = addsql.substring(0, addsql.length()-4);
        }
        System.out.println(addsql);

        return dao.paginate(pageNumber, pageSize,"SELECT station_name,gas_station_staff.*",addsql);

    }

    public GasStationStaff findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}
