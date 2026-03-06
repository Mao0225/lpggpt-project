package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasDocs;

public class GasDocsService {

    private final GasDocs dao = new GasDocs().dao();

    public Page<GasDocs> search(int pageNumber, int pageSize, String inspOrg, String reportNo) {
        StringBuilder where = new StringBuilder("WHERE 1=1");

        if (inspOrg != null && !inspOrg.trim().isEmpty()) {
            where.append(" AND insp_org LIKE '%").append(inspOrg.trim()).append("%'");
        }
        if (reportNo != null && !reportNo.trim().isEmpty()) {
            where.append(" AND report_no LIKE '%").append(reportNo.trim()).append("%'");
        }

        String sql = "FROM gas_docs left join user u on gas_docs.create_userid = u.id  " + where + " ORDER BY create_time DESC, id DESC";
        return dao.paginate(pageNumber, pageSize, "SELECT gas_docs.*,u.username ", sql);
    }

    public GasDocs findById(Integer id) {
        return dao.findById(id);
    }

    public void delete(Integer id) {
        dao.deleteById(id);
    }
}
