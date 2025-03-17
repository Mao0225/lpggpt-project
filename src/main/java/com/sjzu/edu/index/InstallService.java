package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseXiaohezi;

import java.util.ArrayList;
import java.util.List;

public class InstallService {
    BseXiaohezi dao = new BseXiaohezi().dao();

    public Page<Record> paginate(int pageNumber, int pageSize, String xiaohezi, String time, String name) {
        // 构建 SELECT 字段
        String select = "SELECT x.*, r.name AS restaurant_name, r.address AS restaurant_address, "
                + "d.door_video, d.qiguan_video, d.daoguan_video, d.louguan_video ";

        // 构建基础 SQL
        StringBuilder fromSql = new StringBuilder()
                .append("FROM bse_xiaohezi x ")
                .append("INNER JOIN bse_data d ON x.uuid = d.uuid ")
                .append("INNER JOIN restaurant r ON x.xiaohezi_number = r.xiaohezi ");

        List<Object> params = new ArrayList<>();
        int conditionCount = 0;

        // 处理查询条件
        if (xiaohezi != null && !xiaohezi.isEmpty()) {
            fromSql.append(conditionCount++ == 0 ? " WHERE " : " AND ")
                    .append("x.xiaohezi_number LIKE ? ");
            params.add("%" + xiaohezi + "%");
        }

        if (time != null && !time.isEmpty()) {
            fromSql.append(conditionCount++ == 0 ? " WHERE " : " AND ")
                    .append("x.creattime >= ? AND x.creattime <= ? ");
            params.add(time + " 00:00:00");
            params.add(time + " 23:59:59");
        }
        if (name != null && !name.isEmpty()) {
            fromSql.append(conditionCount++ == 0 ? " WHERE " : " AND ")
                    .append("r.name LIKE ? ");
            params.add("%" + name + "%");
        }

        // 排序
        fromSql.append(" ORDER BY x.id DESC ");

        // 执行分页查询
        return Db.paginate(
                pageNumber,
                pageSize,
                select,
                fromSql.toString(),
                params.toArray()
        );
    }

    // 其他方法保持原样
    public BseXiaohezi findById(Integer id){
        return dao.findById(id);
    }

    public void deleteById(Integer id) {
        dao.deleteById(id);
    }
}