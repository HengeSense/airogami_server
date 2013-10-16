/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     10/10/2013 2:45:41 PM                        */
/*==============================================================*/


drop table if exists ACCOUNT;

drop table if exists ACCOUNT_STAT;

drop table if exists ACCOUNT_SYS;

drop table if exists AGENT;

drop index EMAIL_UNIQUE on AUTHENTICATE;

drop index SCREENNAME_UNIQUE on AUTHENTICATE;

drop index ACCOUNT_ID_PK on AUTHENTICATE;

drop table if exists AUTHENTICATE;

drop table if exists CATEGORY;

drop table if exists CHAIN;

drop table if exists CHAIN_HIST;

drop index UPDATE_INC_INDEX on CHAIN_MESSAGE;

drop index CREATEDTIME_INDEX on CHAIN_MESSAGE;

drop table if exists CHAIN_MESSAGE;

drop index CREATEDTIME_INDEX on MESSAGE;

drop table if exists MESSAGE;

drop index OWNER_INC_INDEX on PLANE;

drop index TARGET_INC_INDEX on PLANE;

drop table if exists PLANE;

drop table if exists PLANE_HIST;

drop index SCREENNAME_UNIQUE on PROFILE;

drop table if exists PROFILE;

drop table if exists REPORT;

/*==============================================================*/
/* Table: ACCOUNT                                               */
/*==============================================================*/
create table ACCOUNT
(
   ACCOUNT_ID           int not null,
   UPDATE_COUNT         int not null default 0,
   primary key (ACCOUNT_ID)
);

/*==============================================================*/
/* Table: ACCOUNT_STAT                                          */
/*==============================================================*/
create table ACCOUNT_STAT
(
   ACCOUNT_ID           int not null,
   SIGNIN_COUNT         int not null default 0,
   STATUS               tinyint not null default 0,
   MSG_COUNT            int not null default 0,
   CHAIN_MSG_COUNT      int not null default 0,
   PICKUP_LEFT_COUNT    smallint not null default 0,
   SEND_LEFT_COUNT      smallint not null default 0,
   PICKUP_COUNT         smallint not null default 0,
   SEND_COUNT           smallint not null default 0,
   primary key (ACCOUNT_ID)
);

/*==============================================================*/
/* Table: ACCOUNT_SYS                                           */
/*==============================================================*/
create table ACCOUNT_SYS
(
   ACCOUNT_ID           int not null,
   CHAIN_INC            bigint not null default -9223372036854775808,
   PLANE_INC            bigint not null default -9223372036854775808,
   primary key (ACCOUNT_ID)
);

/*==============================================================*/
/* Table: AGENT                                                 */
/*==============================================================*/
create table AGENT
(
   ACCOUNT_ID           int not null,
   DEV_TYPE             tinyint not null default 0,
   DEV_VERSION          tinyint not null default 0,
   DEV_TOKEN            varchar(4096),
   primary key (ACCOUNT_ID)
);

/*==============================================================*/
/* Table: AUTHENTICATE                                          */
/*==============================================================*/
create table AUTHENTICATE
(
   ACCOUNT_ID           int not null auto_increment,
   EMAIL                varchar(255) not null,
   SCREEN_NAME          varchar(32),
   PASSWORD             varchar(20) not null,
   primary key (ACCOUNT_ID)
);

/*==============================================================*/
/* Index: ACCOUNT_ID_PK                                         */
/*==============================================================*/
create unique index ACCOUNT_ID_PK on AUTHENTICATE
(
   ACCOUNT_ID
);

/*==============================================================*/
/* Index: SCREENNAME_UNIQUE                                     */
/*==============================================================*/
create unique index SCREENNAME_UNIQUE on AUTHENTICATE
(
   SCREEN_NAME
);

/*==============================================================*/
/* Index: EMAIL_UNIQUE                                          */
/*==============================================================*/
create unique index EMAIL_UNIQUE on AUTHENTICATE
(
   EMAIL
);

/*==============================================================*/
/* Table: CATEGORY                                              */
/*==============================================================*/
create table CATEGORY
(
   CATEGORY_ID          smallint not null auto_increment,
   NAME                 varchar(255) not null,
   DESCRIPTION          varchar(255) not null,
   primary key (CATEGORY_ID)
);

