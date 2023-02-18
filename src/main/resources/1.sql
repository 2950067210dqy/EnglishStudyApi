alter table word auto_increment=1;

CREATE TABLE  IF NOT EXISTS  `englishstudy`.`word` (
                                       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                       `word` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `charac` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `trans` VARCHAR( 1500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `soundmark1` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `soundmark2` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                        `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                       `createtime` DATETIME NOT NULL,
                                       `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
                                       INDEX ( `word` ,`charac` ,`updatetime` ,`deleted`)
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE  IF NOT EXISTS `englishstudy`.`prounce` (
                                          `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                          `initial` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                          `wid` INT( 10 ) NOT NULL ,
                                          `url1` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                          `url2` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                          `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                          `createtime` DATETIME NOT NULL,
                                          `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  NULL DEFAULT CURRENT_TIMESTAMP,
                                          INDEX ( `initial` , `wid` , `updatetime`,`deleted` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE  IF NOT EXISTS `englishstudy`.`cikutype` (
                                           `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                           `dsc` VARCHAR( 500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                           `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                           `createtime` DATETIME NOT NULL ,
                                           `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
                                           INDEX ( `deleted` , `createtime` , `updatetime` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE  IF NOT EXISTS `englishstudy`.`ciku` (
                                       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                        `uid` INT( 10 ) NOT NULL,
                                       `dsc` VARCHAR( 500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `dscabb` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                       `createtime` DATETIME NOT NULL,
                                       `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  NULL DEFAULT CURRENT_TIMESTAMP ,
                                       INDEX (  `uid`,`updatetime`, `dscabb`,  `deleted` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE  IF NOT EXISTS `englishstudy`.`cikuexample` (
                                              `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                              `initial` VARCHAR( 10 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                              `wid` INT( 10 ) NOT NULL ,
                                              `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                              `createtime` DATETIME NOT NULL,
                                              `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
                                              INDEX ( `initial` , `wid` , `updatetime` ,`deleted`)
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE  IF NOT EXISTS `englishstudy`.`liju` (
                                       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                       `word` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `sentences` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                       `createtime` DATETIME NOT NULL ,
                                       `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                                       INDEX ( `word` , `deleted` , `updatetime` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE TABLE  IF NOT EXISTS `englishstudy`.`user` (
                                       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                       `username` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `password` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `name` VARCHAR( 500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `headimage` VARCHAR( 500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `sex` INT( 2 ) NOT NULL ,
                                       `birthday` DATE NOT NULL ,
                                       `age` INT( 4 ) NOT NULL ,
                                       `phone` VARCHAR( 20 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `email` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `type` INT( 2 ) NOT NULL ,
                                       `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                       `createtime` DATETIME NOT NULL ,
                                       `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                                       INDEX ( `sex` , `birthday` , `age` , `type` , `deleted` , `updatetime` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS `englishstudy`.`nowresite` (
                                            `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                            `uid` INT NOT NULL ,
                                            `cikutypeid` INT NOT NULL ,
                                            `cikuid` INT NOT NULL ,
                                            `isstudy` INT NOT NULL ,
                                            `type` INT( 3 ) NOT NULL COMMENT '0为词库 1为生词本',
                                            `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                            `createtime` DATETIME NOT NULL ,
                                            `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                                            INDEX ( `uid` , `cikutypeid` , `cikuid` , `isstudy` ,`deleted`, `updatetime` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS `englishstudy`.`resite` (
                                         `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                         `nowresiteid` INT( 50 ) NOT NULL ,
                                         `learn` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                         `review1` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                         `review2` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                         `review4` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                         `review7` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                         `review15` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                         `over` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                         `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                         `createtime` DATETIME NOT NULL ,
                                         `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                                         INDEX ( `nowresiteid` , `deleted` , `updatetime` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE `englishstudy`.`freshword` (
                                            `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                            `uid` INT( 50 ) NOT NULL ,
                                            `words` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                            `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                            `createtime` DATETIME NOT NULL ,
                                            `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                                            INDEX ( `uid` , `deleted` , `updatetime` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;

SELECT * FROM `word_a` where word like '% for %' or word like '% for' or word like 'for %';

W3siaWQiOjMsInVwZGF0ZXRpbWUiOiIyMDIzLTAyLTE1VDE4OjEzOjMzIn0seyJpZCI6NSwidXBkYXRldGltZSI6IjIwMjMtMDItMTVUMTg6MTM6NDQifSx7ImlkIjo2LCJ1cGRhdGV0aW1lIjoiMjAyMy0wMi0xNVQxODoxMzo0OSJ9LHsiaWQiOjcsInVwZGF0ZXRpbWUiOiIyMDIzLTAyLTE1VDE4OjEzOjUzIn0seyJpZCI6NCwidXBkYXRldGltZSI6IjIwMjMtMDItMTZUMTg6MTM6NTgifV0=

max_allowed_packet = 10M;
