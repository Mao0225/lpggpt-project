package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCustomer<M extends BaseCustomer<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	/**
	 * 加气站名称
	 */
	public void setStation(java.lang.String station) {
		set("station", station);
	}
	
	/**
	 * 加气站名称
	 */
	public java.lang.String getStation() {
		return getStr("station");
	}
	
	/**
	 * 加气站地址
	 */
	public void setSaddress(java.lang.String saddress) {
		set("saddress", saddress);
	}
	
	/**
	 * 加气站地址
	 */
	public java.lang.String getSaddress() {
		return getStr("saddress");
	}
	
	/**
	 * 加气站id
	 */
	public void setStationid(java.lang.Integer stationid) {
		set("stationid", stationid);
	}
	
	/**
	 * 加气站id
	 */
	public java.lang.Integer getStationid() {
		return getInt("stationid");
	}
	
	/**
	 * 客户名称
	 */
	public void setCustomer(java.lang.String customer) {
		set("customer", customer);
	}
	
	/**
	 * 客户名称
	 */
	public java.lang.String getCustomer() {
		return getStr("customer");
	}
	
	public void setTelephone(java.lang.String telephone) {
		set("telephone", telephone);
	}
	
	public java.lang.String getTelephone() {
		return getStr("telephone");
	}
	
	public void setCaddress(java.lang.String caddress) {
		set("caddress", caddress);
	}
	
	public java.lang.String getCaddress() {
		return getStr("caddress");
	}
	
	public void setCreattime(java.util.Date creattime) {
		set("creattime", creattime);
	}
	
	public java.util.Date getCreattime() {
		return getDate("creattime");
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
	
	public void setRemark1(java.lang.String remark1) {
		set("remark1", remark1);
	}
	
	public java.lang.String getRemark1() {
		return getStr("remark1");
	}
	
	public void setRemark2(java.lang.String remark2) {
		set("remark2", remark2);
	}
	
	public java.lang.String getRemark2() {
		return getStr("remark2");
	}
	
}

