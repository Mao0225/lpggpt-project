package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseEnterStation<M extends BaseEnterStation<M>> extends Model<M> implements IBean {

	/**
	 * 进站记录ID
	 */
	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	/**
	 * 进站记录ID
	 */
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	/**
	 * 气瓶编码（合格证号）
	 */
	public void setGasBottleNo(java.lang.String gasBottleNo) {
		set("gas_bottle_no", gasBottleNo);
	}
	
	/**
	 * 气瓶编码（合格证号）
	 */
	public java.lang.String getGasBottleNo() {
		return getStr("gas_bottle_no");
	}
	
	/**
	 * 操作员ID
	 */
	public void setStaffId(java.lang.Integer staffId) {
		set("staff_id", staffId);
	}
	
	/**
	 * 操作员ID
	 */
	public java.lang.Integer getStaffId() {
		return getInt("staff_id");
	}
	
	/**
	 * 操作员姓名
	 */
	public void setStaffName(java.lang.String staffName) {
		set("staff_name", staffName);
	}
	
	/**
	 * 操作员姓名
	 */
	public java.lang.String getStaffName() {
		return getStr("staff_name");
	}
	
	/**
	 * 经度
	 */
	public void setJingdu(java.lang.String jingdu) {
		set("jingdu", jingdu);
	}
	
	/**
	 * 经度
	 */
	public java.lang.String getJingdu() {
		return getStr("jingdu");
	}
	
	/**
	 * 纬度
	 */
	public void setWeidu(java.lang.String weidu) {
		set("weidu", weidu);
	}
	
	/**
	 * 纬度
	 */
	public java.lang.String getWeidu() {
		return getStr("weidu");
	}
	
	/**
	 * 进站时间
	 */
	public void setEnterTime(java.util.Date enterTime) {
		set("enter_time", enterTime);
	}
	
	/**
	 * 进站时间
	 */
	public java.util.Date getEnterTime() {
		return getDate("enter_time");
	}
	
	/**
	 * 加气站ID
	 */
	public void setStationId(java.lang.Integer stationId) {
		set("station_id", stationId);
	}
	
	/**
	 * 加气站ID
	 */
	public java.lang.Integer getStationId() {
		return getInt("station_id");
	}
	
	/**
	 * 加气站名称
	 */
	public void setStationName(java.lang.String stationName) {
		set("station_name", stationName);
	}
	
	/**
	 * 加气站名称
	 */
	public java.lang.String getStationName() {
		return getStr("station_name");
	}
	
}

