package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStaff<M extends BaseStaff<M>> extends Model<M> implements IBean {

	public void setStaffId(java.lang.Integer staffId) {
		set("staff_id", staffId);
	}
	
	public java.lang.Integer getStaffId() {
		return getInt("staff_id");
	}
	
	/**
	 * 工作人员
	 */
	public void setStaffName(java.lang.String staffName) {
		set("staff_name", staffName);
	}
	
	/**
	 * 工作人员
	 */
	public java.lang.String getStaffName() {
		return getStr("staff_name");
	}
	
}

