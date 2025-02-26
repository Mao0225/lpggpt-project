package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasFile;

public class GasfileService {

    private GasFile dao = new GasFile().dao();


    public void delete(int id) {
        dao.deleteById(id);
    }

    public GasFile findById(Integer id) {
        return dao.findById(id);
    }

    public Page<GasFile> paginate(int pageNumber, int pageSize) {
//
        return dao.paginate(pageNumber, pageSize, "select *", "FROM gas_file ORDER BY id ASC");
    }
    public Page<GasFile> search(int pageNumber, int pageSize, String searchKey, String valve_body_code, String filling_specification) {
        StringBuilder addsql = new StringBuilder("WHERE 1=1"); // 初始化为 true 条件，方便拼接多个条件

        if (searchKey != null && !searchKey.isEmpty()) {
            addsql.append(" AND gas_number LIKE '%").append(searchKey).append("%'");
        }
        if (valve_body_code != null && !valve_body_code.isEmpty()) {
            addsql.append(" AND valve_body_code LIKE '%").append(valve_body_code).append("%'");
        }
        if (filling_specification != null && !filling_specification.isEmpty()) {
            addsql.append(" AND filling_specification LIKE '%").append(filling_specification).append("%'");
        }

        String sql = "FROM gas_file " + addsql.toString() + " ORDER BY id DESC";

        return dao.paginate(pageNumber, pageSize, "SELECT *", sql);
    }


}