/*==============================================================*/
/* Table: CHAIN                                                 */
/*==============================================================*/
create table CHAIN
(
   CHAIN_ID             bigint not null auto_increment,
   ACCOUNT_ID           int not null,
   UPDATE_COUNT         int not null default 0,
   UPDATED_TIME         datetime not null,
   CREATED_TIME         datetime not null,
   STATUS               tinyint not null default 0,
   CITY                 varchar(255),
   PROVINCE             varchar(255),
   LONGITUDE            decimal(10,6) default 0.0,
   LATITUDE             decimal(10,6) default 0.0,
   SEX                  tinyint not null,
   COUNTRY              varchar(255),
   PASS_COUNT           smallint not null default 0,
   MATCH_COUNT          smallint not null default 0,
   MAX_PASS_COUNT       smallint not null default 0,
   MAX_MATCH_COUNT      smallint not null default 0,
   BIRTHDAY_LOWER       date,
   BIRTHDAY_UPPER       date,
   LANGUAGE             varchar(255),
   primary key (CHAIN_ID)
);

/*==============================================================*/
/* Table: CHAIN_HIST                                            */
/*==============================================================*/
create table CHAIN_HIST
(
   ACCOUNT_ID           int not null,
   CHAIN_ID             bigint not null,
   primary key (ACCOUNT_ID, CHAIN_ID)
);

/*==============================================================*/
/* Table: CHAIN_MESSAGE                                         */
/*==============================================================*/
create table CHAIN_MESSAGE
(
   CHAIN_ID             bigint not null,
   ACCOUNT_ID           int not null,
   UPDATE_INC           bigint not null default -9223372036854775808,
   CONTENT              varchar(255),
   TYPE                 tinyint,
   CREATED_TIME         datetime not null,
   STATUS               tinyint not null default 0,
   LAST_VIEWED_TIME     datetime not null,
   SOURCE               tinyint not null default 0,
   LAST_TIME            datetime,
   primary key (CHAIN_ID, ACCOUNT_ID)
);

/*==============================================================*/
/* Index: CREATEDTIME_INDEX                                     */
/*==============================================================*/
create index CREATEDTIME_INDEX on CHAIN_MESSAGE
(
   CREATED_TIME
);

/*==============================================================*/
/* Index: UPDATE_INC_INDEX                                      */
/*==============================================================*/
create index UPDATE_INC_INDEX on CHAIN_MESSAGE
(
   UPDATE_INC
);

/*==============================================================*/
/* Table: MESSAGE                                               */
/*==============================================================*/
create table MESSAGE
(
   MESSAGE_ID           bigint not null auto_increment,
   PLANE_ID             bigint not null,
   ACCOUNT_ID           int not null,
   TYPE                 tinyint not null,
   CREATED_TIME         datetime not null,
   STATUS               tinyint not null default 0,
   CONTENT              varchar(255) not null,
   primary key (MESSAGE_ID)
);

/*==============================================================*/
/* Index: CREATEDTIME_INDEX                                     */
/*==============================================================*/
create index CREATEDTIME_INDEX on MESSAGE
(
   CREATED_TIME
);

/*==============================================================*/
/* Table: PLANE                                                 */
/*==============================================================*/
create table PLANE
(
   PLANE_ID             bigint not null auto_increment,
   OWNER_INC            bigint not null default -9223372036854775808,
   CATEGORY_ID          smallint not null,
   OWNER_ID             int,
   TARGET_ID            int,
   UPDATE_COUNT         int not null default 0,
   UPDATED_TIME         datetime not null,
   CREATED_TIME         datetime not null,
   TARGET_INC           bigint not null default -9223372036854775808,
   STATUS               tinyint not null default 0,
   LONGITUDE            decimal(10,6) default 0.0,
   LATITUDE             decimal(10,6) default 0.0,
   LAST_MSG_ID_OF_T     bigint not null default 0,
   LAST_MSG_ID_OF_O     bigint not null default 0,
   CITY                 varchar(255),
   PROVINCE             varchar(255),
   COUNTRY              varchar(255),
   SEX                  tinyint not null,
   MATCH_COUNT          smallint not null default 0,
   MAX_MATCH_COUNT      smallint not null default 0,
   LIKED_BY_O           tinyint not null default 0,
   LIKED_BY_T           tinyint not null default 0,
   DELETED_BY_O         tinyint not null default 0,
   DELETED_BY_T         tinyint not null default 0,
   BIRTHDAY_LOWER       date,
   BIRTHDAY_UPPER       date,
   LANGUAGE             varchar(255),
   SOURCE               tinyint not null default 0,
   LAST_MSG_ID          bigint,
   primary key (PLANE_ID)
);

/*==============================================================*/
/* Index: TARGET_INC_INDEX                                      */
/*==============================================================*/
create index TARGET_INC_INDEX on PLANE
(
   TARGET_INC
);

