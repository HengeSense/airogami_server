delimiter $$
drop procedure if exists test1;
 create procedure test1()
begin
 while 1 < 11 do
SELECT SLEEP(.3);
 update PLANE  set UPDATE_INC = (select max_value from (select max(UPDATE_INC) + 1 as max_value from PLANE) as tmp) WHERE PLANE_ID = 1;
commit;
end while;
end $$

delimiter $$
drop procedure if exists test2;
 create procedure test2()
begin
 while 1 < 11 do
SELECT SLEEP(.2);
 update PLANE  set UPDATE_INC = (select max_value from (select max(UPDATE_INC) + 1 as max_value from PLANE) as tmp) WHERE PLANE_ID = 1;
commit;
end while;
end $$

delimiter $$
drop procedure if exists test3;
 create procedure test3()
begin
 while 1 < 11 do
SELECT SLEEP(.1);
 update PLANE  set UPDATE_INC = (select max_value from (select max(UPDATE_INC) + 1 as max_value from PLANE) as tmp) WHERE PLANE_ID = 2;
commit;
end while;
end $$

delimiter $$
drop procedure if exists test3;
 create procedure test3()
begin
 while 1 < 11 do
SELECT SLEEP(.7);
 update PLANE  set UPDATE_INC = (select max_value from (select max(UPDATE_INC) + 1 as max_value from PLANE) as tmp) WHERE PLANE_ID = 2;
commit;
end while;
end $$

delimiter $$
drop procedure if exists test4;
 create procedure test4()
begin
 while 1 < 11 do
SELECT SLEEP(.3);
 update PLANE  set UPDATE_INC = (select max_value from (select max(UPDATE_INC) + 1 as max_value from PLANE) as tmp) WHERE PLANE_ID = 3;
commit;
end while;
end $$

delimiter $$
drop procedure if exists test5;
 create procedure test5()
begin
 while 1 < 11 do
SELECT SLEEP(.3);
 update PLANE  set UPDATE_INC = (select max_value from (select max(UPDATE_INC) + 1 as max_value from PLANE) as tmp) WHERE PLANE_ID = 8;
commit;
end while;
end $$




