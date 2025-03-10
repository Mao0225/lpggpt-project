package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSysUserPost<M extends BaseSysUserPost<M>> extends Model<M> implements IBean {

	/**
	 * 用户ID
	 */
	public void setUserId(java.lang.Long userId) {
		set("user_id", userId);
	}
	
	/**
	 * 用户ID
	 */
	public java.lang.Long getUserId() {
		return getLong("user_id");
	}
	
	/**
	 * 岗位ID
	 */
	public void setPostId(java.lang.Long postId) {
		set("post_id", postId);
	}
	
	/**
	 * 岗位ID
	 */
	public java.lang.Long getPostId() {
		return getLong("post_id");
	}
	
}

