package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDrivergpsinfo<M extends BaseDrivergpsinfo<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	/**
	 * drivercar表id，用来区分是哪个司机和车
	 */
	public void setDrivercarid(java.lang.Integer drivercarid) {
		set("drivercarid", drivercarid);
	}
	
	/**
	 * drivercar表id，用来区分是哪个司机和车
	 */
	public java.lang.Integer getDrivercarid() {
		return getInt("drivercarid");
	}
	
	public void setJingdu(java.lang.String jingdu) {
		set("jingdu", jingdu);
	}
	
	public java.lang.String getJingdu() {
		return getStr("jingdu");
	}
	
	public void setWeidu(java.lang.String weidu) {
		set("weidu", weidu);
	}
	
	public java.lang.String getWeidu() {
		return getStr("weidu");
	}
	
	public void setAddress(java.lang.String address) {
		set("address", address);
	}
	
	public java.lang.String getAddress() {
		return getStr("address");
	}
	
	/**
	 * 数据上传时间
	 */
	public void setUpdatetime(java.lang.String updatetime) {
		set("updatetime", updatetime);
	}
	
	/**
	 * 数据上传时间
	 */
	public java.lang.String getUpdatetime() {
		return getStr("updatetime");
	}
	
	/**
	 * 司机姓名
	 */
	public void setDrivername(java.lang.String drivername) {
		set("drivername", drivername);
	}
	
	/**
	 * 司机姓名
	 */
	public java.lang.String getDrivername() {
		return getStr("drivername");
	}
	
	/**
	 * 车牌
	 */
	public void setCarno(java.lang.String carno) {
		set("carno", carno);
	}
	
	/**
	 * 车牌
	 */
	public java.lang.String getCarno() {
		return getStr("carno");
	}
	
	/**
	 * 司机电话
	 */
	public void setDriverphone(java.lang.String driverphone) {
		set("driverphone", driverphone);
	}
	
	/**
	 * 司机电话
	 */
	public java.lang.String getDriverphone() {
		return getStr("driverphone");
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
	
	public void setMemo(java.lang.String memo) {
		set("memo", memo);
	}
	
	public java.lang.String getMemo() {
		return getStr("memo");
	}
	
}

