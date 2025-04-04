package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRanqibiaobangding<M extends BaseRanqibiaobangding<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	/**
	 * 燃气表编号
	 */
	public void setRanqibiaono(java.lang.String ranqibiaono) {
		set("ranqibiaono", ranqibiaono);
	}
	
	/**
	 * 燃气表编号
	 */
	public java.lang.String getRanqibiaono() {
		return getStr("ranqibiaono");
	}
	
	/**
	 * 气瓶编号，多个气瓶用-分割
	 */
	public void setQipingno(java.lang.String qipingno) {
		set("qipingno", qipingno);
	}
	
	/**
	 * 气瓶编号，多个气瓶用-分割
	 */
	public java.lang.String getQipingno() {
		return getStr("qipingno");
	}
	
	/**
	 * 扫描时间
	 */
	public void setSaomiaoshijian(java.lang.String saomiaoshijian) {
		set("saomiaoshijian", saomiaoshijian);
	}
	
	/**
	 * 扫描时间
	 */
	public java.lang.String getSaomiaoshijian() {
		return getStr("saomiaoshijian");
	}
	
	/**
	 * 扫描人
	 */
	public void setSaomiaoren(java.lang.String saomiaoren) {
		set("saomiaoren", saomiaoren);
	}
	
	/**
	 * 扫描人
	 */
	public java.lang.String getSaomiaoren() {
		return getStr("saomiaoren");
	}
	
	public void setBiaodushu(java.lang.String biaodushu) {
		set("biaodushu", biaodushu);
	}
	
	public java.lang.String getBiaodushu() {
		return getStr("biaodushu");
	}
	
	/**
	 * 绑定读数时间
	 */
	public void setBiaodushuriqi(java.lang.String biaodushuriqi) {
		set("biaodushuriqi", biaodushuriqi);
	}
	
	/**
	 * 绑定读数时间
	 */
	public java.lang.String getBiaodushuriqi() {
		return getStr("biaodushuriqi");
	}
	
	/**
	 * 10是安装；20是卸载
	 */
	public void setFlag(java.lang.Integer flag) {
		set("flag", flag);
	}
	
	/**
	 * 10是安装；20是卸载
	 */
	public java.lang.Integer getFlag() {
		return getInt("flag");
	}
	
	/**
	 * 状态预留
	 */
	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}
	
	/**
	 * 状态预留
	 */
	public java.lang.Integer getStatus() {
		return getInt("status");
	}
	
	public void setYuliu1(java.lang.String yuliu1) {
		set("yuliu1", yuliu1);
	}
	
	public java.lang.String getYuliu1() {
		return getStr("yuliu1");
	}
	
	public void setYuliu2(java.lang.String yuliu2) {
		set("yuliu2", yuliu2);
	}
	
	public java.lang.String getYuliu2() {
		return getStr("yuliu2");
	}
	
	public void setYuliu3(java.lang.String yuliu3) {
		set("yuliu3", yuliu3);
	}
	
	public java.lang.String getYuliu3() {
		return getStr("yuliu3");
	}
	
	public void setJingdu(java.lang.String jingdu) {
		set("jingdu", jingdu);
	}
	
	public java.lang.String getJingdu() {
		return getStr("jingdu");
	}
	
	public void setWeidu(java.lang.String weidu) {
		set("weidu", weidu);
	}
	
	public java.lang.String getWeidu() {
		return getStr("weidu");
	}
	
	public void setAddress(java.lang.String address) {
		set("address", address);
	}
	
	public java.lang.String getAddress() {
		return getStr("address");
	}
	
}

