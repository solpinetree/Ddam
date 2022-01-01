package com.ddam.spring.domain;

import java.sql.Timestamp;
import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class Notification implements Comparator<Notification>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	Timestamp writetime;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	private String noti;


@Override
public int compare(Notification c1, Notification c2) { // 알림 정렬 위해 compare method를 만들어놓음
	long l1 = c1.getWritetime().getTime();
	long l2 = c2.getWritetime().getTime();
	
	if(l1 < l2)
		return 1;
	else
		return -1;
}
}
