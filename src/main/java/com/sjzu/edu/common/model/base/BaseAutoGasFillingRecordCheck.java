package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAutoGasFillingRecordCheck<M extends BaseAutoGasFillingRecordCheck<M>> extends Model<M> implements IBean {

	public void setFillUuid(java.lang.String fillUuid) {
		set("fill_uuid", fillUuid);
	}
	
	public java.lang.String getFillUuid() {
		return getStr("fill_uuid");
	}
	
	public void setBeforeCorrosion(java.lang.String beforeCorrosion) {
		set("before_corrosion", beforeCorrosion);
	}
	
	public java.lang.String getBeforeCorrosion() {
		return getStr("before_corrosion");
	}
	
	public void setBeforeLeak(java.lang.String beforeLeak) {
		set("before_leak", beforeLeak);
	}
	
	public java.lang.String getBeforeLeak() {
		return getStr("before_leak");
	}
	
	public void setBeforeDeformation(java.lang.String beforeDeformation) {
		set("before_deformation", beforeDeformation);
	}
	
	public java.lang.String getBeforeDeformation() {
		return getStr("before_deformation");
	}
	
	public void setBeforeAbrasion(java.lang.String beforeAbrasion) {
		set("before_abrasion", beforeAbrasion);
	}
	
	public java.lang.String getBeforeAbrasion() {
		return getStr("before_abrasion");
	}
	
	public void setBeforeConnect(java.lang.String beforeConnect) {
		set("before_connect", beforeConnect);
	}
	
	public java.lang.String getBeforeConnect() {
		return getStr("before_connect");
	}
	
	public void setBeforeGreasydirt(java.lang.String beforeGreasydirt) {
		set("before_greasyDirt", beforeGreasydirt);
	}
	
	public java.lang.String getBeforeGreasydirt() {
		return getStr("before_greasyDirt");
	}
	
	public void setIngLeak(java.lang.String ingLeak) {
		set("ing_leak", ingLeak);
	}
	
	public java.lang.String getIngLeak() {
		return getStr("ing_leak");
	}
	
	public void setIngAbnormal(java.lang.String ingAbnormal) {
		set("ing_abnormal", ingAbnormal);
	}
	
	public java.lang.String getIngAbnormal() {
		return getStr("ing_abnormal");
	}
	
	public void setAfterLeak(java.lang.String afterLeak) {
		set("after_leak", afterLeak);
	}
	
	public java.lang.String getAfterLeak() {
		return getStr("after_leak");
	}
	
	public void setAfterDeformation(java.lang.String afterDeformation) {
		set("after_deformation", afterDeformation);
	}
	
	public java.lang.String getAfterDeformation() {
		return getStr("after_deformation");
	}
	
	public void setAfterTemperature(java.lang.String afterTemperature) {
		set("after_temperature", afterTemperature);
	}
	
	public java.lang.String getAfterTemperature() {
		return getStr("after_temperature");
	}
	
	public void setLicensePlate(java.lang.String licensePlate) {
		set("license_plate", licensePlate);
	}
	
	public java.lang.String getLicensePlate() {
		return getStr("license_plate");
	}
	
	public void setGunNo(java.lang.Integer gunNo) {
		set("gun_no", gunNo);
	}
	
	public java.lang.Integer getGunNo() {
		return getInt("gun_no");
	}
	
	public void setOrgCode(java.lang.String orgCode) {
		set("org_code", orgCode);
	}
	
	public java.lang.String getOrgCode() {
		return getStr("org_code");
	}
	
	public void setCreateTime(java.lang.String createTime) {
		set("create_time", createTime);
	}
	
	public java.lang.String getCreateTime() {
		return getStr("create_time");
	}
	
	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}
	
	public java.lang.String getRemark() {
		return getStr("remark");
	}
	
}

