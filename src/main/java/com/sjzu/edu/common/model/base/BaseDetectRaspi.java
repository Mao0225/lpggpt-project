package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDetectRaspi<M extends BaseDetectRaspi<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	/**
	 * 唯一标识
	 */
	public void setRaspiUuid(java.lang.String raspiUuid) {
		set("raspi_uuid", raspiUuid);
	}
	
	/**
	 * 唯一标识
	 */
	public java.lang.String getRaspiUuid() {
		return getStr("raspi_uuid");
	}
	
	/**
	 * 编号
	 */
	public void setSerialNumber(java.lang.String serialNumber) {
		set("serial_number", serialNumber);
	}
	
	/**
	 * 编号
	 */
	public java.lang.String getSerialNumber() {
		return getStr("serial_number");
	}
	
	/**
	 * 所属部门id
	 */
	public void setDeptId(java.lang.Integer deptId) {
		set("dept_id", deptId);
	}
	
	/**
	 * 所属部门id
	 */
	public java.lang.Integer getDeptId() {
		return getInt("dept_id");
	}
	
	/**
	 * 放置地点
	 */
	public void setLocation(java.lang.String location) {
		set("location", location);
	}
	
	/**
	 * 放置地点
	 */
	public java.lang.String getLocation() {
		return getStr("location");
	}
	
	/**
	 * 上传间隔时间(秒)
	 */
	public void setUploadInterval(java.lang.Integer uploadInterval) {
		set("upload_interval", uploadInterval);
	}
	
	/**
	 * 上传间隔时间(秒)
	 */
	public java.lang.Integer getUploadInterval() {
		return getInt("upload_interval");
	}
	
	/**
	 * 报警词汇
	 */
	public void setAlertKeywords(java.lang.String alertKeywords) {
		set("alert_keywords", alertKeywords);
	}
	
	/**
	 * 报警词汇
	 */
	public java.lang.String getAlertKeywords() {
		return getStr("alert_keywords");
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
	 * 删除标记(0-未删除,1-已删除)
	 */
	public void setIsDeleted(java.lang.Boolean isDeleted) {
		set("is_deleted", isDeleted);
	}
	
	/**
	 * 删除标记(0-未删除,1-已删除)
	 */
	public java.lang.Boolean getIsDeleted() {
		return get("is_deleted");
	}
	
	/**
	 * 终端ssl链接
	 */
	public void setTerminalLink(java.lang.String terminalLink) {
		set("terminal_link", terminalLink);
	}
	
	/**
	 * 终端ssl链接
	 */
	public java.lang.String getTerminalLink() {
		return getStr("terminal_link");
	}
	
	/**
	 * 桌面vnc链接
	 */
	public void setDesktopLink(java.lang.String desktopLink) {
		set("desktop_link", desktopLink);
	}
	
	/**
	 * 桌面vnc链接
	 */
	public java.lang.String getDesktopLink() {
		return getStr("desktop_link");
	}
	
	/**
	 * 树莓派连接账号
	 */
	public void setUsername(java.lang.String username) {
		set("username", username);
	}
	
	/**
	 * 树莓派连接账号
	 */
	public java.lang.String getUsername() {
		return getStr("username");
	}
	
	/**
	 * 树莓派连接密码
	 */
	public void setPassword(java.lang.String password) {
		set("password", password);
	}
	
	/**
	 * 树莓派连接密码
	 */
	public java.lang.String getPassword() {
		return getStr("password");
	}
	
}

