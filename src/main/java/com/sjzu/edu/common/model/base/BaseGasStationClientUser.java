package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGasStationClientUser<M extends BaseGasStationClientUser<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	public void setUserName(java.lang.String userName) {
		set("user_name", userName);
	}
	
	public java.lang.String getUserName() {
		return getStr("user_name");
	}
	
	public void setPassword(java.lang.String password) {
		set("password", password);
	}
	
	public java.lang.String getPassword() {
		return getStr("password");
	}
	
	public void setRealName(java.lang.String realName) {
		set("real_name", realName);
	}
	
	public java.lang.String getRealName() {
		return getStr("real_name");
	}
	
	public void setPhoneNum(java.lang.String phoneNum) {
		set("phone_num", phoneNum);
	}
	
	public java.lang.String getPhoneNum() {
		return getStr("phone_num");
	}
	
	public void setEmail(java.lang.String email) {
		set("email", email);
	}
	
	public java.lang.String getEmail() {
		return getStr("email");
	}
	
	public void setSex(java.lang.String sex) {
		set("sex", sex);
	}
	
	public java.lang.String getSex() {
		return getStr("sex");
	}
	
	public void setOrgCode(java.lang.String orgCode) {
		set("org_code", orgCode);
	}
	
	public java.lang.String getOrgCode() {
		return getStr("org_code");
	}
	
	public void setStationName(java.lang.String stationName) {
		set("station_name", stationName);
	}
	
	public java.lang.String getStationName() {
		return getStr("station_name");
	}
	
	/**
	 * 停用标识：1停用，0在用
	 */
	public void setDisableFlag(java.lang.String disableFlag) {
		set("disable_flag", disableFlag);
	}
	
	/**
	 * 停用标识：1停用，0在用
	 */
	public java.lang.String getDisableFlag() {
		return getStr("disable_flag");
	}
	
	/**
	 * 停用标识：1生效，0注销
	 */
	public void setSts(java.lang.String sts) {
		set("sts", sts);
	}
	
	/**
	 * 停用标识：1生效，0注销
	 */
	public java.lang.String getSts() {
		return getStr("sts");
	}
	
	public void setStsDate(java.util.Date stsDate) {
		set("sts_date", stsDate);
	}
	
	public java.util.Date getStsDate() {
		return getDate("sts_date");
	}
	
	public void setCreateBy(java.lang.String createBy) {
		set("create_by", createBy);
	}
	
	public java.lang.String getCreateBy() {
		return getStr("create_by");
	}
	
	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	
	public java.util.Date getCreateTime() {
		return getDate("create_time");
	}
	
	public void setUpdateBy(java.lang.String updateBy) {
		set("update_by", updateBy);
	}
	
	public java.lang.String getUpdateBy() {
		return getStr("update_by");
	}
	
	public void setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
	}
	
	public java.util.Date getUpdateTime() {
		return getDate("update_time");
	}
	
	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}
	
	public java.lang.String getRemark() {
		return getStr("remark");
	}
	
}

