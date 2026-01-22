package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.GasFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GasfileService {

    private GasFile dao = new GasFile().dao();


    public void delete(int id) {
        dao.deleteById(id);
    }

    public boolean isGasNumberDuplicate(String gasNumber, int stationid) {
        if (gasNumber == null || gasNumber.trim().isEmpty()) {
            System.out.println("校验失败：气瓶编号为空");
            return true;
        }
        if (stationid <= 0) {
            System.out.println("校验失败：加气站ID无效，stationid=" + stationid);
            return true;
        }

        String sql = "SELECT COUNT(*) FROM gas_file WHERE gas_number = ? AND stationid = ?";
        Long count = 0L;
        try {
            count = Db.queryLong(sql, gasNumber.trim(), stationid);
            System.out.printf("校验气瓶编号[%s]（加气站ID：%d）重复度：数据库中存在%d条记录%n",
                    gasNumber.trim(), stationid, count);
        } catch (Exception e) {
            System.err.printf("校验气瓶编号[%s]（加气站ID：%d）时出错：%s%n",
                    gasNumber.trim(), stationid, e.getMessage());
            e.printStackTrace();
            return true;
        }

        return count > 0;
    }

    public boolean saveGasFile(GasFile gasFile) {
        String gasNumber = gasFile.getGasNumber();
        int stationid = gasFile.getStationid();
        if (isGasNumberDuplicate(gasNumber, stationid)) {
            return false;
        }
        gasFile.save();
        return true;
    }

    public GasFile findById(Integer id) {
        return dao.findById(id);
    }

    public Page<GasFile> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize, "select *", "FROM gas_file ORDER BY id DESC");
    }

    // 修改：添加uploadStatus参数，用于筛选上传状态
    public Page<GasFile> search(int pageNumber, int pageSize,String filing_gas_station, String searchKey, String fentiancode, String filling_specification, String expiredStatus, String uploadStatus) {
        StringBuilder addsql = new StringBuilder("WHERE 1=1");

        if (searchKey != null && !searchKey.isEmpty()) {
            addsql.append(" AND gas_number LIKE '%").append(searchKey).append("%'");
        }
        if (fentiancode != null && !fentiancode.isEmpty()) {
            addsql.append(" AND fengtiangasno LIKE '%").append(fentiancode).append("%'");
        }
        if (filling_specification != null && !filling_specification.isEmpty()) {
            addsql.append(" AND filling_specification LIKE '%").append(filling_specification).append("%'");
        }

        if (filing_gas_station != null && !filing_gas_station.isEmpty()) {
            addsql.append(" AND filing_gas_station LIKE '%").append(filing_gas_station).append("%'");
        }

        // 处理过期状态筛选
        if ("expired".equals(expiredStatus)) {
            // 获取当前日期（格式：yyyy-MM-dd）
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());
            // 添加终止使用日期早于当前日期的条件
            addsql.append(" AND terminate_use_date < '").append(currentDate).append("'");
            System.out.println("筛选已过期记录，当前日期：" + currentDate);
        }

        // 新增：处理上传状态筛选
        if ("0".equals(uploadStatus)) {
            addsql.append(" AND upload_status = 0");
            System.out.println("筛选未上传记录");
        } else if ("1".equals(uploadStatus)) {
            addsql.append(" AND upload_status = 1");
            System.out.println("筛选已上传记录");
        }

        String sql = "FROM gas_file " + addsql.toString() + " ORDER BY id DESC ";

        return dao.paginate(pageNumber, pageSize, "SELECT *", sql);
    }


}