package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGasStationGunMap<M extends BaseGasStationGunMap<M>> extends Model<M> implements IBean {

	/**
	 * 加气枪id
	 */
	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	/**
	 * 加气枪id
	 */
	public java.lang.Long getId() {
		return getLong("id");
	}
	
	/**
	 * 加气枪编码
	 */
	public void setGunId(java.lang.String gunId) {
		set("gun_id", gunId);
	}
	
	/**
	 * 加气枪编码
	 */
	public java.lang.String getGunId() {
		return getStr("gun_id");
	}
	
	/**
	 * 加气枪名称
	 */
	public void setGunName(java.lang.String gunName) {
		set("gun_name", gunName);
	}
	
	/**
	 * 加气枪名称
	 */
	public java.lang.String getGunName() {
		return getStr("gun_name");
	}
	
	/**
	 * 充装介质
	 */
	public void setGunMedium(java.lang.String gunMedium) {
		set("gun_medium", gunMedium);
	}
	
	/**
	 * 充装介质
	 */
	public java.lang.String getGunMedium() {
		return getStr("gun_medium");
	}
	
	/**
	 * 充装单位，CNG：m3，LNG：kg
	 */
	public void setUnit(java.lang.String unit) {
		set("unit", unit);
	}
	
	/**
	 * 充装单位，CNG：m3，LNG：kg
	 */
	public java.lang.String getUnit() {
		return getStr("unit");
	}
	
	/**
	 * 组织ID，用来查询部门和用户的id
	 */
	public void setOrgId(java.lang.String orgId) {
		set("org_id", orgId);
	}
	
	/**
	 * 组织ID，用来查询部门和用户的id
	 */
	public java.lang.String getOrgId() {
		return getStr("org_id");
	}
	
	/**
	 * 单位组织代码（加气站代码）
	 */
	public void setOrgCode(java.lang.String orgCode) {
		set("org_code", orgCode);
	}
	
	/**
	 * 单位组织代码（加气站代码）
	 */
	public java.lang.String getOrgCode() {
		return getStr("org_code");
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
	
	/**
	 * 站内加气枪编号
	 */
	public void setGunNo(java.lang.Integer gunNo) {
		set("gun_no", gunNo);
	}
	
	/**
	 * 站内加气枪编号
	 */
	public java.lang.Integer getGunNo() {
		return getInt("gun_no");
	}
	
	/**
	 * 部门编码
	 */
	public void setDeptCode(java.lang.String deptCode) {
		set("dept_code", deptCode);
	}
	
	/**
	 * 部门编码
	 */
	public java.lang.String getDeptCode() {
		return getStr("dept_code");
	}
	
	/**
	 * 部门名称
	 */
	public void setDeptName(java.lang.String deptName) {
		set("dept_name", deptName);
	}
	
	/**
	 * 部门名称
	 */
	public java.lang.String getDeptName() {
		return getStr("dept_name");
	}
	
	/**
	 * 登记人ID
	 */
	public void setFillinUserid(java.lang.String fillinUserid) {
		set("fillin_userid", fillinUserid);
	}
	
	/**
	 * 登记人ID
	 */
	public java.lang.String getFillinUserid() {
		return getStr("fillin_userid");
	}
	
	/**
	 * 登记人
	 */
	public void setFillinName(java.lang.String fillinName) {
		set("fillin_name", fillinName);
	}
	
	/**
	 * 登记人
	 */
	public java.lang.String getFillinName() {
		return getStr("fillin_name");
	}
	
	/**
	 * 检查人ID
	 */
	public void setCheckinUserid(java.lang.String checkinUserid) {
		set("checkin_userid", checkinUserid);
	}
	
	/**
	 * 检查人ID
	 */
	public java.lang.String getCheckinUserid() {
		return getStr("checkin_userid");
	}
	
	/**
	 * 检查人
	 */
	public void setCheckinName(java.lang.String checkinName) {
		set("checkin_name", checkinName);
	}
	
	/**
	 * 检查人
	 */
	public java.lang.String getCheckinName() {
		return getStr("checkin_name");
	}
	
	/**
	 * 状态
	 */
	public void setSts(java.lang.String sts) {
		set("sts", sts);
	}
	
	/**
	 * 状态
	 */
	public java.lang.String getSts() {
		return getStr("sts");
	}
	
	/**
	 * 状态时间
	 */
	public void setStsDate(java.util.Date stsDate) {
		set("sts_date", stsDate);
	}
	
	/**
	 * 状态时间
	 */
	public java.util.Date getStsDate() {
		return getDate("sts_date");
	}
	
}