/*==============================================================*/
/* Index: OWNER_INC_INDEX                                       */
/*==============================================================*/
create index OWNER_INC_INDEX on PLANE
(
   OWNER_ID
);

/*==============================================================*/
/* Table: PLANE_HIST                                            */
/*==============================================================*/
create table PLANE_HIST
(
   PLANE_ID             bigint not null,
   ACCOUNT_ID           int not null,
   primary key (PLANE_ID, ACCOUNT_ID)
);

/*==============================================================*/
/* Table: PROFILE                                               */
/*==============================================================*/
create table PROFILE
(
   ACCOUNT_ID           int not null,
   FULL_NAME            varchar(70) not null,
   SCREEN_NAME          varchar(32),
   SEX                  tinyint not null,
   LONGITUDE            decimal(10,6) not null default 0.0,
   LATITUDE             decimal(10,6) not null default 0.0,
   STATUS               tinyint not null default 0,
   CREATED_TIME         datetime not null,
   CITY                 varchar(255) not null,
   PROVINCE             varchar(255) not null,
   COUNTRY              varchar(255) not null,
   BIRTHDAY             date not null,
   UPDATE_COUNT         int not null default 0,
   LIKES_COUNT          int not null default 0,
   SHOUT                varchar(255),
   LANGUAGE             varchar(255),
   primary key (ACCOUNT_ID)
);

/*==============================================================*/
/* Index: SCREENNAME_UNIQUE                                     */
/*==============================================================*/
create unique index SCREENNAME_UNIQUE on PROFILE
(
   SCREEN_NAME
);

/*==============================================================*/
/* Table: REPORT                                                */
/*==============================================================*/
create table REPORT
(
   REPORT_ID            int not null,
   REPORTED_ID          int not null,
   UPDATED_TIME         timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   REPORT_COUNT         int not null default 0,
   REASON               varchar(255) not null,
   primary key (REPORT_ID, REPORTED_ID)
);

alter table ACCOUNT add constraint FK_ACCOUNTOWNAUTHENTICATE foreign key (ACCOUNT_ID)
      references AUTHENTICATE (ACCOUNT_ID) on delete restrict on update restrict;

alter table ACCOUNT_STAT add constraint FK_ACCOUNTOWNACCOUNTSTAT foreign key (ACCOUNT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table ACCOUNT_SYS add constraint FK_ACCOUNTOWNACCOUNTSYS foreign key (ACCOUNT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table AGENT add constraint FK_ACCOUNTOWNAGENT foreign key (ACCOUNT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table CHAIN add constraint FK_ACCOUNTHAVECHAIN foreign key (ACCOUNT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table CHAIN_HIST add constraint FK_ACCOUNTHAVECHAINHIST foreign key (ACCOUNT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table CHAIN_HIST add constraint FK_CHAINHAVECHAINHIST foreign key (CHAIN_ID)
      references CHAIN (CHAIN_ID) on delete restrict on update restrict;

alter table CHAIN_MESSAGE add constraint FK_CHAINHAVECHAINMESSAGE foreign key (CHAIN_ID)
      references CHAIN (CHAIN_ID) on delete restrict on update restrict;

alter table CHAIN_MESSAGE add constraint FK_CHAINMESSAGEBELONGTOACCOUNT foreign key (ACCOUNT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table MESSAGE add constraint FK_ACCOUNTOWNMESSAGE foreign key (ACCOUNT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table MESSAGE add constraint FK_PLANEHAVEMESSAGES foreign key (PLANE_ID)
      references PLANE (PLANE_ID) on delete restrict on update cascade;

alter table PLANE add constraint FK_ACCOUNTHAVEPLANES foreign key (OWNER_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table PLANE add constraint FK_PLANEBELONGTOCATEGORY foreign key (CATEGORY_ID)
      references CATEGORY (CATEGORY_ID) on delete restrict on update restrict;

alter table PLANE add constraint FK_PLANETARGETTOACCOUNT foreign key (TARGET_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table PLANE_HIST add constraint FK_ACCOUNTHAVEPLANEHIST foreign key (ACCOUNT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table PLANE_HIST add constraint FK_PLANEHAVEPLANEHIST foreign key (PLANE_ID)
      references PLANE (PLANE_ID) on delete restrict on update restrict;

alter table PROFILE add constraint FK_ACCOUNTOWNPROFILE foreign key (ACCOUNT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table REPORT add constraint FK_REPORTACCOUNT foreign key (REPORT_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;

alter table REPORT add constraint FK_REPORTEDBYACCOUNT foreign key (REPORTED_ID)
      references ACCOUNT (ACCOUNT_ID) on delete restrict on update restrict;
