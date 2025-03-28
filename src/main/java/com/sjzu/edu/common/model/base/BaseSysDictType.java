package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSysDictType<M extends BaseSysDictType<M>> extends Model<M> implements IBean {

	/**
	 * 字典主键
	 */
	public void setDictId(java.lang.Long dictId) {
		set("dict_id", dictId);
	}
	
	/**
	 * 字典主键
	 */
	public java.lang.Long getDictId() {
		return getLong("dict_id");
	}
	
	/**
	 * 字典名称
	 */
	public void setDictName(java.lang.String dictName) {
		set("dict_name", dictName);
	}
	
	/**
	 * 字典名称
	 */
	public java.lang.String getDictName() {
		return getStr("dict_name");
	}
	
	/**
	 * 字典类型
	 */
	public void setDictType(java.lang.String dictType) {
		set("dict_type", dictType);
	}
	
	/**
	 * 字典类型
	 */
	public java.lang.String getDictType() {
		return getStr("dict_type");
	}
	
	/**
	 * 状态（0正常 1停用）
	 */
	public void setStatus(java.lang.String status) {
		set("status", status);
	}
	
	/**
	 * 状态（0正常 1停用）
	 */
	public java.lang.String getStatus() {
		return getStr("status");
	}
	
	/**
	 * 创建者
	 */
	public void setCreateBy(java.lang.String createBy) {
		set("create_by", createBy);
	}
	
	/**
	 * 创建者
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
	 * 更新者
	 */
	public void setUpdateBy(java.lang.String updateBy) {
		set("update_by", updateBy);
	}
	
	/**
	 * 更新者
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

