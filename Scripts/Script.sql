select * from user;

call next value for hibernate_sequence;   -- AI 값 증가
insert into user (`u_id`,`username`,`password`, `name`,`gender`, `email`, `phone`, `auth`) values (1,'크루원1','1111', '이영진','남',
		'1111@redknight.com', '111-1111-1111', null);

call next value for hibernate_sequence;
insert into user (`u_id`,`username`,`password`, `name`,`gender`, `email`, `phone`, `auth`) values (2,'크루원2','2222', '이진영','여',
		'2222@redknight.com', '222-2222-2222', null);
		
call next value for hibernate_sequence;
insert into user (`u_id`,`username`,`password`, `name`,`gender`, `email`, `phone`, `auth`) values (3,'크루원3','3333', '김순자','여',
		'3333@redknight.com', '333-3333-3333', null);
		
call next value for hibernate_sequence;
insert into user (`u_id`,`username`,`password`, `name`,`gender`, `email`, `phone`, `auth`) values (4,'크루원4','444', '김현중','남',
		'4444@redknight.com', '444-4444-4444', null);


