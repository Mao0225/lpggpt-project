package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDeviceipaddress<M extends BaseDeviceipaddress<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	public void setDevicename(java.lang.String devicename) {
		set("devicename", devicename);
	}
	
	public java.lang.String getDevicename() {
		return getStr("devicename");
	}
	
	public void setIpaddress(java.lang.String ipaddress) {
		set("ipaddress", ipaddress);
	}
	
	public java.lang.String getIpaddress() {
		return getStr("ipaddress");
	}
	
	public void setCreatetime(java.lang.String createtime) {
		set("createtime", createtime);
	}
	
	public java.lang.String getCreatetime() {
		return getStr("createtime");
	}
	
	public void setFlag(java.lang.Integer flag) {
		set("flag", flag);
	}
	
	public java.lang.Integer getFlag() {
		return getInt("flag");
	}
	
	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}
	
	public java.lang.Integer getStatus() {
		return getInt("status");
	}
	
}

