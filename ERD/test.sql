drop table if exists follow;
drop table if exists crew;
drop table if exists user;
drop table if exists likes;
drop table if exists follow_request;
drop table if exists meetup;
drop table if exists meetup_user;
drop table if exists community_board;
drop table if exists community_comment;
drop table if exists community_file;
drop table if exists community_like;
drop table if exists ask_board;
drop table if exists ask_file;
drop table if exists notification;
drop table if exists crew_chat;
drop table if exists notice_board;
drop table if exists notice_file;

select * from meetup_user;



SELECT * FROM crew;
select * from user;
desc user;
insert into user (username, rold) values()


show tables;


select * from user;

select * from follow_request;


SET foreign_key_checks = 0;
SET foreign_key_checks = 1;



select * from ask_file;
select * from ask_board;

show tables;

select * from notification;

-- 1	admin@mail.com	F	관리자	$2a$10$brmUGn9NBEofvBAUiY7.cueF8FeKyQdbvDdcs7Af85NxMJmrYFr7a	000-111-2222	ROLE_ADMIN	admin
-- 2	admin@mail.com	F	유저	$2a$10$8cFT1JYztyHYjazrmQC68untNjp9hBG0W/QNY7yRfPS0E.DgOJwp.	000-111-2222	ROLE_USER	user