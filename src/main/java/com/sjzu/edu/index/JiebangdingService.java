package com.sjzu.edu.index;


import java.util.List;
import java.util.ArrayList;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Dianzicheng;
import com.sjzu.edu.common.model.Jiebangding;

public class JiebangdingService {

    /**  此操作对应的表：drivergpsinfo
     *
     */
    private Jiebangding dao = new Jiebangding().dao();

//    public Page<Jiebangding> paginate(int pageNumber, int pageSize) {
//        return dao.paginate(pageNumber, pageSize, "select *", "from jiebangding order by id asc");
//    }
//
//    public Jiebangding findById(int id) {
//        return dao.findById(id);
//    }


//
//
//
//    public Page<Jiebangding> paginate(int pageNumber, int pageSize, String searchKey, String unbinder) {
//        if (unbinder != null && !unbinder.trim().isEmpty() && searchKey != null && !searchKey.trim().isEmpty()) {
//            return dao.paginate(pageNumber, pageSize, "select *", "from jiebangding where jiebangren like ? and qipingbianma like ? order by id asc", "%" + unbinder + "%", "%" + searchKey + "%");
//        } else if (searchKey != null && !searchKey.trim().isEmpty()) {
//            return dao.paginate(pageNumber, pageSize, "select *", "from jiebangding where qipingbianma like ? order by id asc", "%" + searchKey + "%");
//        } else if (unbinder != null && !unbinder.trim().isEmpty()) {
//            return dao.paginate(pageNumber, pageSize, "select *", "from jiebangding where jiebangren like ? order by id asc", "%" + unbinder + "%");
//        } else {
//            return dao.paginate(pageNumber, pageSize, "select *", "from jiebangding order by id asc");
//        }
//    }
//

    public Page<Jiebangding> paginate(int pageNumber, int pageSize) {
        return  dao.paginate(pageNumber,pageSize,"select *", "from jiebangding order by id asc");
    }


    public Page<Jiebangding> searchJiebangding(int pageNumber, int pageSize, String searchKey, String unbinder, String chengbianma, String startDate, String endDate) {
        String sql = "FROM jiebangding WHERE ";
        String sql2 = "SELECT * ";
        int cout  = 0;
        // 拼接条件
        if (searchKey != null && !searchKey.isEmpty()) {
            sql += "qipingbianma LIKE '%" + searchKey + "%' AND ";
            cout++;
        }
        if (unbinder != null && !unbinder.isEmpty()) {
            sql += "jiebangren LIKE '%" + unbinder + "%' AND ";
            cout++;
        }
        if (chengbianma != null && !chengbianma.isEmpty()) {
            sql += "chengbianma LIKE '%" + chengbianma + "%' AND ";
            cout++;
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql += "bangdingshijian >= '" + startDate + "' AND "; // 注意日期范围的判断方向
            cout++;
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += "jiebangdingshijian <= '" + endDate + "' AND "; // 注意日期范围的判断方向
            cout++;
        }

        // 去掉最后的 AND
        if(cout==0){
            sql = sql.substring(0, sql.length()-7);
        }else{
            sql = sql.substring(0, sql.length()-4);
        }

        System.out.println("finalsql:" + sql2 + sql);
        String finalsql = sql2 + sql;
        System.out.println(dao.find(finalsql));
        System.out.println(dao.paginate(pageNumber, pageSize, sql2, sql));

        return dao.paginate(pageNumber, pageSize, sql2, sql);
    }




//       return dao.paginate(pageNumber, pageSize, "select *", sql.toString(), parameters.toArray());
//    }
//



}
