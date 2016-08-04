/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.12-log : Database - wenda
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wenda` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `wenda`;

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `user_id` int(11) NOT NULL,
  `entity_id` int(11) NOT NULL,
  `entity_type` int(11) NOT NULL,
  `created_date` datetime NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `entity_index` (`entity_type`),
  KEY `entity_id` (`entity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

/*Data for the table `comment` */

insert  into `comment`(`id`,`content`,`user_id`,`entity_id`,`entity_type`,`created_date`,`status`) values (1,'测试添加评论',3,19,1,'2016-07-31 16:06:46',0),(2,'添加评论',12,19,1,'2016-07-31 16:09:41',0),(3,'在添加一条***评论',13,19,1,'2016-07-31 16:20:21',0),(4,'添加***评论',13,19,1,'2016-07-31 16:20:39',0),(5,'用户信息不对啊',13,19,1,'2016-07-31 16:21:07',0),(6,'测试',12,19,1,'2016-07-31 16:25:21',0),(7,'添加评论是谁呢 ',3,19,1,'2016-07-31 16:28:06',0),(8,'添加的的的的对的对的 ',3,19,1,'2016-07-31 16:28:54',0),(9,'舔你的的的的 ',3,19,1,'2016-07-31 16:36:21',0),(10,'添加品论后',3,19,1,'2016-07-31 17:00:51',0),(11,'添加； ',13,18,1,'2016-07-31 17:01:17',0),(12,'再次添加',13,18,1,'2016-07-31 17:01:54',0),(13,'添加',13,18,1,'2016-07-31 17:02:20',0),(14,'ggggggg',12,20,1,'2016-08-03 20:50:13',0),(15,'ffdfdfdf',12,20,1,'2016-08-03 20:50:17',0);

/*Table structure for table `login_ticket` */

DROP TABLE IF EXISTS `login_ticket`;

CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(45) NOT NULL,
  `expired` datetime NOT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_UNIQUE` (`ticket`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/*Data for the table `login_ticket` */

insert  into `login_ticket`(`id`,`user_id`,`ticket`,`expired`,`status`) values (1,12,'f13165a4fdf647a0a98b0932d0cb186f','2016-07-27 22:47:16',0),(2,12,'d38df0898a6a46d998f30f77cbee28b8','2016-07-28 22:41:22',0),(3,12,'157341d325f14047a39c0f45a06a962d','2016-07-31 15:13:58',0),(4,13,'937f887030be46f68fe85d331d4c7da6','2016-07-31 15:15:59',0),(5,12,'1a655d0e6c7c464b8a1acb8920d9dd17','2016-07-31 16:32:11',1),(6,13,'577a7b828af945a3875da8f1487b628a','2016-07-31 16:36:35',1),(7,12,'fea4a6ebb2c34b4bbbbd66c1088f6d20','2016-07-31 21:37:05',1),(8,13,'3f86f76453224dda80fc1c001c8bcabb','2016-07-31 21:37:25',1),(9,12,'713cb619451f4e72bca03cf9f41fc702','2016-07-31 22:53:09',1),(10,12,'b1bced75d5244089ba9494bbf5e76c92','2016-08-01 09:10:30',1),(11,13,'9f7f64f757064962bd502c6f1352d4c1','2016-08-01 09:27:23',0),(12,13,'a8ad7e58cbe84fcb994fb1741883f8c6','2016-08-01 13:13:05',1),(13,12,'45b55943737d48388bf5b510877a9342','2016-08-01 21:39:44',1),(14,13,'b5185866188241f6935a658ec6b1839d','2016-08-01 22:48:22',1),(15,12,'acf90856e0c64a868d1240bc652a1bef','2016-08-01 22:48:56',0),(16,12,'5f13ea102f8241ebb2401bf054f580e2','2016-08-04 20:27:11',1),(17,13,'848ade60b2e141ec959d8dcbd51cdd8f','2016-08-04 20:36:00',0),(18,12,'fc270691e43d4a4790b187b91a31dd05','2016-08-04 20:36:42',0);

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `content` text,
  `created_date` datetime DEFAULT NULL,
  `has_read` int(11) DEFAULT NULL,
  `conversation_id` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `conversation_index` (`conversation_id`),
  KEY `created_date` (`created_date`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Data for the table `message` */

insert  into `message`(`id`,`from_id`,`to_id`,`content`,`created_date`,`has_read`,`conversation_id`) values (1,13,12,'你好  ***  aaa','2016-07-31 21:32:25',1,'12_13'),(2,12,13,'发送给 bbb 的 ***去吧？','2016-07-31 21:40:05',1,'12_13'),(3,12,2,'辅导费的收费','2016-07-31 21:49:41',0,'2_12'),(4,3,12,'fdfdfdfdfdfd','2016-07-31 21:50:13',1,'3_12'),(5,4,12,'fdfsfsfasfasfasfdsfasfdasf','2016-08-01 21:50:54',1,'4_12'),(6,5,12,'ccccccccc','2016-07-31 21:51:23',1,'5_12'),(7,12,5,'fdfdfdfdfddddddddddddd','2016-07-31 21:51:47',0,'5_12'),(8,12,4,'ccccccccccccccyyyyyyyyyyyy','2016-07-31 21:52:08',0,'4_12'),(9,12,3,'35345trgdsgdgf','2016-07-31 21:52:30',0,'3_12'),(10,13,2,'fdfdfdfdf','2016-07-31 21:56:48',0,'2_13'),(11,12,13,'kokokookk','2016-07-31 22:21:35',1,'12_13'),(12,13,12,'fdfdfdfdfdf','2016-07-31 22:48:38',1,'12_13'),(13,12,13,'dfdfdfdf','2016-07-31 22:49:05',0,'12_13');

/*Table structure for table `question` */

DROP TABLE IF EXISTS `question`;

CREATE TABLE `question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` text,
  `user_id` int(11) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comment_count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

/*Data for the table `question` */

insert  into `question`(`id`,`title`,`content`,`user_id`,`created_date`,`comment_count`) values (1,'This is a Title 0','this is a content dfdfdsfsafsdfsdfsdf0',1,'2016-07-23 22:53:13',1),(2,'This is a Title 1','this is a content dfdfdsfsafsdfsdfsdf1',2,'2016-07-23 22:54:39',2),(3,'This is a Title 2','this is a content dfdfdsfsafsdfsdfsdf2',3,'2016-07-23 22:56:06',3),(4,'This is a Title 3','this is a content dfdfdsfsafsdfsdfsdf3',4,'2016-07-23 22:57:32',4),(5,'This is a Title 4','this is a content dfdfdsfsafsdfsdfsdf4',5,'2016-07-23 22:58:58',5),(6,'This is a Title 5','this is a content dfdfdsfsafsdfsdfsdf5',6,'2016-07-23 23:00:25',6),(7,'This is a Title 6','this is a content dfdfdsfsafsdfsdfsdf6',7,'2016-07-23 23:01:51',7),(8,'This is a Title 7','this is a content dfdfdsfsafsdfsdfsdf7',8,'2016-07-23 23:03:18',8),(9,'This is a Title 8','this is a content dfdfdsfsafsdfsdfsdf8',9,'2016-07-23 23:04:44',9),(10,'This is a Title 9','this is a content dfdfdsfsafsdfsdfsdf9',10,'2016-07-23 23:06:10',10),(11,'This is a Title 10','this is a content dfdfdsfsafsdfsdfsdf10',11,'2016-07-23 23:07:37',11),(12,'aaa','aaaaaa',12,'2016-07-31 09:18:05',0),(13,'fdfddf','fdfdfdfdf',12,'2016-07-31 09:20:00',0),(14,'fffff','fffffff',13,'2016-07-31 09:38:55',0),(15,'hhhrhr','hrhrhrhh',13,'2016-07-31 13:23:03',0),(16,'Controller不加@Controller 则不能被程序识别','这个问题好傻',13,'2016-07-31 13:23:46',0),(17,'测试XXS','&lt;script&gt;alert(&quot;XSS&quot;);&lt;/script&gt;',13,'2016-07-31 13:27:31',0),(18,'测试敏感词','澳门神话大赌场，赌博。这里有色情服务。fff。。。ＳＡＦＡ',13,'2016-07-31 17:02:19',2),(19,'测试敏感词22','澳门神话大赌场，***。这里有***服务。fff。。。ＳＡＦＡ',13,'2016-07-31 17:00:51',9),(20,'aaa','你好  ***  aaa',13,'2016-08-03 20:50:16',2);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `head_url` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`password`,`salt`,`head_url`) values (1,'User:0','password0','salt0','http://images.nowcoder.com/head/616t.png'),(2,'User:1','password1','salt1','http://images.nowcoder.com/head/577t.png'),(3,'User:2','password2','salt2','http://images.nowcoder.com/head/966t.png'),(4,'User:3','password3','salt3','http://images.nowcoder.com/head/980t.png'),(5,'User:4','password4','salt4','http://images.nowcoder.com/head/393t.png'),(6,'User:5','password5','salt5','http://images.nowcoder.com/head/621t.png'),(7,'User:6','password6','salt6','http://images.nowcoder.com/head/872t.png'),(8,'User:7','password7','salt7','http://images.nowcoder.com/head/957t.png'),(9,'User:8','password8','salt8','http://images.nowcoder.com/head/552t.png'),(10,'User:9','password9','salt9','http://images.nowcoder.com/head/866t.png'),(11,'User:10','password10','salt10','http://images.nowcoder.com/head/700t.png'),(12,'aaa','3A0AC46FA60B25053C756325F887741B','bb36a','http://images.nowcoder.com/head/819t.png'),(13,'bbb','34E9C54A9145E0EC162DEDD24DDA4E41','f99b7','http://images.nowcoder.com/head/887t.png');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
