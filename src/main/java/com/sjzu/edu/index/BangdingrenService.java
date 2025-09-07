package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Bangdingren;

import java.util.ArrayList;
import java.util.List;


public class BangdingrenService {


    private final Bangdingren dao = new Bangdingren().dao();

    public Page<Bangdingren> paginate(int pageNumber, int pageSize) {
        System.out.println(dao.paginate(pageNumber, pageSize, "select *", "from bangdingren order by id asc"));

        return dao.paginate(pageNumber, pageSize, "select *", "from bangdingren order by id asc");
    }

    public Bangdingren findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }

    public Page<Bangdingren> searchdata(int pageNumber, int pageSize, String name, String telephone) {
        // 构建 SQL 查询语句的基础部分
        StringBuilder sqlBuilder = new StringBuilder("FROM bangdingren WHERE 1=1");

        // 如果传入了姓名参数，则添加姓名作为搜索条件
        if (name != null && !name.isEmpty()) {
            sqlBuilder.append(" AND name LIKE '%").append(name).append("%'");
        }

        // 如果传入了电话参数，则添加电话作为搜索条件
        if (telephone != null && !telephone.isEmpty()) {
            sqlBuilder.append(" AND telphone LIKE '%").append(telephone).append("%'");
        }

        // 添加排序条件
        sqlBuilder.append(" ORDER BY id ASC");

        // 使用构建好的查询语句进行分页查询
        return (Page<Bangdingren>) dao.paginate(pageNumber, pageSize, "SELECT * ", sqlBuilder.toString());
    }
}