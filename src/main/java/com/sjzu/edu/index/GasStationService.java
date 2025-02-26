package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasStation;

public class GasStationService {

    private GasStation dao = new GasStation().dao();

    public Page<GasStation> paginate(int pageNumber, int pageSize,String searchKey) {
        String addsql = "";
        if(searchKey!=null&&searchKey.length()!=0){
            addsql = "WHERE station_name like'%" + searchKey +"%'";
        }
        String sql = "from gas_station "+ addsql +"order by id asc";
        return dao.paginate(pageNumber, pageSize, "select *", sql);
    }

    public GasStation findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}
