package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDianzibiaoqian<M extends BaseDianzibiaoqian<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	public void setDeviceid(java.lang.String deviceid) {
		set("deviceid", deviceid);
	}
	
	public java.lang.String getDeviceid() {
		return getStr("deviceid");
	}
	
	public void setDzbqvalue(java.lang.String dzbqvalue) {
		set("dzbqvalue", dzbqvalue);
	}
	
	public java.lang.String getDzbqvalue() {
		return getStr("dzbqvalue");
	}
	
	public void setCreattime(java.lang.String creattime) {
		set("creattime", creattime);
	}
	
	public java.lang.String getCreattime() {
		return getStr("creattime");
	}
	
	public void setFlag(java.lang.String flag) {
		set("flag", flag);
	}
	
	public java.lang.String getFlag() {
		return getStr("flag");
	}
	
	public void setStatus(java.lang.String status) {
		set("status", status);
	}
	
	public java.lang.String getStatus() {
		return getStr("status");
	}
	
	public void setType(java.lang.String type) {
		set("type", type);
	}
	
	public java.lang.String getType() {
		return getStr("type");
	}
	
	public void setMemo(java.lang.String memo) {
		set("memo", memo);
	}
	
	public java.lang.String getMemo() {
		return getStr("memo");
	}
	
}

