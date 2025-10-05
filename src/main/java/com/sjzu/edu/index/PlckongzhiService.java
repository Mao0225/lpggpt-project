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
    public Page<Plckongzhi> paginate(int pageNumber, int pageSize, String plcno) {
        String select = "select * ";
        String from = "from plckongzhi ";

        // 构建查询条件
        if (com.jfinal.kit.StrKit.notBlank(plcno)) {
            from += "where plcno like '%" + plcno + "%' ";
        }

        // 按id倒序排序，最新的在前面
        from += "order by id desc";

        return dao.paginate(pageNumber, pageSize, select, from);
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