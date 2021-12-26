package com.ddam.spring.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(
		// 유니크 제약 조건(팔로우 중복을 막아주는 부분)
		uniqueConstraints = {
				@UniqueConstraint(
						name="follow_uk",
						columnNames = {"fromUserId","toCrewId"}
						)
		}
)
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 중간 테이블 생성됨.
	// fromUser가 toCrew를 following 함.
	
	@ManyToOne
	@JoinColumn(name="fromUserId")	 // 컬럼명을 이렇게 만들어라
	@JsonIgnoreProperties({"crews"})
	private User fromUser;
	
	@ManyToOne
	@JoinColumn(name="toCrewId")
//	@JsonIgnoreProperties({"images"})
	private Crew toCrew;
	
	
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;
}


