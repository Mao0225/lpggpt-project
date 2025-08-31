package com.sjzu.edu.index;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.sjzu.edu.common.model.AppManage;
import com.sjzu.edu.common.model.Bangdingren;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppManageService {

    private final AppManage dao = new AppManage().dao();


    /**
     * 根据ID查询
     */
    public AppManage findById(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return dao.findById(id);
    }



    public Page<AppManage> searchPaginate(int pageNumber, int pageSize, String name, String version) {
        // 初始化查询参数列表
        List<Object> params = new ArrayList<>();

        // 构建主查询，获取每个 name 的最新记录
        StringBuilder fromSql = new StringBuilder(
                "FROM app_manage am1 " +
                        "WHERE am1.version = ( " +
                        "    SELECT MAX(am2.version) " +
                        "    FROM app_manage am2 " +
                        "    WHERE am2.appcode = am1.appcode " +
                        ")"
        );

        // 添加筛选条件，使用参数化查询防止 SQL 注入
        if (name != null && !name.trim().isEmpty()) {
            fromSql.append(" AND am1.name LIKE ?");
            params.add("%" + name.trim() + "%"); // 模糊匹配名称
        }
        if (version != null && !version.trim().isEmpty()) {
            fromSql.append(" AND am1.version LIKE ?");
            params.add("%" + version.trim() + "%"); // 模糊匹配版本
        }

        // 添加排序，按名称升序
        fromSql.append(" ORDER BY am1.name ASC");

        // 执行分页查询，传入参数化查询语句和参数
        return dao.paginate(pageNumber, pageSize, "SELECT am1.*", fromSql.toString(), params.toArray());
    }


    /**
     * 删除App
     */
    public boolean deleteById(Integer id) {
        if (id == null || id <= 0) {
            return false;
        }
        return dao.deleteById(id);
    }

    public void deleteByCode(String code) {
        Db.delete("DELETE FROM app_manage WHERE appcode = ?", code);
    }

}
