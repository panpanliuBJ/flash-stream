drop table if exists t_user_share_5min;
create table t_user_share_5min
(
  user_id       bigint          not null default -1,
  share         bigint          not null default 0,
  create_time   datetime,
  update_time   timestamp       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key(user_id, create_time)
);
create index ind_ctime on t_user_share_5min(create_time);


drop table if exists t_user_share_15min;
create table t_user_share_15min
(
  user_id       bigint          not null default -1,
  share         bigint          not null default 0,
  create_time   datetime,
  update_time   timestamp       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key(user_id, create_time)
);
create index ind_ctime on t_user_share_15min(create_time);



drop table if exists t_user_share_hour;
create table t_user_share_hour
(
  user_id       bigint          not null default -1,
  share         bigint          not null default 0,
  create_time   datetime,
  update_time   timestamp       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  primary key(user_id, create_time)
);
create index ind_ctime on t_user_share_hour(create_time);


