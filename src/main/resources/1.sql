CREATE TABLE `englishstudy`.`word` (
                                       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                       `word` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `trans` VARCHAR( 500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `soundmark1` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `soundmark2` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                        `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                       `createtime` DATETIME NOT NULL,
                                       `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
                                       INDEX ( `word` , `updatetime` ,`deleted`)
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE `englishstudy`.`prounce` (
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

CREATE TABLE `englishstudy`.`cikutype` (
                                           `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                           `desc` VARCHAR( 500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                           `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                           `createtime` DATETIME NOT NULL ,
                                           `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
                                           INDEX ( `deleted` , `createtime` , `updatetime` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;


CREATE TABLE `englishstudy`.`ciku` (
                                       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                       `desc` VARCHAR( 500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                       `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                       `createtime` DATETIME NOT NULL,
                                       `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  NULL DEFAULT CURRENT_TIMESTAMP ,
                                       INDEX (  `updatetime`,   `deleted` )
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE `englishstudy`.`cikuexample` (
                                              `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                              `initial` VARCHAR( 10 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
                                              `wid` INT( 10 ) NOT NULL ,
                                              `deleted` INT( 2 ) NOT NULL DEFAULT '0',
                                              `createtime` DATETIME NOT NULL,
                                              `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
                                              INDEX ( `initial` , `wid` , `updatetime` ,`deleted`)
) ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;