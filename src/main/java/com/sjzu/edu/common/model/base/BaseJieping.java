package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseJieping<M extends BaseJieping<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	public void setShexiangtouno(java.lang.String shexiangtouno) {
		set("shexiangtouno", shexiangtouno);
	}
	
	public java.lang.String getShexiangtouno() {
		return getStr("shexiangtouno");
	}
	
	public void setHappendtime(java.lang.String happendtime) {
		set("happendtime", happendtime);
	}
	
	public java.lang.String getHappendtime() {
		return getStr("happendtime");
	}
	
	public void setPhoto(java.lang.String photo) {
		set("photo", photo);
	}
	
	public java.lang.String getPhoto() {
		return getStr("photo");
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

