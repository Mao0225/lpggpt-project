package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDetectCamera<M extends BaseDetectCamera<M>> extends Model<M> implements IBean {

	/**
	 * 主键ID
	 */
	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	/**
	 * 主键ID
	 */
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	/**
	 * 视频流地址
	 */
	public void setVideoUrl(java.lang.String videoUrl) {
		set("video_url", videoUrl);
	}
	
	/**
	 * 视频流地址
	 */
	public java.lang.String getVideoUrl() {
		return getStr("video_url");
	}
	
	/**
	 * 摄像头编号
	 */
	public void setDeviceNumber(java.lang.String deviceNumber) {
		set("device_number", deviceNumber);
	}
	
	/**
	 * 摄像头编号
	 */
	public java.lang.String getDeviceNumber() {
		return getStr("device_number");
	}
	
	/**
	 * 摄像头唯一标识
	 */
	public void setCameraUuid(java.lang.String cameraUuid) {
		set("camera_uuid", cameraUuid);
	}
	
	/**
	 * 摄像头唯一标识
	 */
	public java.lang.String getCameraUuid() {
		return getStr("camera_uuid");
	}
	
	/**
	 * 所属树莓派ID
	 */
	public void setRaspberryPiId(java.lang.Integer raspberryPiId) {
		set("raspberry_pi_id", raspberryPiId);
	}
	
	/**
	 * 所属树莓派ID
	 */
	public java.lang.Integer getRaspberryPiId() {
		return getInt("raspberry_pi_id");
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
	 * 删除标记(0:未删除 1:已删除)
	 */
	public void setIsDeleted(java.lang.Boolean isDeleted) {
		set("is_deleted", isDeleted);
	}
	
	/**
	 * 删除标记(0:未删除 1:已删除)
	 */
	public java.lang.Boolean getIsDeleted() {
		return get("is_deleted");
	}
	
}

