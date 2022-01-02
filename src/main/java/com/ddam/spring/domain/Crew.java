package com.ddam.spring.domain;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // lombok
@Entity // JPA -> ORM
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@EntityListeners(AuditingEntityListener.class) /* JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알립니다. */
public class Crew {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30, unique = true, nullable = false)
	private String name; // 이름

	private String category; // 크루 카테고리

	private String area; // 크루 활동 지역
	
//	@OneToMany(mappedBy="crew")
//	private Set<Meetup> meetupLists = new HashSet<>();
	
   // 크루 썸네일
	private String fileOriginName; // 받아올 때 파일 이름
	private String fileName;	// 저장할 때 파일 이름
	private String filePath;	// 저장 및 불러올 경로

	@Column(columnDefinition = "LONGTEXT")
	private String description; // 크루 소개

	private Long memberLimit; // 크루 멤버 인원 제한수
	
//	@ColumnDefault("0")
	private Long memberCount; // 크루 멤버 수
	
	private Long likesCount; // 좋아요 수
	
//	@OneToMany
//	@JoinColumn(name ="crewId")
//	@JsonIgnoreProperties({"crews"})
//	private Set<User> members;

//	@OneToMany
//	@JoinColumn(name ="followReqeustId")
//	@JsonIgnoreProperties({"crews"})
//	private List<User> requests;
	
	// (1) Like List
//	@OneToMany(mappedBy = "crew")
//	private List<Likes> likes = new ArrayList<>();

	
	@ManyToOne
	@JoinColumn(name="adminId")
//	@JsonIgnoreProperties({"crews"})
	private User crewAdmin;	// 크루장

	
	//@Transient // DB에 영향을 미치지 않는다.

    
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;

    

    
}
