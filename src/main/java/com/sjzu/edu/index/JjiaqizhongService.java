package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.AutoGasFillingWaitRecord;

import java.util.ArrayList;
import java.util.List;

public class JjiaqizhongService {
    /**
     * 对 auto_gas_filling_wait_record 表进行分页查询
     * @param pageNumber 当前页码
     * @param pageSize 每页显示的记录数
     * @param companyid 公司 ID，用于与 org_code 比对筛选数据
     * @return 包含 AutoGasFillingWaitRecord 模型的分页对象
     */
    public Page<AutoGasFillingWaitRecord> paginate(int pageNumber, int pageSize, String license_plate, Integer gun_no,String companyid ) {
        // 定义查询语句的 SELECT 部分，选择所有列
        String select = "SELECT *";
        // 定义查询语句除 SELECT 部分之外的内容
        StringBuilder sqlExceptSelect = new StringBuilder("FROM auto_gas_filling_wait_record WHERE 1=1");

        // 添加 companyid 条件，为字符串类型的值添加引号
        if (companyid != null) {
            System.out.println("companyid:" + companyid);
            sqlExceptSelect.append(" AND org_code = '").append(companyid).append("'");
        }

        // 用于记录是否已经添加了 WHERE 子句的条件
        int conditionCount = 0;


        if (license_plate != null && !license_plate.isEmpty()) {
            sqlExceptSelect.append(conditionCount == 0 ? " AND " : " AND ").append("license_plate = '").append(license_plate).append("'");
            conditionCount++;
        }
        // 处理 status 参数
        if (gun_no != null) {
            sqlExceptSelect.append(conditionCount == 0 ? " AND " : " AND ").append("gun_no = '").append(gun_no).append("'");
            conditionCount++;
        }

        // 最终的 SQL 查询语句，按 id 降序排列
        sqlExceptSelect.append(" ORDER BY id DESC");

        // 打印最终的 SQL 查询语句，方便调试
        System.out.println("Final SQL: " + sqlExceptSelect);

        // 使用指定的数据源（jiaqi）进行分页查询，得到的是 Page<Record> 对象
        Page<Record> pageOfRecords = Db.use("jiaqi").paginate(pageNumber, pageSize, select, sqlExceptSelect.toString());

        // 将查询结果中的 Record 对象转换为 AutoGasFillingWaitRecord 模型对象
        List<AutoGasFillingWaitRecord> listOfModels = new ArrayList<>();
        for (Record record : pageOfRecords.getList()) {
            AutoGasFillingWaitRecord model = new AutoGasFillingWaitRecord()._setAttrs(record.getColumns());
            listOfModels.add(model);
        }

        // 构建新的 Page 对象，将转换后的模型列表封装到 Page 对象中
        Page<AutoGasFillingWaitRecord> pageOfModels = new Page<>(
                listOfModels,
                pageOfRecords.getPageNumber(),
                pageOfRecords.getPageSize(),
                pageOfRecords.getTotalPage(),
                pageOfRecords.getTotalRow()
        );

        return pageOfModels;
    }

    /**
     * 根据 id 删除 auto_gas_filling_wait_record 表中的记录
     * @param id 要删除的记录的 id
     */
    public void deleteById(Integer id) {
        Db.use("jiaqi").deleteById("auto_gas_filling_wait_record", "id", id);
    }
}