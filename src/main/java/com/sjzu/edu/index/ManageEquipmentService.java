package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseEquipment;
import com.sjzu.edu.common.model.Restaurant;
import com.sjzu.edu.common.model.User;

import javax.servlet.http.HttpSession;

public class ManageEquipmentService {
    private BseEquipment dao = new BseEquipment().dao();
    public Page<BseEquipment> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from bse_equipment order by id DESC");
    }

    public Page<Record> search(int pageNumber, int pageSize, Integer id) {  // 参数id改为包装类型Integer，方便处理可能为null的情况
        String selectFields = "select *";
        StringBuilder sqlBuilder = new StringBuilder("from bse_equipment");

        // 构建动态查询条件，添加根据companyid与传入id匹配的筛选条件
        if (id!= null) {
            sqlBuilder.append(" WHERE companyid = ").append(id);  // 精确匹配companyid与传入的id相等
            // 如果需要模糊匹配，可以使用如下方式（根据实际需求选择）
            // sqlBuilder.append(" WHERE companyid LIKE '%").append(id).append("%'");
        }

        sqlBuilder.append(" ORDER BY id DESC");
        String sql = sqlBuilder.toString();
        System.out.println(sql);
        return Db.paginate(pageNumber, pageSize, selectFields, sql);
    }
    public Record findByEquipmentIdAndCompanyId(int equipmentId) {
        // 明确指定SELECT语句的字段部分
        String selectFields = "SELECT * ";
        StringBuilder sqlBuilder = new StringBuilder();
        // 先添加SELECT字段部分
        sqlBuilder.append(selectFields);
        // 再添加FROM和表名以及WHERE条件部分，使用String.format确保空格等格式正确
        sqlBuilder.append(String.format("FROM %s ", "bse_equipment"));
        sqlBuilder.append(String.format("WHERE id = %d ", equipmentId));
        String sql = sqlBuilder.toString();
        System.out.println("构建的完整SQL语句为: " + sql);
        // 使用Db.findFirst尝试获取符合条件的第一条（也是唯一一条，根据你的业务逻辑应该是对应唯一数据行）记录
        return Db.findFirst(sql);
    }
    public void deleteById(int id) {
        dao.deleteById(id);
    }
    public BseEquipment findById(int id) {
        return dao.findById(id);
    }
}
