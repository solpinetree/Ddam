package com.ddam.spring.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@DynamicInsert 
public class Ask_board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long abid;   
	
	@NonNull
	@NotNull  // NOT NULL
	@Column(length=50)
	private String subject;
	@NotNull
	@Column(columnDefinition = "TEXT")
	
	private String content;
	
	@Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP")
	private LocalDateTime regdate;
	
	@Column(columnDefinition = "integer default 0")
	private int view_cnt;
	
	@Column(columnDefinition = "TEXT")
	private String answer;
	
	@ManyToOne
	@JoinColumn(name = "Id")
	private User uid;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "board_id")
	@ToString.Exclude
	private List<Ask_file> askFiles = new ArrayList<>();
	
	

}
