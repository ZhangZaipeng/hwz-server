package com.hwz.user.domain;

import java.sql.Timestamp;
import java.util.Date;

public class User {
	
	private Long userId;			//自增长主键
	
	private String nickName;		//昵称
	
	private String gender;			//男/女
	
	private String mobile;			//手机号
	
	private String iconImgUrl;	//头像
	
	private Date birthdate;			//出生日期
	
	private Timestamp createdAt;	//创建时间
	
	private Timestamp updatedAt;	//最后修改时间

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIconImgUrl() {
		return iconImgUrl;
	}

	public void setIconImgUrl(String iconImgUrl) {
		this.iconImgUrl = iconImgUrl;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
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
	
}
