package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseEquipment;

public class ManageEquipmentService {
    private BseEquipment dao = new BseEquipment().dao();

    // 原有方法：查询全部设备（返回Page<BseEquipment>）
    public Page<BseEquipment> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from bse_equipment order by id DESC");
    }

    // 新增方法：按公司ID查询设备（返回Page<BseEquipment>，和原有paginate返回类型一致）
    public Page<BseEquipment> searchByCompanyId(int pageNumber, int pageSize, Integer companyId) {
        StringBuilder sqlBuilder = new StringBuilder("from bse_equipment");
        // 只有companyId不为null且不等于-1时，才添加筛选条件
        if (companyId != null && companyId != -1) {
            sqlBuilder.append(" WHERE companyid = ?");
            // 使用参数化查询，避免SQL注入
            return dao.paginate(pageNumber, pageSize, "select *", sqlBuilder.toString(), companyId);
        } else {
            // 查全部，复用原有逻辑
            return paginate(pageNumber, pageSize);
        }
    }

    // 保留原有search方法（如果其他地方用到），但mequiplist不再使用
    public Page<Record> search(int pageNumber, int pageSize, Integer id) {
        String selectFields = "select *";
        StringBuilder sqlBuilder = new StringBuilder("from bse_equipment");

        if (id != null && id != -1) {
            sqlBuilder.append(" WHERE companyid = ").append(id);
        }
        sqlBuilder.append(" ORDER BY id DESC");
        String sql = sqlBuilder.toString();
        System.out.println("最终执行的SQL：" + sql);
        return Db.paginate(pageNumber, pageSize, selectFields, sql);
    }

    public Record findByEquipmentIdAndCompanyId(int equipmentId) {
        String sql = "SELECT * FROM bse_equipment WHERE id = ?";
        System.out.println("查询维修数据SQL：" + sql + " 参数：" + equipmentId);
        return Db.findFirst(sql, equipmentId);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }

    public BseEquipment findById(int id) {
        return dao.findById(id);
    }
}