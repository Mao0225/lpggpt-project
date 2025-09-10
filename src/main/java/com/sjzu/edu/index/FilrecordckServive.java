package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Bangdingren;
import com.sjzu.edu.common.model.FillRecordCheck1;
import com.sjzu.edu.common.model.GasStation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FilrecordckServive {
    private final GasStation dao = new GasStation().dao();
    private final FillRecordCheck1 fdao = new FillRecordCheck1().dao();

    // 分页查询充装记录
    public Page<FillRecordCheck1> search(int pageNumber, int pageSize,
                                         Timestamp finditem, String gastion, String gasname) {

        StringBuilder baseSql = new StringBuilder(
                "FROM fill_record_check1 f LEFT JOIN gas_file g ON g.gas_number = f.gas_number WHERE 1=1 ");
        String selectSql = "SELECT f.*, g.* ";
        List<Object> params = new ArrayList<>();

        if (finditem != null) {
            baseSql.append(" AND f.fill_time >= ? AND f.fill_time < ? ");
            params.add(finditem);
            params.add(new Timestamp(finditem.getTime() + 86400000)); // +1天
        }

        if (gastion != null && !gastion.isEmpty()) {
            GasStation station =dao.findFirst(
                    "SELECT * FROM gas_station WHERE companyid = ?", gastion);
            if (station != null) {
                baseSql.append(" AND f.gasstation = ? ");
                params.add(station.getInt("station_id"));
            }
        }

        if (gasname != null && !gasname.isEmpty()) {
            baseSql.append(" AND f.gas_number LIKE ? ");
            params.add("%" + gasname + "%");
        }

        baseSql.append(" ORDER BY f.id DESC");

        return fdao.paginate(pageNumber, pageSize, selectSql, baseSql.toString(), params.toArray());
    }

    // 查询所有加气站
    public List<GasStation> pagdetail() {
        List<GasStation> list = dao.find("SELECT * FROM gas_station");
        return list != null ? list : new ArrayList<>();
    }

    public FillRecordCheck1 findById(Integer id) {
        return fdao.findById(id);
    }

    public void deleteById(Integer id) {
        fdao.deleteById(id);
    }
}
