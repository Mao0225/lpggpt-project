package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Plckongzhi;

public class PlckongzhiService {

    private Plckongzhi dao = new Plckongzhi().dao();

    /**
     * 分页查询，支持plcno筛选
     */
    public Page<Record> paginate(int pageNumber, int pageSize, String plcno) {
        // 构建查询字段和关联逻辑
        String select = "SELECT pk.plcno, pk.*, px.qieduanfa1, px.qieduanfa2, px.fengji ";
        String from = "FROM plckongzhi pk " +
                "LEFT JOIN ( " +
                "    SELECT plcno, MAX(id) AS max_id " +
                "    FROM plcxinhao " +
                "    GROUP BY plcno " +
                ") mx ON pk.plcno = mx.plcno " +
                "LEFT JOIN plcxinhao px ON mx.max_id = px.id ";

        // 添加查询条件
        if (com.jfinal.kit.StrKit.notBlank(plcno)) {
            from += "WHERE pk.plcno LIKE '%" + plcno + "%' ";
        }

        // 按id倒序排序
        from += "ORDER BY pk.id DESC";

        // 使用Db.paginate查询，返回Record类型（因涉及多表字段）
        return Db.paginate(pageNumber, pageSize, select, from);
    }

    /**
     * 根据ID查询
     */
    public Plckongzhi findById(Integer id) {
        return dao.findById(id);
    }

    /**
     * 根据ID删除
     */
    public boolean deleteById(Integer id) {
        return dao.deleteById(id);
    }
}