package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseQrtzPausedTriggerGrps<M extends BaseQrtzPausedTriggerGrps<M>> extends Model<M> implements IBean {

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
	
}

