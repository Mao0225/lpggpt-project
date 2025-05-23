package com.sjzu.edu.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("auto_gas_filling_record", "id", AutoGasFillingRecord.class);
		arp.addMapping("bangdingren", "id", Bangdingren.class);
		arp.addMapping("bas_bill", "id", BasBill.class);
		arp.addMapping("basshexiangtouinfo", "id", Basshexiangtouinfo.class);
		arp.addMapping("bill_info", "id", BillInfo.class);
		arp.addMapping("bottle_person", "pid", BottlePerson.class);
		arp.addMapping("bse_company", "id", BseCompany.class);
		arp.addMapping("bse_company_copy1", "id", BseCompanyCopy1.class);
		arp.addMapping("bse_data", "id", BseData.class);
		arp.addMapping("bse_equip_operation", "id", BseEquipOperation.class);
		arp.addMapping("bse_equipment", "id", BseEquipment.class);
		arp.addMapping("bse_equipment_copy1", "id", BseEquipmentCopy1.class);
		arp.addMapping("bse_fitt_operate", "id", BseFittOperate.class);
		arp.addMapping("bse_fitting", "id", BseFitting.class);
		arp.addMapping("bse_gun", "id", BseGun.class);
		arp.addMapping("bse_interval", "id", BseInterval.class);
		arp.addMapping("bse_maintenance", "id", BseMaintenance.class);
		arp.addMapping("bse_maintenance_copy1", "id", BseMaintenanceCopy1.class);
		arp.addMapping("bse_xiaohezi", "id", BseXiaohezi.class);
		arp.addMapping("cheng", "id", Cheng.class);
		arp.addMapping("course", "id", Course.class);
		arp.addMapping("custromer", "id", Custromer.class);
		arp.addMapping("cylinder", "id", Cylinder.class);
		arp.addMapping("deviceipaddress", "id", Deviceipaddress.class);
		arp.addMapping("dianzibiaoqian", "id", Dianzibiaoqian.class);
		arp.addMapping("dianzicheng", "id", Dianzicheng.class);
		arp.addMapping("downcar", "id", Downcar.class);
		arp.addMapping("drivercar", "id", Drivercar.class);
		arp.addMapping("drivergpsinfo", "id", Drivergpsinfo.class);
		arp.addMapping("enter_station", "id", EnterStation.class);
		arp.addMapping("fill_record_check", "id", FillRecordCheck.class);
		arp.addMapping("fill_record_check1", "id", FillRecordCheck1.class);
		arp.addMapping("func", "id", Func.class);
		arp.addMapping("gas_bottle", "id", GasBottle.class);
		arp.addMapping("gas_file", "id", GasFile.class);
		arp.addMapping("gas_file123", "id", GasFile123.class);
		arp.addMapping("gas_file_copy1", "id", GasFileCopy1.class);
		arp.addMapping("gas_station", "id", GasStation.class);
		arp.addMapping("gas_station_staff", "id", GasStationStaff.class);
		arp.addMapping("gasbottleinstore", "id", Gasbottleinstore.class);
		arp.addMapping("gen_table", "table_id", GenTable.class);
		arp.addMapping("gen_table_column", "column_id", GenTableColumn.class);
		arp.addMapping("ipaddress", "id", Ipaddress.class);
		arp.addMapping("jiebangding", "id", Jiebangding.class);
		arp.addMapping("jieping", "id", Jieping.class);
		arp.addMapping("manage_xiaohezi", "id", ManageXiaohezi.class);
		arp.addMapping("ranqibiaobangding", "id", Ranqibiaobangding.class);
		arp.addMapping("restaurant", "id", Restaurant.class);
		arp.addMapping("shexiangtou", "id", Shexiangtou.class);
		arp.addMapping("shexiangtoulanya", "id", Shexiangtoulanya.class);
		arp.addMapping("shop", "id", Shop.class);
		arp.addMapping("staff", "staff_id", Staff.class);
		arp.addMapping("state_time", "state_id", StateTime.class);
		arp.addMapping("station", "id", Station.class);
		arp.addMapping("station_current", "id", StationCurrent.class);
		arp.addMapping("sys_config", "config_id", SysConfig.class);
		arp.addMapping("sys_dept", "dept_id", SysDept.class);
		arp.addMapping("sys_dict_data", "dict_code", SysDictData.class);
		arp.addMapping("sys_dict_type", "dict_id", SysDictType.class);
		arp.addMapping("sys_logininfor", "info_id", SysLogininfor.class);
		arp.addMapping("sys_menu", "menu_id", SysMenu.class);
		arp.addMapping("sys_notice", "notice_id", SysNotice.class);
		arp.addMapping("sys_oper_log", "oper_id", SysOperLog.class);
		arp.addMapping("sys_oss", "oss_id", SysOss.class);
		arp.addMapping("sys_oss_config", "oss_config_id", SysOssConfig.class);
		arp.addMapping("sys_post", "post_id", SysPost.class);
		arp.addMapping("sys_role", "role_id", SysRole.class);
		// Composite Primary Key order: dept_id,role_id
		arp.addMapping("sys_role_dept", "dept_id,role_id", SysRoleDept.class);
		// Composite Primary Key order: menu_id,role_id
		arp.addMapping("sys_role_menu", "menu_id,role_id", SysRoleMenu.class);
		arp.addMapping("sys_user", "user_id", SysUser.class);
		// Composite Primary Key order: post_id,user_id
		arp.addMapping("sys_user_post", "post_id,user_id", SysUserPost.class);
		// Composite Primary Key order: role_id,user_id
		arp.addMapping("sys_user_role", "role_id,user_id", SysUserRole.class);
		arp.addMapping("t_base_property", "id", BaseProperty.class);
		arp.addMapping("t_device_records", "id", DeviceRecords.class);
		arp.addMapping("t_pressure_gauge_records", "id", PressureGaugeRecords.class);
		arp.addMapping("t_pressure_gauge_records1", "id", PressureGaugeRecords1.class);
		arp.addMapping("t_pressure_value_records", "id", PressureValueRecords.class);
		arp.addMapping("t_product_records", "id", ProductRecords.class);
		arp.addMapping("t_scale_records", "id", ScaleRecords.class);
		arp.addMapping("t_successful_records", "id", SuccessfulRecords.class);
		arp.addMapping("transport", "id", Transport.class);
		arp.addMapping("uploaddianzibiaoqian", "id", Uploaddianzibiaoqian.class);
		arp.addMapping("use_detail", "id", UseDetail.class);
		arp.addMapping("user", "id", User.class);
		arp.addMapping("userfunction", "id", Userfunction.class);
		arp.addMapping("version", "id", Version.class);
	}
}


