package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.IotSyncRdsRecordsV2;
import com.sjzu.edu.common.model.Othergas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//t_iot_sync_rds_records_v2
public class LpgyunxingService {

//auto_gas_filling_record
//    public Page<IotSyncRdsRecordsV2> paginate(int pageNumber, int pageSize) {
//        String select = "SELECT *";
//        String sqlExceptSelect = "FROM t_iot_sync_rds_records_v2 ORDER BY id DESC";
//
//        // 使用指定的数据源进行分页查询，得到的是Page<Record>
//        Page<Record> pageOfRecords = Db.use("lpg").paginate(pageNumber, pageSize, select, sqlExceptSelect);
//
//        // 查询restaurant表中的所有记录
//        List<Record> restaurants = Db.find("SELECT * FROM restaurant");
//
//        // 构建一个xiaohezi到name的映射表
//        Map<String, String> xiaoheziToNameMap = restaurants.stream()
//                .collect(Collectors.toMap(
//                        record -> record.getStr("xiaohezi"), // Key: xiaohezi
//                        record -> record.getStr("name") // Value: name
//                ));
//
//        // 转换Record到IotSyncRdsRecordsV2，并设置fandianname属性
//        List<IotSyncRdsRecordsV2> listOfModels = pageOfRecords.getList().stream()
//                .map(record -> {
//                    IotSyncRdsRecordsV2 model = new IotSyncRdsRecordsV2()._setAttrs(record.getColumns());
//
//                    // 获取devicename
//                    String devicename = record.getStr("devicename");
//
//                    // 匹配xiaohezi并设置fandianname
//                    String fandianname = xiaoheziToNameMap.get(devicename);
//                    model.put("fandianname", fandianname); // 使用put方法而不是set方法
//
//                    return model;
//                })
//                .collect(Collectors.toList());
//
//        // 构建新的Page对象
//        Page<IotSyncRdsRecordsV2> pageOfModels = new Page<>(
//                listOfModels,
//                pageOfRecords.getPageNumber(),
//                pageOfRecords.getPageSize(),
//                pageOfRecords.getTotalPage(),
//                pageOfRecords.getTotalRow()
//        );
//
//        // 打印出pageOfModels中的所有数据
//        listOfModels.forEach(model -> {
//            System.out.println("ID: " + model.get("id"));
//            System.out.println("DeviceName: " + model.get("devicename"));
//            System.out.println("FandianName: " + model.get("fandianname"));
//            // 根据需要打印其他字段
//            System.out.println("------");
//        });
//
//        return pageOfModels;
//    }



    public Page<IotSyncRdsRecordsV2> search(int pageNumber, int pageSize,String restaurantname,Integer alarm) {
        String select = "SELECT *";
        String sqlExceptSelect = "FROM t_iot_sync_rds_records_v2 WHERE 1=1"; // 确保WHERE子句始终有效

    // 处理restaurantname
        if (restaurantname != null && !restaurantname.trim().isEmpty()) {
            String query = "SELECT xiaohezi FROM restaurant WHERE name LIKE ?";
            List<Record> deviceNames = Db.find(query, "%" + restaurantname.trim() + "%"); // 获取设备名称

            // 如果存在设备名称
            if (!deviceNames.isEmpty()) {
                // 创建一个StringBuilder来构建WHERE子句
                StringBuilder whereClause = new StringBuilder();
                List<String> deviceList = new ArrayList<>();

                // 遍历设备名称并构建查询条件
                for (Record record : deviceNames) {
                    String deviceName = record.getStr("xiaohezi"); // 假设列名为xiaohezi
                    deviceList.add("'" + deviceName + "'"); // 添加到列表中，并加上单引号以便于SQL语句
                }

                // 使用IN语句构建完整的WHERE子句
                whereClause.append(" AND devicename IN (").append(String.join(", ", deviceList)).append(")");
                sqlExceptSelect += whereClause.toString();
            }
        }

// 处理alarm
        if (alarm != null) {
            sqlExceptSelect += " AND Alarm = " + alarm; // 直接将Integer值拼接到SQL中
        }

// 最终的SQL查询，确保添加ORDER BY
        String finalSql = sqlExceptSelect + " ORDER BY id DESC";


        System.out.println("finalsql=============:"+finalSql);
        // 使用指定的数据源进行分页查询，得到的是Page<Record>
        Page<Record> pageOfRecords = Db.use("lpg").paginate(pageNumber, pageSize, select, finalSql);
        // 查询restaurant表中的所有记录
        List<Record> restaurants = Db.find("SELECT * FROM restaurant");
        // 构建一个xiaohezi到name的映射表
//        Map<String, String> xiaoheziToNameMap = restaurants.stream()
//                .collect(Collectors.toMap(
//                        record -> record.getStr("xiaohezi"), // Key: xiaohezi
//                        record -> record.getStr("name") // Value: name
//                ));
        Map<String, String> xiaoheziToNameMap = restaurants.stream()
                .filter(record -> record.getStr("xiaohezi") != null)
                .collect(Collectors.toMap(
                        record -> record.getStr("xiaohezi"),
                        record -> record.getStr("name"),
                        (existing, replacement) -> existing // 合并函数：保留第一个值
                ));


        // 转换Record到IotSyncRdsRecordsV2，并设置fandianname属性
        List<IotSyncRdsRecordsV2> listOfModels = pageOfRecords.getList().stream()
                .map(record -> {
                    IotSyncRdsRecordsV2 model = new IotSyncRdsRecordsV2()._setAttrs(record.getColumns());

                    // 获取devicename
                    String devicename = record.getStr("devicename");

                    // 匹配xiaohezi并设置fandianname
                    String fandianname = xiaoheziToNameMap.get(devicename);
                    model.put("fandianname", fandianname); // 使用put方法而不是set方法

                    return model;
                })
                .collect(Collectors.toList());

        // 构建新的Page对象
        Page<IotSyncRdsRecordsV2> pageOfModels = new Page<>(
                listOfModels,
                pageOfRecords.getPageNumber(),
                pageOfRecords.getPageSize(),
                pageOfRecords.getTotalPage(),
                pageOfRecords.getTotalRow()
        );

        // 打印出pageOfModels中的所有数据
        listOfModels.forEach(model -> {
            System.out.println("ID: " + model.get("id"));
            System.out.println("DeviceName: " + model.get("devicename"));
            System.out.println("FandianName: " + model.get("fandianname"));
            // 根据需要打印其他字段
            System.out.println("------");
        });

        return pageOfModels;
    }


}