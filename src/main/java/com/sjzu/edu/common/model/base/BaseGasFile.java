package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGasFile<M extends BaseGasFile<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	public void setGasNumber(java.lang.String gasNumber) {
		set("gas_number", gasNumber);
	}
	
	public java.lang.String getGasNumber() {
		return getStr("gas_number");
	}
	
	public void setValveBodyCode(java.lang.String valveBodyCode) {
		set("valve_body_code", valveBodyCode);
	}
	
	public java.lang.String getValveBodyCode() {
		return getStr("valve_body_code");
	}
	
	public void setFillingMedium(java.lang.String fillingMedium) {
		set("filling_medium", fillingMedium);
	}
	
	public java.lang.String getFillingMedium() {
		return getStr("filling_medium");
	}
	
	public void setMonitoringStation(java.lang.String monitoringStation) {
		set("monitoring_station", monitoringStation);
	}
	
	public java.lang.String getMonitoringStation() {
		return getStr("monitoring_station");
	}
	
	public void setFillingSpecification(java.lang.String fillingSpecification) {
		set("filling_specification", fillingSpecification);
	}
	
	public java.lang.String getFillingSpecification() {
		return getStr("filling_specification");
	}
	
	public void setGasSpecification(java.lang.String gasSpecification) {
		set("gas_specification", gasSpecification);
	}
	
	public java.lang.String getGasSpecification() {
		return getStr("gas_specification");
	}
	
	public void setGasSuttle(java.lang.String gasSuttle) {
		set("gas_suttle", gasSuttle);
	}
	
	public java.lang.String getGasSuttle() {
		return getStr("gas_suttle");
	}
	
	public void setGasImageUrl(java.lang.String gasImageUrl) {
		set("gas_image_url", gasImageUrl);
	}
	
	public java.lang.String getGasImageUrl() {
		return getStr("gas_image_url");
	}
	
	public void setManufactureDate(java.lang.String manufactureDate) {
		set("manufacture_date", manufactureDate);
	}
	
	public java.lang.String getManufactureDate() {
		return getStr("manufacture_date");
	}
	
	public void setInspectDate(java.util.Date inspectDate) {
		set("inspect_date", inspectDate);
	}
	
	public java.util.Date getInspectDate() {
		return getDate("inspect_date");
	}
	
	public void setRegularCheckDate(java.util.Date regularCheckDate) {
		set("regular_check_date", regularCheckDate);
	}
	
	public java.util.Date getRegularCheckDate() {
		return getDate("regular_check_date");
	}
	
	public void setTerminateUseDate(java.util.Date terminateUseDate) {
		set("terminate_use_date", terminateUseDate);
	}
	
	public java.util.Date getTerminateUseDate() {
		return getDate("terminate_use_date");
	}
	
	public void setGongchengyali(java.lang.String gongchengyali) {
		set("gongchengyali", gongchengyali);
	}
	
	public java.lang.String getGongchengyali() {
		return getStr("gongchengyali");
	}
	
	public void setShuishiyanya(java.lang.String shuishiyanya) {
		set("shuishiyanya", shuishiyanya);
	}
	
	public java.lang.String getShuishiyanya() {
		return getStr("shuishiyanya");
	}
	
	public void setRongji(java.lang.String rongji) {
		set("rongji", rongji);
	}
	
	public java.lang.String getRongji() {
		return getStr("rongji");
	}
	
	public void setBihou(java.lang.String bihou) {
		set("bihou", bihou);
	}
	
	public java.lang.String getBihou() {
		return getStr("bihou");
	}
	
	public void setShifouzhuanyong(java.lang.String shifouzhuanyong) {
		set("shifouzhuanyong", shifouzhuanyong);
	}
	
	public java.lang.String getShifouzhuanyong() {
		return getStr("shifouzhuanyong");
	}
	
	public void setQpsydjzbh(java.lang.String qpsydjzbh) {
		set("qpsydjzbh", qpsydjzbh);
	}
	
	public java.lang.String getQpsydjzbh() {
		return getStr("qpsydjzbh");
	}
	
	public void setFilingGasStation(java.lang.String filingGasStation) {
		set("filing_gas_station", filingGasStation);
	}
	
	public java.lang.String getFilingGasStation() {
		return getStr("filing_gas_station");
	}
	
	public void setJiandangshijian(java.lang.String jiandangshijian) {
		set("jiandangshijian", jiandangshijian);
	}
	
	public java.lang.String getJiandangshijian() {
		return getStr("jiandangshijian");
	}
	
	public void setManufactureUnit(java.lang.String manufactureUnit) {
		set("manufacture_unit", manufactureUnit);
	}
	
	public java.lang.String getManufactureUnit() {
		return getStr("manufacture_unit");
	}
	
	public void setXukezhenghao(java.lang.String xukezhenghao) {
		set("xukezhenghao", xukezhenghao);
	}
	
	public java.lang.String getXukezhenghao() {
		return getStr("xukezhenghao");
	}
	
	public void setGasstationid(java.lang.Integer gasstationid) {
		set("gasstationid", gasstationid);
	}
	
	public java.lang.Integer getGasstationid() {
		return getInt("gasstationid");
	}
	
	public void setStationid(java.lang.Integer stationid) {
		set("stationid", stationid);
	}
	
	public java.lang.Integer getStationid() {
		return getInt("stationid");
	}
	
	public void setFilingDate(java.lang.String filingDate) {
		set("filing_date", filingDate);
	}
	
	public java.lang.String getFilingDate() {
		return getStr("filing_date");
	}
	
	public void setValveBodyBluetooth(java.lang.String valveBodyBluetooth) {
		set("valve_body_bluetooth", valveBodyBluetooth);
	}
	
	public java.lang.String getValveBodyBluetooth() {
		return getStr("valve_body_bluetooth");
	}
	
	public void setFengtiangasno(java.lang.String fengtiangasno) {
		set("fengtiangasno", fengtiangasno);
	}
	
	public java.lang.String getFengtiangasno() {
		return getStr("fengtiangasno");
	}
	
	public void setLoukongno(java.lang.String loukongno) {
		set("loukongno", loukongno);
	}
	
	public java.lang.String getLoukongno() {
		return getStr("loukongno");
	}
	
	public void setInsuranceDate(java.util.Date insuranceDate) {
		set("insurance_date", insuranceDate);
	}
	
	public java.util.Date getInsuranceDate() {
		return getDate("insurance_date");
	}
	
	public void setGasImageOssId(java.lang.String gasImageOssId) {
		set("gas_image_oss_id", gasImageOssId);
	}
	
	public java.lang.String getGasImageOssId() {
		return getStr("gas_image_oss_id");
	}
	
	public void setShiyongzhemingcheng(java.lang.String shiyongzhemingcheng) {
		set("shiyongzhemingcheng", shiyongzhemingcheng);
	}
	
	public java.lang.String getShiyongzhemingcheng() {
		return getStr("shiyongzhemingcheng");
	}
	
	public void setShiyongzhedizhi(java.lang.String shiyongzhedizhi) {
		set("shiyongzhedizhi", shiyongzhedizhi);
	}
	
	public java.lang.String getShiyongzhedizhi() {
		return getStr("shiyongzhedizhi");
	}
	
	public void setShiyongzhedianhua(java.lang.String shiyongzhedianhua) {
		set("shiyongzhedianhua", shiyongzhedianhua);
	}
	
	public java.lang.String getShiyongzhedianhua() {
		return getStr("shiyongzhedianhua");
	}
	
	public void setShiyongzhejingdu(java.lang.String shiyongzhejingdu) {
		set("shiyongzhejingdu", shiyongzhejingdu);
	}
	
	public java.lang.String getShiyongzhejingdu() {
		return getStr("shiyongzhejingdu");
	}
	
	public void setShiyongzheweidu(java.lang.String shiyongzheweidu) {
		set("shiyongzheweidu", shiyongzheweidu);
	}
	
	public java.lang.String getShiyongzheweidu() {
		return getStr("shiyongzheweidu");
	}
	
}

