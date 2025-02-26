package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGasBotMedium<M extends BaseGasBotMedium<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}
	
	public void setIdCode(java.lang.String idCode) {
		set("id_code", idCode);
	}
	
	public java.lang.String getIdCode() {
		return getStr("id_code");
	}
	
	/**
	 * 气瓶种类
	 */
	public void setBotType(java.lang.String botType) {
		set("bot_type", botType);
	}
	
	/**
	 * 气瓶种类
	 */
	public java.lang.String getBotType() {
		return getStr("bot_type");
	}
	
	/**
	 * 充装介质
	 */
	public void setMedium(java.lang.String medium) {
		set("medium", medium);
	}
	
	/**
	 * 充装介质
	 */
	public java.lang.String getMedium() {
		return getStr("medium");
	}
	
	/**
	 * 充装单位
	 */
	public void setUnit(java.lang.String unit) {
		set("unit", unit);
	}
	
	/**
	 * 充装单位
	 */
	public java.lang.String getUnit() {
		return getStr("unit");
	}
	
	/**
	 * 最大充装量
	 */
	public void setLimitNum(java.lang.String limitNum) {
		set("limit_num", limitNum);
	}
	
	/**
	 * 最大充装量
	 */
	public java.lang.String getLimitNum() {
		return getStr("limit_num");
	}
	
}

