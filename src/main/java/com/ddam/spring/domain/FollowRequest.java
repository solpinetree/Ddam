package com.ddam.spring.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@org.hibernate.annotations.DynamicUpdate
public class FollowRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne
	@JoinColumn(name = "fromUserId")
	User fromUser;

	@ManyToOne
	@JoinColumn(name = "toCrewId")
	Crew toCrew;
	
	@Column(nullable = false, length=1000000)
	String info;  // 지원동기

}
