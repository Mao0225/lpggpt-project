package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePressureGaugeRecords<M extends BasePressureGaugeRecords<M>> extends Model<M> implements IBean {

	/**
	 * 无符号自增，主键
	 */
	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	/**
	 * 无符号自增，主键
	 */
	public java.lang.Long getId() {
		return getLong("id");
	}
	
	/**
	 * 标识，可空
	 */
	public void setName(java.lang.String name) {
		set("name", name);
	}
	
	/**
	 * 标识，可空
	 */
	public java.lang.String getName() {
		return getStr("name");
	}
	
	/**
	 * 外键，当前所属用户/饭店id，非空。
默认系统管理员id为1，库存管理id为2，下同。
	 */
	public void setAffiliationUserId(java.lang.Integer affiliationUserId) {
		set("affiliation_user_id", affiliationUserId);
	}
	
	/**
	 * 外键，当前所属用户/饭店id，非空。
默认系统管理员id为1，库存管理id为2，下同。
	 */
	public java.lang.Integer getAffiliationUserId() {
		return getInt("affiliation_user_id");
	}
	
	/**
	 * 外键，当前储气瓶编号，可能是多个
	 */
	public void setGasCylinderId(java.lang.String gasCylinderId) {
		set("gas_cylinder_id", gasCylinderId);
	}
	
	/**
	 * 外键，当前储气瓶编号，可能是多个
	 */
	public java.lang.String getGasCylinderId() {
		return getStr("gas_cylinder_id");
	}
	
	/**
	 * 控制器号，唯一标识
	 */
	public void setControllerId(java.lang.String controllerId) {
		set("controller_id", controllerId);
	}
	
	/**
	 * 控制器号，唯一标识
	 */
	public java.lang.String getControllerId() {
		return getStr("controller_id");
	}
	
	/**
	 * 当前状态。0：正常；1：异常；2：维修；3：其他
	 */
	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}
	
	/**
	 * 当前状态。0：正常；1：异常；2：维修；3：其他
	 */
	public java.lang.Integer getStatus() {
		return getInt("status");
	}
	
	/**
	 * 外键，用户id，非空，首次创建记录时写入
	 */
	public void setCreatedId(java.lang.Integer createdId) {
		set("created_id", createdId);
	}
	
	/**
	 * 外键，用户id，非空，首次创建记录时写入
	 */
	public java.lang.Integer getCreatedId() {
		return getInt("created_id");
	}
	
	/**
	 * 非空，首次创建记录时写入数据库服务器时间
	 */
	public void setCreatedTime(java.util.Date createdTime) {
		set("created_time", createdTime);
	}
	
	/**
	 * 非空，首次创建记录时写入数据库服务器时间
	 */
	public java.util.Date getCreatedTime() {
		return getDate("created_time");
	}
	
	/**
	 * 外键，用户id，非空，每次操作记录时写入。
	 */
	public void setUpdatedId(java.lang.Integer updatedId) {
		set("updated_id", updatedId);
	}
	
	/**
	 * 外键，用户id，非空，每次操作记录时写入。
	 */
	public java.lang.Integer getUpdatedId() {
		return getInt("updated_id");
	}
	
	/**
	 * 非空，每次操作记录时写入数据库服务器时间。
	 */
	public void setUpdatedTime(java.util.Date updatedTime) {
		set("updated_time", updatedTime);
	}
	
	/**
	 * 非空，每次操作记录时写入数据库服务器时间。
	 */
	public java.util.Date getUpdatedTime() {
		return getDate("updated_time");
	}
	
	/**
	 * 可null。为null时表示逻辑存在，写入操作时服务器时间表示逻辑删除。
	 */
	public void setDeletedTime(java.util.Date deletedTime) {
		set("deleted_time", deletedTime);
	}
	
	/**
	 * 可null。为null时表示逻辑存在，写入操作时服务器时间表示逻辑删除。
	 */
	public java.util.Date getDeletedTime() {
		return getDate("deleted_time");
	}
	
	public void setDakaifamen(java.lang.String dakaifamen) {
		set("dakaifamen", dakaifamen);
	}
	
	public java.lang.String getDakaifamen() {
		return getStr("dakaifamen");
	}
	
	public void setGuanbifamen(java.lang.String guanbifamen) {
		set("guanbifamen", guanbifamen);
	}
	
	public java.lang.String getGuanbifamen() {
		return getStr("guanbifamen");
	}
	
	/**
	 * 备用
	 */
	public void setXiafamingling(java.lang.String xiafamingling) {
		set("xiafamingling", xiafamingling);
	}
	
	/**
	 * 备用
	 */
	public java.lang.String getXiafamingling() {
		return getStr("xiafamingling");
	}
	
	public void setStopData(java.lang.String stopData) {
		set("stopData", stopData);
	}
	
	public java.lang.String getStopData() {
		return getStr("stopData");
	}
	
	public void setCreated(java.lang.String created) {
		set("created", created);
	}
	
	public java.lang.String getCreated() {
		return getStr("created");
	}
	
	public void setStopTime(java.lang.String stopTime) {
		set("stop_time", stopTime);
	}
	
	public java.lang.String getStopTime() {
		return getStr("stop_time");
	}
	
	public void setRemark1(java.lang.String remark1) {
		set("remark1", remark1);
	}
	
	public java.lang.String getRemark1() {
		return getStr("remark1");
	}
	
}

