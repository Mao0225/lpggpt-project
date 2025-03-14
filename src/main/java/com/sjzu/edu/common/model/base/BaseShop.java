package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseShop<M extends BaseShop<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	public void setWeightId(java.lang.Integer weightId) {
		set("weight_id", weightId);
	}
	
	public java.lang.Integer getWeightId() {
		return getInt("weight_id");
	}
	
	public void setShopName(java.lang.String shopName) {
		set("shop_name", shopName);
	}
	
	public java.lang.String getShopName() {
		return getStr("shop_name");
	}
	
	public void setShopId(java.lang.Integer shopId) {
		set("shop_id", shopId);
	}
	
	public java.lang.Integer getShopId() {
		return getInt("shop_id");
	}
	
	public void setShopTel(java.lang.String shopTel) {
		set("shop_tel", shopTel);
	}
	
	public java.lang.String getShopTel() {
		return getStr("shop_tel");
	}
	
	public void setAddress(java.lang.String address) {
		set("address", address);
	}
	
	public java.lang.String getAddress() {
		return getStr("address");
	}
	
}

