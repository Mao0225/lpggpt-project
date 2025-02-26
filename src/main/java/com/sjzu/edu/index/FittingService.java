package com.sjzu.edu.index;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.BseFittOperate;
import com.sjzu.edu.common.model.Custromer;
import com.sjzu.edu.common.model.BseFitting;
public class  FittingService {
    private BseFitting dao = new BseFitting().dao();
    private BseFittOperate daos = new BseFittOperate().dao();

    public Page<BseFitting> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from bse_fitting order by id asc");
    }
    public Page<BseFitting> search(int pageNumber, int pageSize,String name,String kind,Integer stationid){
        String sql = "from bse_fitting WHERE companyid = "+ stationid +" AND ";
        String sql2 = "select * ";
// 拼接条件
        if (name != null && !name.isEmpty()) {
            sql += "name LIKE '%" + name + "%' AND ";
        }
        if (kind != null && !kind.isEmpty()) {
            sql += "kind LIKE '%" + kind + "%' AND ";
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
    public Page<BseFittOperate> osearch(int pageNumber, int pageSize, String name, String starttime, String endtime, Integer stationid) {
        String sql = "from bse_fitt_operate WHERE stationid = " + stationid + " AND ";
        String sql2 = "select * ";

        // 拼接条件：根据 name 和 saddress 进行匹配
        if (name != null && !name.isEmpty()) {
            sql += "name LIKE '%" + name + "%' AND ";
        }

        // 处理时间查询
        if (starttime != null && !starttime.isEmpty()) {
            // 开始时间当天的第一秒
            String startDateTime = starttime + " 00:00:00";
            sql += "creattime >= '" + startDateTime + "' AND ";
        }
        if (endtime != null && !endtime.isEmpty()) {
            // 结束时间当天的最后一秒
            String endDateTime = endtime + " 23:59:59";
            sql += "creattime <= '" + endDateTime + "' AND ";
        }

        // 去掉最后的 AND
        System.out.println("------------------------------" + sql);
        sql = sql.substring(0, sql.length() - 4);
        System.out.println("finalsql:" + sql2 + sql);

        String finalsql = sql2 + sql;
        System.out.println(daos.find(finalsql));
        System.out.println(daos.paginate(pageNumber, pageSize, sql2, sql));

        return daos.paginate(pageNumber, pageSize, sql2, sql);
    }


    public BseFitting findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}
