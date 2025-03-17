package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseShexiangtou<M extends BaseShexiangtou<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	/**
	 * 摄像头编码
	 */
	public void setShexiangtouno(java.lang.String shexiangtouno) {
		set("shexiangtouno", shexiangtouno);
	}
	
	/**
	 * 摄像头编码
	 */
	public java.lang.String getShexiangtouno() {
		return getStr("shexiangtouno");
	}
	
	/**
	 * 饭店id关联
	 */
	public void setRestaurantid(java.lang.Integer restaurantid) {
		set("restaurantid", restaurantid);
	}
	
	/**
	 * 饭店id关联
	 */
	public java.lang.Integer getRestaurantid() {
		return getInt("restaurantid");
	}
	
	/**
	 * 上传数据时间
	 */
	public void setHappendtime(java.lang.String happendtime) {
		set("happendtime", happendtime);
	}
	
	/**
	 * 上传数据时间
	 */
	public java.lang.String getHappendtime() {
		return getStr("happendtime");
	}
	
	/**
	 * 报警类型
	 */
	public void setAlarmtype(java.lang.String alarmtype) {
		set("alarmtype", alarmtype);
	}
	
	/**
	 * 报警类型
	 */
	public java.lang.String getAlarmtype() {
		return getStr("alarmtype");
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
	
	/**
	 * 报警图片
	 */
	public void setAlarmpic(java.lang.String Alarmpic) {
		set("Alarmpic", Alarmpic);
	}
	
	/**
	 * 报警图片
	 */
	public java.lang.String getAlarmpic() {
		return getStr("Alarmpic");
	}
	
	/**
	 * 警报信息
	 */
	public void setAlarmmes(java.lang.String alarmmes) {
		set("alarmmes", alarmmes);
	}
	
	/**
	 * 警报信息
	 */
	public java.lang.String getAlarmmes() {
		return getStr("alarmmes");
	}
	
	/**
	 * 饭店名字
	 */
	public void setRestaurantname(java.lang.String restaurantname) {
		set("restaurantname", restaurantname);
	}
	
	/**
	 * 饭店名字
	 */
	public java.lang.String getRestaurantname() {
		return getStr("restaurantname");
	}
	
	/**
	 * 饭店电话
	 */
	public void setRestaurantphone(java.lang.String restaurantphone) {
		set("restaurantphone", restaurantphone);
	}
	
	/**
	 * 饭店电话
	 */
	public java.lang.String getRestaurantphone() {
		return getStr("restaurantphone");
	}
	
}

