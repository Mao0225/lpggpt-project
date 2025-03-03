package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAlgModelSdk<M extends BaseAlgModelSdk<M>> extends Model<M> implements IBean {

	/**
	 * 主键标识
	 */
	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	/**
	 * 主键标识
	 */
	public java.lang.Long getId() {
		return getLong("id");
	}
	
	/**
	 * SDK编码
	 */
	public void setCode(java.lang.String code) {
		set("code", code);
	}
	
	/**
	 * SDK编码
	 */
	public java.lang.String getCode() {
		return getStr("code");
	}
	
	/**
	 * SDK名称
	 */
	public void setName(java.lang.String name) {
		set("name", name);
	}
	
	/**
	 * SDK名称
	 */
	public java.lang.String getName() {
		return getStr("name");
	}
	
	/**
	 * SDK版本
	 */
	public void setVersion(java.lang.String version) {
		set("version", version);
	}
	
	/**
	 * SDK版本
	 */
	public java.lang.String getVersion() {
		return getStr("version");
	}
	
	/**
	 * 引擎框架
	 */
	public void setEngineFrame(java.lang.String engineFrame) {
		set("engine_frame", engineFrame);
	}
	
	/**
	 * 引擎框架
	 */
	public java.lang.String getEngineFrame() {
		return getStr("engine_frame");
	}
	
	/**
	 * 模型介绍
	 */
	public void setModelIntro(java.lang.String modelIntro) {
		set("model_intro", modelIntro);
	}
	
	/**
	 * 模型介绍
	 */
	public java.lang.String getModelIntro() {
		return getStr("model_intro");
	}
	
	/**
	 * 模型路径
	 */
	public void setModelPath(java.lang.String modelPath) {
		set("model_path", modelPath);
	}
	
	/**
	 * 模型路径
	 */
	public java.lang.String getModelPath() {
		return getStr("model_path");
	}
	
	/**
	 * 基础模型
	 */
	public void setModelBase(java.lang.String modelBase) {
		set("model_base", modelBase);
	}
	
	/**
	 * 基础模型
	 */
	public java.lang.String getModelBase() {
		return getStr("model_base");
	}
	
	/**
	 * 状态
	 */
	public void setSts(java.lang.String sts) {
		set("sts", sts);
	}
	
	/**
	 * 状态
	 */
	public java.lang.String getSts() {
		return getStr("sts");
	}
	
	/**
	 * 状态时间
	 */
	public void setStsDate(java.util.Date stsDate) {
		set("sts_date", stsDate);
	}
	
	/**
	 * 状态时间
	 */
	public java.util.Date getStsDate() {
		return getDate("sts_date");
	}
	
	/**
	 * 创建人
	 */
	public void setCreateBy(java.lang.String createBy) {
		set("create_by", createBy);
	}
	
	/**
	 * 创建人
	 */
	public java.lang.String getCreateBy() {
		return getStr("create_by");
	}
	
	/**
	 * 创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	
	/**
	 * 创建时间
	 */
	public java.util.Date getCreateTime() {
		return getDate("create_time");
	}
	
	/**
	 * 更新人
	 */
	public void setUpdateBy(java.lang.String updateBy) {
		set("update_by", updateBy);
	}
	
	/**
	 * 更新人
	 */
	public java.lang.String getUpdateBy() {
		return getStr("update_by");
	}
	
	/**
	 * 更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
	}
	
	/**
	 * 更新时间
	 */
	public java.util.Date getUpdateTime() {
		return getDate("update_time");
	}
	
	/**
	 * 备注
	 */
	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}
	
	/**
	 * 备注
	 */
	public java.lang.String getRemark() {
		return getStr("remark");
	}
	
}

