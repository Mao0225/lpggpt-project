package com.sjzu.edu.index;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.BseEquipment;
import com.sjzu.edu.common.model.User;

import javax.servlet.http.HttpSession;

public class EquipmentService {
    private BseEquipment dao = new BseEquipment().dao();

    // 用于分页查询bse_equipment表的方法，可根据传入条件筛选
    public Page<Record> paginate(int pageNumber, int pageSize, String equipmentName, String equipmentKind, Controller controller) {
        String selectFields = "select *";
        StringBuilder sqlBuilder = new StringBuilder("from bse_equipment");

        // 构建动态查询条件，先添加原有的根据设备名称和种类的筛选条件
        if (equipmentName!= null &&!equipmentName.isEmpty()) {
            sqlBuilder.append(" WHERE name LIKE '%").append(equipmentName).append("%'");
        }
        if (equipmentKind!= null &&!equipmentKind.isEmpty()) {
            if (sqlBuilder.toString().contains("WHERE")) {
                sqlBuilder.append(" AND kind = '").append(equipmentKind).append("'");
            } else {
                sqlBuilder.append(" WHERE kind = '").append(equipmentKind).append("'");
            }
        }

        // 获取当前登录用户的stationid，从存储在会话中的User对象里获取（这里假设你在其他地方已经正确将登录用户存入会话了）
        if (controller!= null) {
            HttpSession session = controller.getSession(false);
            if (session!= null) {
                User currentUser = (User) session.getAttribute("user");
                if (currentUser!= null) {
                    int userStationId = currentUser.getStationid();
                    // 添加基于用户stationid与设备表companyid匹配的筛选条件
                    if (sqlBuilder.toString().contains("WHERE")) {
                        sqlBuilder.append(" AND companyid = ").append(userStationId);
                    } else {
                        sqlBuilder.append(" WHERE companyid = ").append(userStationId);
                    }
                }
            }
        }

        sqlBuilder.append(" ORDER BY id DESC");
        String sql = sqlBuilder.toString();
         System.out.println(sql);
        return Db.paginate(pageNumber, pageSize, selectFields, sql);
    }

    // 可根据更多条件灵活查询的方法（这里你可以按需进一步扩展添加更多条件判断逻辑，目前先按照名称和种类示例）
    public Page<Record> search(int pageNumber, int pageSize, String equipmentName, String equipmentKind, Controller controller) {
        return paginate(pageNumber, pageSize, equipmentName, equipmentKind, controller);
    }


    public BseEquipment findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }
    // 新增方法，根据equipmentid和companyid查找bse_maintenance表中对应数据行的所有字段并返回
    public Record findByEquipmentIdAndCompanyId(int equipmentId) {
        // 明确指定SELECT语句的字段部分
        String selectFields = "SELECT * ";
        StringBuilder sqlBuilder = new StringBuilder();
        // 先添加SELECT字段部分
        sqlBuilder.append(selectFields);
        // 再添加FROM和表名以及WHERE条件部分，使用String.format确保空格等格式正确
        sqlBuilder.append(String.format("FROM %s ", "bse_equipment"));
        sqlBuilder.append(String.format("WHERE id = %d ", equipmentId));
        String sql = sqlBuilder.toString();
        System.out.println("构建的完整SQL语句为: " + sql);
        // 使用Db.findFirst尝试获取符合条件的第一条（也是唯一一条，根据你的业务逻辑应该是对应唯一数据行）记录
        return Db.findFirst(sql);
    }
    public Record findBycoampanyId( int companyid) {
        // 明确指定SELECT语句的字段部分
        String selectFields = "SELECT * ";
        StringBuilder sqlBuilder = new StringBuilder();
        // 先添加SELECT字段部分
        sqlBuilder.append(selectFields);
        // 再添加FROM和表名以及WHERE条件部分，使用String.format确保空格等格式正确
        sqlBuilder.append(String.format("FROM %s ", "gas_station"));
        sqlBuilder.append(String.format("WHERE id= %d ", companyid));
        String sql = sqlBuilder.toString();
        System.out.println("构建的完整SQL语句为: " + sql);
        System.out.println(Db.findFirst(sql));
        // 使用Db.findFirst尝试获取符合条件的第一条（也是唯一一条，根据你的业务逻辑应该是对应唯一数据行）记录
        return Db.findFirst(sql);
    }
    // 新增函数，用于从bse_equipment表中查找最大的id值并返回
    public Integer findMaxId() {
        String sql = "SELECT MAX(id) AS max_id FROM bse_equipment";
        Record record = Db.findFirst(sql);
        if (record!= null) {
            return record.getInt("max_id");
        }
        return null;
    }
}