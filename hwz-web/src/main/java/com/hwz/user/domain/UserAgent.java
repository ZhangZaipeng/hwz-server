package com.hwz.user.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.hwz.common.security.Principal;

public class UserAgent implements Principal {
	private Long userAgentId;			//用户登录编号
	
	private Long userId;				//用户编号
	
	private String loginName;			//登录名
	
	private String loginPwd;			//登录密码
	
	private String telephone;			//电话
	
	private String email;				//邮箱
	
	private Date lastLoginTime;	//最后登录时间
	
	private int loginCount;			//登录次数
	
	private short deleted;				//0 有效 1 无效
	
	private Timestamp createdAt;		//创建时间
	
	private Timestamp updatedAt;		//最后修改时间
	
	private String platform;			//登录平台
	
	private Long bizLastLoginTime;
	
	public Long getUserAgentId() {
		return userAgentId;
	}
	
	public void setUserAgentId(Long userAgentId) {
		this.userAgentId = userAgentId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getLoginPwd() {
		return loginPwd;
	}
	
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getLoginCount() {
		return loginCount;
	}
	
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}
	
	public short getDeleted() {
		return deleted;
	}
	
	public void setDeleted(short deleted) {
		this.deleted = deleted;
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	@Override
	public Serializable getIdentity() {
		return "USER:" + this.getUserAgentId();
	}
	
	@Override
	public Long getLastLoginTime() {
		return lastLoginTime.getTime();
	}
	
	@Override
	public Long getBizLastLoginTime() {
		return bizLastLoginTime;
	}

	public void setBizLastLoginTime(Long bizLastLoginTime) {
	    this.bizLastLoginTime = bizLastLoginTime;
	}
	
}
