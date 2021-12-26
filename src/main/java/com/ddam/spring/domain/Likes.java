package com.ddam.spring.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "fromUserId")
	@JsonIgnoreProperties({"crews",  "createDate", "updateDate"})
	private User fromUser; // id, username, name
	
	@ManyToOne
	@JoinColumn(name = "toCrewId")
	@JsonIgnoreProperties({ "crewAdmin", "likes"})
	private Crew toCrew; // 기본 :  id
	
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;
}


