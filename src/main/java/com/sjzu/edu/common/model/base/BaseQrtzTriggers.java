package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseQrtzTriggers<M extends BaseQrtzTriggers<M>> extends Model<M> implements IBean {

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
	 * 触发器的名字
	 */
	public void setTriggerName(java.lang.String triggerName) {
		set("trigger_name", triggerName);
	}
	
	/**
	 * 触发器的名字
	 */
	public java.lang.String getTriggerName() {
		return getStr("trigger_name");
	}
	
	/**
	 * 触发器所属组的名字
	 */
	public void setTriggerGroup(java.lang.String triggerGroup) {
		set("trigger_group", triggerGroup);
	}
	
	/**
	 * 触发器所属组的名字
	 */
	public java.lang.String getTriggerGroup() {
		return getStr("trigger_group");
	}
	
	/**
	 * qrtz_job_details表job_name的外键
	 */
	public void setJobName(java.lang.String jobName) {
		set("job_name", jobName);
	}
	
	/**
	 * qrtz_job_details表job_name的外键
	 */
	public java.lang.String getJobName() {
		return getStr("job_name");
	}
	
	/**
	 * qrtz_job_details表job_group的外键
	 */
	public void setJobGroup(java.lang.String jobGroup) {
		set("job_group", jobGroup);
	}
	
	/**
	 * qrtz_job_details表job_group的外键
	 */
	public java.lang.String getJobGroup() {
		return getStr("job_group");
	}
	
	/**
	 * 相关介绍
	 */
	public void setDescription(java.lang.String description) {
		set("description", description);
	}
	
	/**
	 * 相关介绍
	 */
	public java.lang.String getDescription() {
		return getStr("description");
	}
	
	/**
	 * 上一次触发时间（毫秒）
	 */
	public void setNextFireTime(java.lang.Long nextFireTime) {
		set("next_fire_time", nextFireTime);
	}
	
	/**
	 * 上一次触发时间（毫秒）
	 */
	public java.lang.Long getNextFireTime() {
		return getLong("next_fire_time");
	}
	
	/**
	 * 下一次触发时间（默认为-1表示不触发）
	 */
	public void setPrevFireTime(java.lang.Long prevFireTime) {
		set("prev_fire_time", prevFireTime);
	}
	
	/**
	 * 下一次触发时间（默认为-1表示不触发）
	 */
	public java.lang.Long getPrevFireTime() {
		return getLong("prev_fire_time");
	}
	
	/**
	 * 优先级
	 */
	public void setPriority(java.lang.Integer priority) {
		set("priority", priority);
	}
	
	/**
	 * 优先级
	 */
	public java.lang.Integer getPriority() {
		return getInt("priority");
	}
	
	/**
	 * 触发器状态
	 */
	public void setTriggerState(java.lang.String triggerState) {
		set("trigger_state", triggerState);
	}
	
	/**
	 * 触发器状态
	 */
	public java.lang.String getTriggerState() {
		return getStr("trigger_state");
	}
	
	/**
	 * 触发器的类型
	 */
	public void setTriggerType(java.lang.String triggerType) {
		set("trigger_type", triggerType);
	}
	
	/**
	 * 触发器的类型
	 */
	public java.lang.String getTriggerType() {
		return getStr("trigger_type");
	}
	
	/**
	 * 开始时间
	 */
	public void setStartTime(java.lang.Long startTime) {
		set("start_time", startTime);
	}
	
	/**
	 * 开始时间
	 */
	public java.lang.Long getStartTime() {
		return getLong("start_time");
	}
	
	/**
	 * 结束时间
	 */
	public void setEndTime(java.lang.Long endTime) {
		set("end_time", endTime);
	}
	
	/**
	 * 结束时间
	 */
	public java.lang.Long getEndTime() {
		return getLong("end_time");
	}
	
	/**
	 * 日程表名称
	 */
	public void setCalendarName(java.lang.String calendarName) {
		set("calendar_name", calendarName);
	}
	
	/**
	 * 日程表名称
	 */
	public java.lang.String getCalendarName() {
		return getStr("calendar_name");
	}
	
	/**
	 * 补偿执行的策略
	 */
	public void setMisfireInstr(java.lang.Integer misfireInstr) {
		set("misfire_instr", misfireInstr);
	}
	
	/**
	 * 补偿执行的策略
	 */
	public java.lang.Integer getMisfireInstr() {
		return getInt("misfire_instr");
	}
	
	/**
	 * 存放持久化job对象
	 */
	public void setJobData(byte[] jobData) {
		set("job_data", jobData);
	}
	
	/**
	 * 存放持久化job对象
	 */
	public byte[] getJobData() {
		return get("job_data");
	}
	
}

