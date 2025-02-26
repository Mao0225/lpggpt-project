package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGasFile<M extends BaseGasFile<M>> extends Model<M> implements IBean {

	/**
	 * ID
	 */
	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	/**
	 * ID
	 */
	public java.lang.Integer getId() {
		return getInt("id");
	}
	
	/**
	 * 气瓶编号
	 */
	public void setGasNumber(java.lang.String gasNumber) {
		set("gas_number", gasNumber);
	}
	
	/**
	 * 气瓶编号
	 */
	public java.lang.String getGasNumber() {
		return getStr("gas_number");
	}
	
	/**
	 * 奉天瓶体编码
	 */
	public void setFengtiangasno(java.lang.String fengtiangasno) {
		set("fengtiangasno", fengtiangasno);
	}
	
	/**
	 * 奉天瓶体编码
	 */
	public java.lang.String getFengtiangasno() {
		return getStr("fengtiangasno");
	}
	
	/**
	 * 镂空码
	 */
	public void setLoukongno(java.lang.String loukongno) {
		set("loukongno", loukongno);
	}
	
	/**
	 * 镂空码
	 */
	public java.lang.String getLoukongno() {
		return getStr("loukongno");
	}
	
	/**
	 * 所属于的气站id,关联gasstation表id字段
	 */
	public void setGasstationid(java.lang.Integer gasstationid) {
		set("gasstationid", gasstationid);
	}
	
	/**
	 * 所属于的气站id,关联gasstation表id字段
	 */
	public java.lang.Integer getGasstationid() {
		return getInt("gasstationid");
	}
	
	/**
	 * 建档气站名字
	 */
	public void setFilingGasStation(java.lang.String filingGasStation) {
		set("filing_gas_station", filingGasStation);
	}
	
	/**
	 * 建档气站名字
	 */
	public java.lang.String getFilingGasStation() {
		return getStr("filing_gas_station");
	}
	
	/**
	 * 电子标签
	 */
	public void setValveBodyCode(java.lang.String valveBodyCode) {
		set("valve_body_code", valveBodyCode);
	}
	
	/**
	 * 电子标签
	 */
	public java.lang.String getValveBodyCode() {
		return getStr("valve_body_code");
	}
	
	/**
	 * 阀体蓝牙
	 */
	public void setValveBodyBluetooth(java.lang.String valveBodyBluetooth) {
		set("valve_body_bluetooth", valveBodyBluetooth);
	}
	
	/**
	 * 阀体蓝牙
	 */
	public java.lang.String getValveBodyBluetooth() {
		return getStr("valve_body_bluetooth");
	}
	
	/**
	 * 制造单位
	 */
	public void setManufactureUnit(java.lang.String manufactureUnit) {
		set("manufacture_unit", manufactureUnit);
	}
	
	/**
	 * 制造单位
	 */
	public java.lang.String getManufactureUnit() {
		return getStr("manufacture_unit");
	}
	
	/**
	 * 制造日期
	 */
	public void setManufactureDate(java.lang.String manufactureDate) {
		set("manufacture_date", manufactureDate);
	}
	
	/**
	 * 制造日期
	 */
	public java.lang.String getManufactureDate() {
		return getStr("manufacture_date");
	}
	
	/**
	 * 检验日期
	 */
	public void setInspectDate(java.util.Date inspectDate) {
		set("inspect_date", inspectDate);
	}
	
	/**
	 * 检验日期
	 */
	public java.util.Date getInspectDate() {
		return getDate("inspect_date");
	}
	
	/**
	 * 定检日期
	 */
	public void setRegularCheckDate(java.util.Date regularCheckDate) {
		set("regular_check_date", regularCheckDate);
	}
	
	/**
	 * 定检日期
	 */
	public java.util.Date getRegularCheckDate() {
		return getDate("regular_check_date");
	}
	
	/**
	 * 终止使用日期
	 */
	public void setTerminateUseDate(java.util.Date terminateUseDate) {
		set("terminate_use_date", terminateUseDate);
	}
	
	/**
	 * 终止使用日期
	 */
	public java.util.Date getTerminateUseDate() {
		return getDate("terminate_use_date");
	}
	
	/**
	 * 设备品种
	 */
	public void setMonitoringStation(java.lang.String monitoringStation) {
		set("monitoring_station", monitoringStation);
	}
	
	/**
	 * 设备品种
	 */
	public java.lang.String getMonitoringStation() {
		return getStr("monitoring_station");
	}
	
	/**
	 * 充装规格
	 */
	public void setFillingSpecification(java.lang.String fillingSpecification) {
		set("filling_specification", fillingSpecification);
	}
	
	/**
	 * 充装规格
	 */
	public java.lang.String getFillingSpecification() {
		return getStr("filling_specification");
	}
	
	/**
	 * 气瓶型号
	 */
	public void setGasSpecification(java.lang.String gasSpecification) {
		set("gas_specification", gasSpecification);
	}
	
	/**
	 * 气瓶型号
	 */
	public java.lang.String getGasSpecification() {
		return getStr("gas_specification");
	}
	
	/**
	 * 充装介质
	 */
	public void setFillingMedium(java.lang.String fillingMedium) {
		set("filling_medium", fillingMedium);
	}
	
	/**
	 * 充装介质
	 */
	public java.lang.String getFillingMedium() {
		return getStr("filling_medium");
	}
	
	/**
	 * 气瓶净重
	 */
	public void setGasSuttle(java.lang.String gasSuttle) {
		set("gas_suttle", gasSuttle);
	}
	
	/**
	 * 气瓶净重
	 */
	public java.lang.String getGasSuttle() {
		return getStr("gas_suttle");
	}
	
	/**
	 * 投保日期
	 */
	public void setInsuranceDate(java.util.Date insuranceDate) {
		set("insurance_date", insuranceDate);
	}
	
	/**
	 * 投保日期
	 */
	public java.util.Date getInsuranceDate() {
		return getDate("insurance_date");
	}
	
	/**
	 * 建档日期
	 */
	public void setFilingDate(java.lang.String filingDate) {
		set("filing_date", filingDate);
	}
	
	/**
	 * 建档日期
	 */
	public java.lang.String getFilingDate() {
		return getStr("filing_date");
	}
	
	/**
	 * 车辆照片ossid
	 */
	public void setGasImageOssId(java.lang.String gasImageOssId) {
		set("gas_image_oss_id", gasImageOssId);
	}
	
	/**
	 * 车辆照片ossid
	 */
	public java.lang.String getGasImageOssId() {
		return getStr("gas_image_oss_id");
	}
	
	public void setGasImageUrl(java.lang.String gasImageUrl) {
		set("gas_image_url", gasImageUrl);
	}
	
	public java.lang.String getGasImageUrl() {
		return getStr("gas_image_url");
	}
	
	/**
	 * 气瓶使用登记证编号
	 */
	public void setQpsydjzbh(java.lang.String qpsydjzbh) {
		set("qpsydjzbh", qpsydjzbh);
	}
	
	/**
	 * 气瓶使用登记证编号
	 */
	public java.lang.String getQpsydjzbh() {
		return getStr("qpsydjzbh");
	}
	
	/**
	 * 建档时间
	 */
	public void setJiandangshijian(java.lang.String jiandangshijian) {
		set("jiandangshijian", jiandangshijian);
	}
	
	/**
	 * 建档时间
	 */
	public java.lang.String getJiandangshijian() {
		return getStr("jiandangshijian");
	}
	
	/**
	 * 是否专用
	 */
	public void setShifouzhuanyong(java.lang.String shifouzhuanyong) {
		set("shifouzhuanyong", shifouzhuanyong);
	}
	
	/**
	 * 是否专用
	 */
	public java.lang.String getShifouzhuanyong() {
		return getStr("shifouzhuanyong");
	}
	
	/**
	 * 许可证号
	 */
	public void setXukezhenghao(java.lang.String xukezhenghao) {
		set("xukezhenghao", xukezhenghao);
	}
	
	/**
	 * 许可证号
	 */
	public java.lang.String getXukezhenghao() {
		return getStr("xukezhenghao");
	}
	
	/**
	 * 公称压力
	 */
	public void setGongchengyali(java.lang.String gongchengyali) {
		set("gongchengyali", gongchengyali);
	}
	
	/**
	 * 公称压力
	 */
	public java.lang.String getGongchengyali() {
		return getStr("gongchengyali");
	}
	
	/**
	 * 水试验压
	 */
	public void setShuishiyanya(java.lang.String shuishiyanya) {
		set("shuishiyanya", shuishiyanya);
	}
	
	/**
	 * 水试验压
	 */
	public java.lang.String getShuishiyanya() {
		return getStr("shuishiyanya");
	}
	
	/**
	 * 容积
	 */
	public void setRongji(java.lang.String rongji) {
		set("rongji", rongji);
	}
	
	/**
	 * 容积
	 */
	public java.lang.String getRongji() {
		return getStr("rongji");
	}
	
	/**
	 * 壁厚
	 */
	public void setBihou(java.lang.String bihou) {
		set("bihou", bihou);
	}
	
	/**
	 * 壁厚
	 */
	public java.lang.String getBihou() {
		return getStr("bihou");
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
	
	public void setStationid(java.lang.Integer stationid) {
		set("stationid", stationid);
	}
	
	public java.lang.Integer getStationid() {
		return getInt("stationid");
	}
	
}

