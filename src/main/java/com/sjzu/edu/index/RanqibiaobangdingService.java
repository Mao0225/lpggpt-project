package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Model;
import com.sjzu.edu.common.model.Ranqibiaobangding;

/**
 * RanqibiaobangdingService
 */
public class RanqibiaobangdingService {

    // dao 对象
    private Ranqibiaobangding dao = new Ranqibiaobangding().dao();

    public Page<Ranqibiaobangding> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "SELECT *", "FROM ranqibiaobangding ORDER BY id ASC");
    }

    public Page<Ranqibiaobangding> search(int pageNumber, int pageSize, String ranqibiaono,
                                          String qipingno, String saomiaoshijian, String saomiaoren,
                                          String biaodushu, String biaodushuriqi, String jingdu,
                                          String weidu, String address) {
        StringBuilder sql = new StringBuilder("FROM ranqibiaobangding WHERE 1=1 "); // 使用 1=1 方便后续拼接
        String sqlSelect = "SELECT * ";

        // 拼接条件
        if (ranqibiaono != null && !ranqibiaono.isEmpty()) {
            sql.append("AND ranqibiaono LIKE '%").append(ranqibiaono).append("%' ");
        }
        if (qipingno != null && !qipingno.isEmpty()) {
            sql.append("AND qipingno LIKE '%").append(qipingno).append("%' ");
        }
        if (saomiaoshijian != null && !saomiaoshijian.isEmpty()) {
            sql.append("AND saomiaoshijian LIKE '%").append(saomiaoshijian).append("%' ");
        }
        if (saomiaoren != null && !saomiaoren.isEmpty()) {
            sql.append("AND saomiaoren LIKE '%").append(saomiaoren).append("%' ");
        }
        if (biaodushu != null && !biaodushu.isEmpty()) {
            sql.append("AND biaodushu LIKE '%").append(biaodushu).append("%' ");
        }
        if (biaodushuriqi != null && !biaodushuriqi.isEmpty()) {
            sql.append("AND biaodushuriqi LIKE '%").append(biaodushuriqi).append("%' ");
        }
        if (jingdu != null && !jingdu.isEmpty()) {
            sql.append("AND jingdu LIKE '%").append(jingdu).append("%' ");
        }
        if (weidu != null && !weidu.isEmpty()) {
            sql.append("AND weidu LIKE '%").append(weidu).append("%' ");
        }
        if (address != null && !address.isEmpty()) {
            sql.append("AND address LIKE '%").append(address).append("%' ");
        }

        // 去掉最后的 AND，使用前面 1=1 避免出现空条件
        String finalSql = sql.toString();
        System.out.println("Final SQL: " + finalSql);

        // 执行分页查询
        return dao.paginate(pageNumber, pageSize, sqlSelect, finalSql);
    }

    public Ranqibiaobangding findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
}