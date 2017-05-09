package com.hwz.user.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.hwz.common.security.Principal;

public class StaffAgent implements Principal {
	
	private Long staffAgentId;			//自增长主键
	
	private String staffCode;				//员工工号
	
	private String telephone;				//员工电话
	
	private String email;					//员工邮箱
	
	private String realName;				//员工真实姓名
	
	private String loginPwd;				//登录密码
	
	private Integer roleId;				//员工隶属角色
	
	private Short  staffStatus;			//审核状态 0=未审核 / 1=审核通过 / 2=审核拒绝
	
	private Date lastLoginTime;		//最后登录时间
	
	private Integer loginCount;			//登录次数
	
	private Timestamp createdAt;			//创建时间
	
	private Timestamp updatedAt;			//最后修改时间
	
	private Long bizLastLoginTime;
	
	public Long getStaffAgentId() {
		return staffAgentId;
	}
	
	public void setStaffAgentId(Long staffAgentId) {
		this.staffAgentId = staffAgentId;
	}
	
	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
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
	
	public String getRealName() {
		return realName;
	}
	
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getLoginPwd() {
		return loginPwd;
	}
	
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	
	public Integer getRoleId() {
		return roleId;
	}
	
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	public Short getStaffStatus() {
		return staffStatus;
	}
	
	public void setStaffStatus(Short staffStatus) {
		this.staffStatus = staffStatus;
	}
	
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	public Integer getLoginCount() {
		return loginCount;
	}
	
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
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
	
	@Override
	public Serializable getIdentity() {
		// TODO Auto-generated method stub
		return "STAFF:" + this.getStaffAgentId();
	}
	
	@Override
	public Long getLastLoginTime() {
		// TODO Auto-generated method stub
		return this.lastLoginTime.getTime();
	}
	
	@Override
	public String getLoginName() {
		// TODO Auto-generated method stub
		return staffCode;
	}
	
	@Override
	public Long getBizLastLoginTime() {
		// TODO Auto-generated method stub
		return bizLastLoginTime;
	}
	
	public void setBizLastLoginTime(Long bizLastLoginTime) {
		this.bizLastLoginTime = bizLastLoginTime;
	}

	
}
