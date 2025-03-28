package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseQrtzBlobTriggers<M extends BaseQrtzBlobTriggers<M>> extends Model<M> implements IBean {

	/**
	 * 调度名称
	 */
	public void setSchedName(java.lang.String schedName) {
		set("sched_name", schedName);
	}
	
	/**
	 * 调度名称
	 */
	public java.lang.String getSchedName() {
		return getStr("sched_name");
	}
	
	/**
	 * qrtz_triggers表trigger_name的外键
	 */
	public void setTriggerName(java.lang.String triggerName) {
		set("trigger_name", triggerName);
	}
	
	/**
	 * qrtz_triggers表trigger_name的外键
	 */
	public java.lang.String getTriggerName() {
		return getStr("trigger_name");
	}
	
	/**
	 * qrtz_triggers表trigger_group的外键
	 */
	public void setTriggerGroup(java.lang.String triggerGroup) {
		set("trigger_group", triggerGroup);
	}
	
	/**
	 * qrtz_triggers表trigger_group的外键
	 */
	public java.lang.String getTriggerGroup() {
		return getStr("trigger_group");
	}
	
	/**
	 * 存放持久化Trigger对象
	 */
	public void setBlobData(byte[] blobData) {
		set("blob_data", blobData);
	}
	
	/**
	 * 存放持久化Trigger对象
	 */
	public byte[] getBlobData() {
		return get("blob_data");
	}
	
}

