package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseQrtzSimpleTriggers<M extends BaseQrtzSimpleTriggers<M>> extends Model<M> implements IBean {

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
	 * 重复的次数统计
	 */
	public void setRepeatCount(java.lang.Long repeatCount) {
		set("repeat_count", repeatCount);
	}
	
	/**
	 * 重复的次数统计
	 */
	public java.lang.Long getRepeatCount() {
		return getLong("repeat_count");
	}
	
	/**
	 * 重复的间隔时间
	 */
	public void setRepeatInterval(java.lang.Long repeatInterval) {
		set("repeat_interval", repeatInterval);
	}
	
	/**
	 * 重复的间隔时间
	 */
	public java.lang.Long getRepeatInterval() {
		return getLong("repeat_interval");
	}
	
	/**
	 * 已经触发的次数
	 */
	public void setTimesTriggered(java.lang.Long timesTriggered) {
		set("times_triggered", timesTriggered);
	}
	
	/**
	 * 已经触发的次数
	 */
	public java.lang.Long getTimesTriggered() {
		return getLong("times_triggered");
	}
	
}

