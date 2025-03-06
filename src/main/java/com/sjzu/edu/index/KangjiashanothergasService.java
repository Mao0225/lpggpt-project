package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.AutoGasFillingRecord;
import com.sjzu.edu.common.model.Othergas;

import java.util.ArrayList;
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
    public Page<Othergas> search(int pageNumber, int pageSize, String stationcode, String tradedate) {
        String select = "SELECT *";
        String sqlExceptSelect = "FROM othergas";
        List<Object> params = new ArrayList<>(); // 用于参数化查询
        int conditionCount = 0; // 记录已添加的条件数量

        // 构建 WHERE 条件
        if (stationcode != null && !stationcode.isEmpty()) {
            sqlExceptSelect += (conditionCount == 0 ? " WHERE " : " AND ") + "stationCode LIKE ?";
            params.add("%" + stationcode + "%");
            conditionCount++;
        }

        if (tradedate != null && !tradedate.isEmpty()) {
            System.out.println("tradedate:" + tradedate);
            sqlExceptSelect += (conditionCount == 0 ? " WHERE " : " AND ") +
                    "TradeDate >= ? AND TradeDate < ?";
            params.add(tradedate + " 00:00:00"); // 开始时间
            params.add(tradedate + " 23:59:59"); // 结束时间
            conditionCount++;
        }

        // 如果没有任何条件，查询当天数据
        if (conditionCount == 0) {
            sqlExceptSelect += " WHERE DATE(TradeDate) = CURDATE()";
        }

        sqlExceptSelect += " ORDER BY TradeDate DESC";

        // 使用参数化查询
        Page<Record> pageOfRecords = Db.use("jiaqi").paginate(
                pageNumber,
                pageSize,
                select,
                sqlExceptSelect,
                params.toArray() // 自动填充参数
        );

        // 转换Record到Othergas
        List<Othergas> listOfModels = pageOfRecords.getList().stream()
                .map(record -> new Othergas()._setAttrs(record.getColumns()))
                .collect(Collectors.toList());

        return new Page<>(
                listOfModels,
                pageOfRecords.getPageNumber(),
                pageOfRecords.getPageSize(),
                pageOfRecords.getTotalPage(),
                pageOfRecords.getTotalRow()
        );
    }
}