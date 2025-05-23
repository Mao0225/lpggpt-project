package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseBangdingren<M extends BaseBangdingren<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	public void setTelphone(java.lang.String telphone) {
		set("telphone", telphone);
	}
	
	public java.lang.String getTelphone() {
		return getStr("telphone");
	}
	
	public void setPsw(java.lang.String psw) {
		set("psw", psw);
	}
	
	public java.lang.String getPsw() {
		return getStr("psw");
	}
	
	public void setName(java.lang.String name) {
		set("name", name);
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}
	
	public void setChushengriqi(java.lang.String chushengriqi) {
		set("chushengriqi", chushengriqi);
	}
	
	public java.lang.String getChushengriqi() {
		return getStr("chushengriqi");
	}
	
	/**
	 * 废弃
	 */
	public void setZigezhengshu(java.lang.String zigezhengshu) {
		set("zigezhengshu", zigezhengshu);
	}
	
	/**
	 * 废弃
	 */
	public java.lang.String getZigezhengshu() {
		return getStr("zigezhengshu");
	}
	
	public void setSex(java.lang.String sex) {
		set("sex", sex);
	}
	
	public java.lang.String getSex() {
		return getStr("sex");
	}
	
	public void setIdentityCardNo(java.lang.String identityCardNo) {
		set("identity_card_no", identityCardNo);
	}
	
	public java.lang.String getIdentityCardNo() {
		return getStr("identity_card_no");
	}
	
	public void setTrainCertificateNo(java.lang.String trainCertificateNo) {
		set("train_certificate_no", trainCertificateNo);
	}
	
	public java.lang.String getTrainCertificateNo() {
		return getStr("train_certificate_no");
	}
	
	public void setTrainCertificateIndate(java.lang.String trainCertificateIndate) {
		set("train_certificate_indate", trainCertificateIndate);
	}
	
	public java.lang.String getTrainCertificateIndate() {
		return getStr("train_certificate_indate");
	}
	
	public void setTrainCertificateImageUrl(java.lang.String trainCertificateImageUrl) {
		set("train_certificate_image_url", trainCertificateImageUrl);
	}
	
	public java.lang.String getTrainCertificateImageUrl() {
		return getStr("train_certificate_image_url");
	}
	
	public void setTrainCertificateImageOssId(java.lang.String trainCertificateImageOssId) {
		set("train_certificate_image_oss_id", trainCertificateImageOssId);
	}
	
	public java.lang.String getTrainCertificateImageOssId() {
		return getStr("train_certificate_image_oss_id");
	}
	
	public void setUpdateSign(java.lang.Integer updateSign) {
		set("update_sign", updateSign);
	}
	
	public java.lang.Integer getUpdateSign() {
		return getInt("update_sign");
	}
	
}

