package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSuccessfulRecords<M extends BaseSuccessfulRecords<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	public void setCameraName(java.lang.String cameraName) {
		set("cameraName", cameraName);
	}
	
	public java.lang.String getCameraName() {
		return getStr("cameraName");
	}
	
	public void setAnnualInspectDate(java.lang.String annualInspectDate) {
		set("annualInspectDate", annualInspectDate);
	}
	
	public java.lang.String getAnnualInspectDate() {
		return getStr("annualInspectDate");
	}
	
	public void setCarKind(java.lang.String carKind) {
		set("carKind", carKind);
	}
	
	public java.lang.String getCarKind() {
		return getStr("carKind");
	}
	
	public void setCarOwner(java.lang.String carOwner) {
		set("carOwner", carOwner);
	}
	
	public java.lang.String getCarOwner() {
		return getStr("carOwner");
	}
	
	public void setCarTelephone(java.lang.String carTelephone) {
		set("carTelephone", carTelephone);
	}
	
	public java.lang.String getCarTelephone() {
		return getStr("carTelephone");
	}
	
	public void setGasBottleNo(java.lang.String gasBottleNo) {
		set("gasBottleNo", gasBottleNo);
	}
	
	public java.lang.String getGasBottleNo() {
		return getStr("gasBottleNo");
	}
	
	public void setPlate(java.lang.String plate) {
		set("plate", plate);
	}
	
	public java.lang.String getPlate() {
		return getStr("plate");
	}
	
	public void setValidTime(java.lang.String validTime) {
		set("validTime", validTime);
	}
	
	public java.lang.String getValidTime() {
		return getStr("validTime");
	}
	
	public void setWarningInfo(java.lang.String warningInfo) {
		set("warningInfo", warningInfo);
	}
	
	public java.lang.String getWarningInfo() {
		return getStr("warningInfo");
	}
	
	public void setWarningSts(java.lang.String warningSts) {
		set("warningSts", warningSts);
	}
	
	public java.lang.String getWarningSts() {
		return getStr("warningSts");
	}
	
	public void setCreatedTime(java.util.Date createdTime) {
		set("created_time", createdTime);
	}
	
	public java.util.Date getCreatedTime() {
		return getDate("created_time");
	}
	
	public void setUpdatedTime(java.util.Date updatedTime) {
		set("updated_time", updatedTime);
	}
	
	public java.util.Date getUpdatedTime() {
		return getDate("updated_time");
	}
	
}

