package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCheng<M extends BaseCheng<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	/**
	 * 秤的编号
	 */
	public void setBianhao(java.lang.String bianhao) {
		set("bianhao", bianhao);
	}
	
	/**
	 * 秤的编号
	 */
	public java.lang.String getBianhao() {
		return getStr("bianhao");
	}
	
	/**
	 * 生产日期
	 */
	public void setShengchanriqi(java.util.Date shengchanriqi) {
		set("shengchanriqi", shengchanriqi);
	}
	
	/**
	 * 生产日期
	 */
	public java.util.Date getShengchanriqi() {
		return getDate("shengchanriqi");
	}
	
	/**
	 * 生产厂家
	 */
	public void setShengchanchangjia(java.lang.String shengchanchangjia) {
		set("shengchanchangjia", shengchanchangjia);
	}
	
	/**
	 * 生产厂家
	 */
	public java.lang.String getShengchanchangjia() {
		return getStr("shengchanchangjia");
	}
	
	/**
	 * 价格
	 */
	public void setJiage(java.lang.String jiage) {
		set("jiage", jiage);
	}
	
	/**
	 * 价格
	 */
	public java.lang.String getJiage() {
		return getStr("jiage");
	}
	
	/**
	 * 图片
	 */
	public void setPicture(java.lang.String picture) {
		set("picture", picture);
	}
	
	/**
	 * 图片
	 */
	public java.lang.String getPicture() {
		return getStr("picture");
	}
	
	/**
	 * 被分配到哪家餐馆的id
	 */
	public void setRestaurantid(java.lang.Integer restaurantid) {
		set("restaurantid", restaurantid);
	}
	
	/**
	 * 被分配到哪家餐馆的id
	 */
	public java.lang.Integer getRestaurantid() {
		return getInt("restaurantid");
	}
	
	/**
	 * 备注
	 */
	public void setMemo(java.lang.String memo) {
		set("memo", memo);
	}
	
	/**
	 * 备注
	 */
	public java.lang.String getMemo() {
		return getStr("memo");
	}
	
	/**
	 * 称的状态，10正常，20维修，30空闲
	 */
	public void setFlag(java.lang.Integer flag) {
		set("flag", flag);
	}
	
	/**
	 * 称的状态，10正常，20维修，30空闲
	 */
	public java.lang.Integer getFlag() {
		return getInt("flag");
	}
	
}

