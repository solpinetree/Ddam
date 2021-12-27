package com.ddam.spring.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MeetupUser {
	
	   	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;
	    @ManyToOne
	    @JoinColumn(name = "meetup_id")
	    private Meetup meetup;
}

