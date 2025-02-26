package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.AutoGasFillingRecord;
import com.sjzu.edu.common.model.Bangdingren;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class KangjiashanService {

//auto_gas_filling_record
public Page<AutoGasFillingRecord> paginate(int pageNumber, int pageSize) {
    String select = "SELECT *";
    String sqlExceptSelect = "FROM auto_gas_filling_record ORDER BY id DESC";

    // 使用指定的数据源进行分页查询，得到的是Page<Record>
    Page<Record> pageOfRecords = Db.use("jiaqi").paginate(pageNumber, pageSize, select, sqlExceptSelect);

    // 转换Record到AutoGasFillingRecord
    List<AutoGasFillingRecord> listOfModels = pageOfRecords.getList().stream()
            .map(record -> new AutoGasFillingRecord()._setAttrs(record.getColumns()))
            .collect(Collectors.toList());

    // 构建新的Page对象
    Page<AutoGasFillingRecord> pageOfModels = new Page<>(
            listOfModels,
            pageOfRecords.getPageNumber(),
            pageOfRecords.getPageSize(),
            pageOfRecords.getTotalPage(),
            pageOfRecords.getTotalRow()
    );

    return pageOfModels;
}
}