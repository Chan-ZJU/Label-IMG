> sql for create table User

```sql
DROP TABLE if exists `user`;
CREATE TABLE `user`
(
    `id`       int unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(255) DEFAULT NULL,
    `email`    varchar(64)  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
```

> sql for create table video and img

```sql
drop table if exists `img`;
create table `img`
(
    `id`        int unsigned not null auto_increment,
    `url`       varchar(128) not null,
    `missionID` int default null,
    `fromID`    int          not null,
    `kind`      int          not null,
    PRIMARY KEY (`id`)
#     FOREIGN KEY (`missionID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

drop table if exists `video`;
create table `video`
(
    `id`        int unsigned not null auto_increment,
    `url`       varchar(128) not null,
    `missionID` int default null,
    `fromID`    int          not null,
    `kind`      int          not null,
    PRIMARY KEY (`id`)
#     FOREIGN KEY (`missionID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
```

> sql for create table mission

```sql
drop table if exists `mission`;
create table `mission`
(
    `id`          int unsigned not null auto_increment,
    `description` varchar(256) not null,
    `fromID`      int          not null,
    `toID`        int default null,
    `state`       int          not null,
    primary key (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
```

> sql for create table image_mission

```sql
create table image_mission
(
    imageID   int not null,
    missionID int not null,
    pointsX   varchar(1024),
    pointsY   varchar(1024),
    remarks   varchar(256),
    sizeX     varchar(128),
    sizeY     varchar(128)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
```