> sql for create table User
```sql
DROP TABLE if exists `user`;
CREATE TABLE `user` (
                        `id` int unsigned NOT NULL AUTO_INCREMENT,
                        `username` varchar(255) DEFAULT NULL,
                        `password` varchar(255) DEFAULT NULL,
                        `email` varchar(64) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
>sql for create table video and img
```sql
create table `img` (
                       `id` int unsigned not null auto_increment,
                       `url` varchar(128) not null,
                       `missionID` int default null,
                       `fromID` int not null,
                       `kind` int not null,
                       PRIMARY KEY (`id`)
#     FOREIGN KEY (`missionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `video` (
                       `id` int unsigned not null auto_increment,
                       `url` varchar(128) not null,
                       `missionID` int default null,
                       `fromID` int not null,
                       `kind` int not null,
                       PRIMARY KEY (`id`)
#     FOREIGN KEY (`missionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```