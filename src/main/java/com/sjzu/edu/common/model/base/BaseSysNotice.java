package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSysNotice<M extends BaseSysNotice<M>> extends Model<M> implements IBean {

	/**
	 * 公告ID
	 */
	public void setNoticeId(java.lang.Long noticeId) {
		set("notice_id", noticeId);
	}
	
	/**
	 * 公告ID
	 */
	public java.lang.Long getNoticeId() {
		return getLong("notice_id");
	}
	
	/**
	 * 公告标题
	 */
	public void setNoticeTitle(java.lang.String noticeTitle) {
		set("notice_title", noticeTitle);
	}
	
	/**
	 * 公告标题
	 */
	public java.lang.String getNoticeTitle() {
		return getStr("notice_title");
	}
	
	/**
	 * 公告类型（1通知 2公告）
	 */
	public void setNoticeType(java.lang.String noticeType) {
		set("notice_type", noticeType);
	}
	
	/**
	 * 公告类型（1通知 2公告）
	 */
	public java.lang.String getNoticeType() {
		return getStr("notice_type");
	}
	
	/**
	 * 公告内容
	 */
	public void setNoticeContent(byte[] noticeContent) {
		set("notice_content", noticeContent);
	}
	
	/**
	 * 公告内容
	 */
	public byte[] getNoticeContent() {
		return get("notice_content");
	}
	
	/**
	 * 公告状态（0正常 1关闭）
	 */
	public void setStatus(java.lang.String status) {
		set("status", status);
	}
	
	/**
	 * 公告状态（0正常 1关闭）
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

