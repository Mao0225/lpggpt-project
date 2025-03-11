package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.BseXiaohezi;

public class InstallService {
    BseXiaohezi dao = new BseXiaohezi().dao();

    public Page<BseXiaohezi> paginate(int pageNumber, int pageSize, String xiaohezi, String time) {
        // 构建 SQL 查询语句
        String addsql = "FROM bse_xiaohezi bx ";

        System.out.println("xiaohezi: " + xiaohezi);
        System.out.println("time: " + time);
        int cout = 0;

        if (xiaohezi != null && !xiaohezi.isEmpty()) {
            System.out.println("xiaohezi: " + xiaohezi);
            addsql += (cout == 0 ? " WHERE " : " AND ") + "bx.xiaohezi_number LIKE '%" + xiaohezi + "%' ";
            cout++;
        }

        if (time != null && !time.isEmpty()) {
            addsql += (cout == 0 ? " WHERE " : " AND ") + "bx.creattime >= '" + time + " 00:00:00' AND bx.creattime < '" + time + " 23:59:59'";
            cout++;
        }

        addsql += " ORDER BY bx.id DESC ";
        System.out.println("addsql: " + addsql.toString());

        // 执行分页查询
        return dao.paginate(pageNumber, pageSize, "SELECT bx.*", addsql);
    }


    public BseXiaohezi findById(Integer id){
        return dao.findById(id);
    }



    public void deleteById(Integer id) {
        dao.deleteById(id);
    }

}