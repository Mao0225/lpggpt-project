package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.AutoGasFillingRecord;
import com.sjzu.edu.common.model.Othergas;

import java.util.List;
import java.util.stream.Collectors;


public class KangjiashanothergasService {

//auto_gas_filling_record
public Page<Othergas> paginate(int pageNumber, int pageSize) {
    String select = "SELECT *";
    String sqlExceptSelect = "FROM othergas ORDER BY TradeDate DESC";

    // 使用指定的数据源进行分页查询，得到的是Page<Record>
    Page<Record> pageOfRecords = Db.use("jiaqi").paginate(pageNumber, pageSize, select, sqlExceptSelect);

    // 转换Record到AutoGasFillingRecord
    List<Othergas> listOfModels = pageOfRecords.getList().stream()
            .map(record -> new Othergas()._setAttrs(record.getColumns()))
            .collect(Collectors.toList());

    // 构建新的Page对象
    Page<Othergas> pageOfModels = new Page<>(
            listOfModels,
            pageOfRecords.getPageNumber(),
            pageOfRecords.getPageSize(),
            pageOfRecords.getTotalPage(),
            pageOfRecords.getTotalRow()
    );

    return pageOfModels;
}
    public Page<Othergas> search(int pageNumber, int pageSize,String stationcode) {
        String select = "SELECT *";

        String sqlExceptSelect = "FROM othergas ";
        if (stationcode != null && !stationcode.isEmpty()) {
            sqlExceptSelect += "WHERE stationCode LIKE '%" + stationcode + "%' ";
        }
        sqlExceptSelect += "ORDER BY TradeDate DESC";

        // 使用指定的数据源进行分页查询，得到的是Page<Record>
        Page<Record> pageOfRecords = Db.use("jiaqi").paginate(pageNumber, pageSize, select, sqlExceptSelect);

        // 转换Record到AutoGasFillingRecord
        List<Othergas> listOfModels = pageOfRecords.getList().stream()
                .map(record -> new Othergas()._setAttrs(record.getColumns()))
                .collect(Collectors.toList());

        // 构建新的Page对象
        Page<Othergas> pageOfModels = new Page<>(
                listOfModels,
                pageOfRecords.getPageNumber(),
                pageOfRecords.getPageSize(),
                pageOfRecords.getTotalPage(),
                pageOfRecords.getTotalRow()
        );

        return pageOfModels;
    }
}