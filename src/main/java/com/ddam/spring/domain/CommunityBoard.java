package com.ddam.spring.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommunityBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cb_id")
	private Long id;
	
	@NonNull
	private String username;
	
	@NonNull
	private String subject;
	
	@NonNull
	@Column(columnDefinition = "text")
	private String content;
	
	@Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;
	
	@Column(columnDefinition = "integer default 0")
	private int viewCnt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "community_board_cb_id")
	@ToString.Exclude
	private List<CommunityFile> communityFiles = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "community_board_cb_id")
	@ToString.Exclude
	private List<CommunityLike> communityLikes = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "community_board_cb_id")
	@ToString.Exclude
	private List<CommunityComment> communityComments = new ArrayList<>();
	
}
