package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Plcxinhao;

public class PlcxinhaoService {
    // 初始化DAO对象
    Plcxinhao dao = new Plcxinhao().dao();

    /**
     * 分页查询PLC信号数据，支持plcno筛选
     * @param pageNumber 页码
     * @param pageSize 每页条数
     * @param plcno PLC编号查询条件
     * @return 分页数据对象
     */
    public Page<Record> paginate(int pageNumber, int pageSize, String plcno,String alarmType) {
        // 构建SQL语句（关联restaurant表，查询plcxinhao所有字段和restaurant的name）
        String selectSql = "SELECT plcxinhao.*, restaurant.name AS restaurant_name ";
        String fromSql = "FROM plcxinhao " +
                "LEFT JOIN restaurant ON plcxinhao.plcno = restaurant.xiaohezi "; // 关联条件

        int paramCount = 0;

        // 添加plcno查询条件（可筛选特定PLC编号的数据）
        if (plcno != null && !plcno.isEmpty()) {
            fromSql += (paramCount == 0 ? " WHERE " : " AND ") + "plcxinhao.plcno LIKE '%" + plcno + "%' ";
            paramCount++;
        }

        // 添加报警类型查询条件
        if (alarmType != null && !alarmType.isEmpty()) {
            String alarmCondition = getAlarmCondition(alarmType);
            if (alarmCondition != null) {
                fromSql += (paramCount == 0 ? " WHERE " : " AND ") + alarmCondition + " ";
                paramCount++;
            }
        }
        // 按ID倒序排序
        fromSql += " ORDER BY plcxinhao.id DESC ";

        // 执行分页查询（使用Db.paginate而非dao.paginate，因为返回结果包含关联表字段）
        return Db.paginate(pageNumber, pageSize, selectSql, fromSql);
    }

    /**
     * 根据报警类型获取查询条件
     * @param alarmType 报警类型
     * @return SQL查询条件
     */
    private String getAlarmCondition(String alarmType){
        switch(alarmType){
            case"gas_room":
                //气瓶间报警一场：不等于0且不等于正常
                return"(plcxinhao.qipingjianbaojing !='0' AND plcxinhao.qipingjianbaojing !='正常')";
            case"kitchen_leak":
                //厨房泄露报警异常：不等于0且不等于正常
        return"(plcxinhao.chufangbaojing !='0' AND plcxinhao.chufangbaojing !='正常')";
            case"smoke":
                //烟感报警异常：不等于0且不等于正常
        return"(plcxinhao.yanganbaojing !='0' AND plcxinhao.yanganbaojing !='正常')";
            default:
        return null;
        }
    }
    /**
     * 根据ID删除记录
     * @param id 记录ID
     */
    public Plcxinhao findById(Integer id) {
        return dao.findById(id);
    }

    /**
     * 根据ID删除记录
     * @param id 记录ID
     */
    public void deleteById(Integer id) {
        dao.deleteById(id);
    }
}