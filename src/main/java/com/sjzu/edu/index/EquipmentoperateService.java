package com.sjzu.edu.index;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Custromer;
import com.sjzu.edu.common.model.BseEquipOperation;
public class EquipmentoperateService {
    private BseEquipOperation dao = new BseEquipOperation().dao();

    public Page<BseEquipOperation> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from bse_equip_operation order by id desc");
    }
    public Page<BseEquipOperation> search(int pageNumber, int pageSize,String factory,String model,String name,String type,Integer stationid){
        String sql = "from bse_equip_operation WHERE stationid = "+ stationid +" AND ";
        String sql2 = "select * ";
// 拼接条件
        if (factory != null && !factory.isEmpty()) {
            sql += "factory LIKE '%" + factory + "%' AND ";
        }
        if (model != null && !model.isEmpty()) {
            sql += "model LIKE '%" + model + "%' AND ";
        }
        if (name != null && !name.isEmpty()) {
            sql += "name LIKE '%" + name + "%' AND ";
        }
        if (type != null && !type.isEmpty()) {
            sql += "operation_type LIKE '%" + type + "%' AND ";
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


    public BseEquipOperation findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}
