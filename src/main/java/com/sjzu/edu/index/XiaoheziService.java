package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.sjzu.edu.common.model.ManageXiaohezi;

public class XiaoheziService {
    ManageXiaohezi dao = new ManageXiaohezi().dao();


    public Page<ManageXiaohezi> paginate(int pageNumber, int pageSize, String xiaohezino, String restaurant, String createTime) {
        // 构建 SQL 查询语句
        String addsql = "FROM manage_xiaohezi ";

        System.out.println("xiaohezino: " + xiaohezino);
        int cout = 0;

        if (xiaohezino != null && !xiaohezino.isEmpty()) {
            System.out.println("xiaohezino: " + xiaohezino);
            addsql += (cout == 0 ? " WHERE " : " AND ") + "xiaohezi_no = '" + xiaohezino + "' ";
            cout++;
        }

        if (restaurant != null && !restaurant.isEmpty()) {
            addsql += (cout == 0 ? " WHERE " : " AND ") + "restaurant LIKE '%" + restaurant + "%' ";
            cout++;
        }

        if (createTime != null && !createTime.isEmpty()) {
            addsql += (cout == 0 ? " WHERE " : " AND ") + "createtime >= '" + createTime + " 00:00:00' AND createtime < '" + createTime + " 23:59:59'";
            cout++;
        }

        addsql += " ORDER BY id DESC ";
        System.out.println("addsql: " + addsql.toString());

        // 执行分页查询
        return dao.paginate(pageNumber, pageSize, "SELECT *", addsql);
    }
    public void deleteById(Integer id) {
        dao.deleteById(id);
    }

    public Object findById(Integer id) {
        System.out.println("id: " + id);
       return dao.findById(id);
    }
}