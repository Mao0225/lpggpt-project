package com.sjzu.edu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSysOssConfig<M extends BaseSysOssConfig<M>> extends Model<M> implements IBean {

	/**
	 * 主建
	 */
	public void setOssConfigId(java.lang.Long ossConfigId) {
		set("oss_config_id", ossConfigId);
	}
	
	/**
	 * 主建
	 */
	public java.lang.Long getOssConfigId() {
		return getLong("oss_config_id");
	}
	
	/**
	 * 配置key
	 */
	public void setConfigKey(java.lang.String configKey) {
		set("config_key", configKey);
	}
	
	/**
	 * 配置key
	 */
	public java.lang.String getConfigKey() {
		return getStr("config_key");
	}
	
	/**
	 * accessKey
	 */
	public void setAccessKey(java.lang.String accessKey) {
		set("access_key", accessKey);
	}
	
	/**
	 * accessKey
	 */
	public java.lang.String getAccessKey() {
		return getStr("access_key");
	}
	
	/**
	 * 秘钥
	 */
	public void setSecretKey(java.lang.String secretKey) {
		set("secret_key", secretKey);
	}
	
	/**
	 * 秘钥
	 */
	public java.lang.String getSecretKey() {
		return getStr("secret_key");
	}
	
	/**
	 * 桶名称
	 */
	public void setBucketName(java.lang.String bucketName) {
		set("bucket_name", bucketName);
	}
	
	/**
	 * 桶名称
	 */
	public java.lang.String getBucketName() {
		return getStr("bucket_name");
	}
	
	/**
	 * 前缀
	 */
	public void setPrefix(java.lang.String prefix) {
		set("prefix", prefix);
	}
	
	/**
	 * 前缀
	 */
	public java.lang.String getPrefix() {
		return getStr("prefix");
	}
	
	/**
	 * 访问站点
	 */
	public void setEndpoint(java.lang.String endpoint) {
		set("endpoint", endpoint);
	}
	
	/**
	 * 访问站点
	 */
	public java.lang.String getEndpoint() {
		return getStr("endpoint");
	}
	
	/**
	 * 自定义域名
	 */
	public void setDomain(java.lang.String domain) {
		set("domain", domain);
	}
	
	/**
	 * 自定义域名
	 */
	public java.lang.String getDomain() {
		return getStr("domain");
	}
	
	/**
	 * 是否https（Y=是,N=否）
	 */
	public void setIsHttps(java.lang.String isHttps) {
		set("is_https", isHttps);
	}
	
	/**
	 * 是否https（Y=是,N=否）
	 */
	public java.lang.String getIsHttps() {
		return getStr("is_https");
	}
	
	/**
	 * 域
	 */
	public void setRegion(java.lang.String region) {
		set("region", region);
	}
	
	/**
	 * 域
	 */
	public java.lang.String getRegion() {
		return getStr("region");
	}
	
	/**
	 * 桶权限类型(0=private 1=public 2=custom)
	 */
	public void setAccessPolicy(java.lang.String accessPolicy) {
		set("access_policy", accessPolicy);
	}
	
	/**
	 * 桶权限类型(0=private 1=public 2=custom)
	 */
	public java.lang.String getAccessPolicy() {
		return getStr("access_policy");
	}
	
	/**
	 * 状态（0=正常,1=停用）
	 */
	public void setStatus(java.lang.String status) {
		set("status", status);
	}
	
	/**
	 * 状态（0=正常,1=停用）
	 */
	public java.lang.String getStatus() {
		return getStr("status");
	}
	
	/**
	 * 扩展字段
	 */
	public void setExt1(java.lang.String ext1) {
		set("ext1", ext1);
	}
	
	/**
	 * 扩展字段
	 */
	public java.lang.String getExt1() {
		return getStr("ext1");
	}
	
	/**
	 * 创建者
	 */
	public void setCreateBy(java.lang.String createBy) {
		set("create_by", createBy);
	}
	
	/**
	 * 创建者
	 */
	public java.lang.String getCreateBy() {
		return getStr("create_by");
	}
	
	/**
	 * 创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	
	/**
	 * 创建时间
	 */
	public java.util.Date getCreateTime() {
		return getDate("create_time");
	}
	
	/**
	 * 更新者
	 */
	public void setUpdateBy(java.lang.String updateBy) {
		set("update_by", updateBy);
	}
	
	/**
	 * 更新者
	 */
	public java.lang.String getUpdateBy() {
		return getStr("update_by");
	}
	
	/**
	 * 更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
	}
	
	/**
	 * 更新时间
	 */
	public java.util.Date getUpdateTime() {
		return getDate("update_time");
	}
	
	/**
	 * 备注
	 */
	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}
	
	/**
	 * 备注
	 */
	public java.lang.String getRemark() {
		return getStr("remark");
	}
	
}

