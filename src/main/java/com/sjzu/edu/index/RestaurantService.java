package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Restaurant;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * BlogService
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class RestaurantService {

    /**
     * 所有的 dao 对象也放在 Service 中，并且声明为 private，避免 sql 满天飞
     * sql 只放在业务层，或者放在外部 sql 模板，用模板引擎管理：
     * 			https://jfinal.com/doc/5-13
     */
    private Restaurant dao = new Restaurant().dao();

    public Page<Restaurant> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from restaurant order by id asc");
    }
    public Page<Restaurant> search(int pageNumber, int pageSize,String restaurantno,String name,String address,String leader,String telephone,String chengno){
        String sql = "from restaurant WHERE ";
        String sql2 = "select * ";
// 拼接条件
        if (restaurantno != null && !restaurantno.isEmpty()) {
            sql += "restaurantno LIKE '%" + restaurantno + "%' AND ";
        }
        if (name != null && !name.isEmpty()) {
            sql += "name LIKE '%" + name + "%' AND ";
        }
        if (address != null && !address.isEmpty()) {
            sql += "address LIKE '%" + address + "%' AND ";
        }
        if (leader != null && !leader.isEmpty()) {
            sql += "leader LIKE '%" + leader + "%' AND ";
        }
        if (telephone != null && !telephone.isEmpty()) {
            sql += "telephone LIKE '%" + telephone + "%' AND ";
        }
        if (chengno != null && !chengno.isEmpty()) {
            sql += "chengno LIKE '%" + chengno + "%' AND ";
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

    public Restaurant findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}
