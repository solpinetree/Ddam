package com.ddam.spring.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data //lombok
@Entity //JPA -> ORM
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 시퀀스
	
	private String username; // 사용자 아이디
//	private String password; // 암호화된 패스워드
//	private String name; // 사용자 닉네임
	
//	@OneToMany(fetch= FetchType.EAGER)
//	@JoinColumn(name ="userId")
//	@JsonIgnoreProperties({"crewAdmin", "likes", "members"})
//	private List<Crew> crews;
	
//	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
//	private List<FollowRequest> followRequests;
	
	
	@CreationTimestamp // 자동으로 현재 시간이 세팅
	private Timestamp createDate;
	@CreationTimestamp // 자동으로 현재 시간이 세팅
	private Timestamp updateDate;
}


