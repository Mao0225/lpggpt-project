package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Jieping;
import com.sjzu.edu.common.model.Restaurant;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * BlogService
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class JiepingService {

    /**
     * 所有的 dao 对象也放在 Service 中，并且声明为 private，避免 sql 满天飞
     * sql 只放在业务层，或者放在外部 sql 模板，用模板引擎管理：
     * 			https://jfinal.com/doc/5-13
     */
    private Jieping dao = new Jieping().dao();

    public Page<Jieping> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "from jieping order by id desc");
    }

    /**
     * 支持检索的分页查询
     */
    public Page<Jieping> paginate(int pageNumber, int pageSize, String shexiangtouno, String date, String time) {
        StringBuilder whereSql = new StringBuilder("from jieping where 1=1");
        String orderBy = "order by happendtime desc";

        // 构建查询条件
        if (shexiangtouno != null && !shexiangtouno.trim().isEmpty()) {
            whereSql.append(" and shexiangtouno like '%").append(shexiangtouno.trim()).append("%'");
        }

        if (date != null && !date.trim().isEmpty()) {
            whereSql.append(" and DATE(happendtime) = '").append(date).append("'");
            orderBy = "order by happendtime asc";

            if (time != null && !time.trim().isEmpty()) {
                whereSql.append(" and TIME(happendtime) >= '").append(time).append("'");
                orderBy = "order by happendtime asc";
            }
        } else if (time != null && !time.trim().isEmpty()) {
            whereSql.append(" and TIME(happendtime) >= '").append(time).append("'");
            orderBy = "order by happendtime asc";
        }

        whereSql.append(" ").append(orderBy);

        return dao.paginate(pageNumber, pageSize, "select *", whereSql.toString());
    }
}