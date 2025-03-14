package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAutoGasFillingImage<M extends BaseAutoGasFillingImage<M>> extends Model<M> implements IBean {

	public void setFillUuid(java.lang.String fillUuid) {
		set("fill_uuid", fillUuid);
	}
	
	public java.lang.String getFillUuid() {
		return getStr("fill_uuid");
	}
	
	public void setImg1Type(java.lang.String img1Type) {
		set("img1_type", img1Type);
	}
	
	public java.lang.String getImg1Type() {
		return getStr("img1_type");
	}
	
	public void setImg1Name(java.lang.String img1Name) {
		set("img1_name", img1Name);
	}
	
	public java.lang.String getImg1Name() {
		return getStr("img1_name");
	}
	
	public void setImg2Type(java.lang.String img2Type) {
		set("img2_type", img2Type);
	}
	
	public java.lang.String getImg2Type() {
		return getStr("img2_type");
	}
	
	public void setImg2Name(java.lang.String img2Name) {
		set("img2_name", img2Name);
	}
	
	public java.lang.String getImg2Name() {
		return getStr("img2_name");
	}
	
	public void setImg3Type(java.lang.String img3Type) {
		set("img3_type", img3Type);
	}
	
	public java.lang.String getImg3Type() {
		return getStr("img3_type");
	}
	
	public void setImg3Name(java.lang.String img3Name) {
		set("img3_name", img3Name);
	}
	
	public java.lang.String getImg3Name() {
		return getStr("img3_name");
	}
	
	public void setImg4Type(java.lang.String img4Type) {
		set("img4_type", img4Type);
	}
	
	public java.lang.String getImg4Type() {
		return getStr("img4_type");
	}
	
	public void setImg4Name(java.lang.String img4Name) {
		set("img4_name", img4Name);
	}
	
	public java.lang.String getImg4Name() {
		return getStr("img4_name");
	}
	
	public void setImg5Type(java.lang.String img5Type) {
		set("img5_type", img5Type);
	}
	
	public java.lang.String getImg5Type() {
		return getStr("img5_type");
	}
	
	public void setImg5Name(java.lang.String img5Name) {
		set("img5_name", img5Name);
	}
	
	public java.lang.String getImg5Name() {
		return getStr("img5_name");
	}
	
	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	
	public java.util.Date getCreateTime() {
		return getDate("create_time");
	}
	
	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}
	
	public java.lang.String getRemark() {
		return getStr("remark");
	}
	
}

