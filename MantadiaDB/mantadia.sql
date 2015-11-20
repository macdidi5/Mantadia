DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id`       INT NOT NULL AUTO_INCREMENT,
  `account`  VARCHAR(20) NOT NULL,
  `password` VARCHAR(8) NOT NULL,
  `name`     VARCHAR(64) NOT NULL,
  `gender`   INT NOT NULL,
  `role`     INT NOT NULL,
  `note`     VARCHAR(120) DEFAULT '',
  `imageid`  INT,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

INSERT INTO `user` VALUES (1,'simon','hello','Simon Johnson',1,0,'',NULL);
INSERT INTO `user` VALUES (2,'mary','hello','Mary Johnson',0,1,'',NULL);
INSERT INTO `user` VALUES (3,'john','hello','John Wise',1,2,'',NULL);
INSERT INTO `user` VALUES (4,'rose','hello','Rose Wise',0,2,'',NULL);
INSERT INTO `user` VALUES (5,'sam','hello','Sam Wise',1,3,'',NULL);

DROP TABLE IF EXISTS `tables`;

CREATE TABLE `tables` (
  `id`     INT NOT NULL AUTO_INCREMENT,
  `status` INT DEFAULT 0,
  `note`   VARCHAR(120) DEFAULT '',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

INSERT INTO `tables` VALUES (1,0,'Asia');
INSERT INTO `tables` VALUES (2,1,'Africa');
INSERT INTO `tables` VALUES (3,1,'North America');
INSERT INTO `tables` VALUES (4,0,'South America');
INSERT INTO `tables` VALUES (5,1,'Europe');
INSERT INTO `tables` VALUES (6,0,'Australia');
INSERT INTO `tables` VALUES (7,0,'Antarctica');

DROP TABLE IF EXISTS `menutype`;

CREATE TABLE `menutype` (
  `id`   INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(120) NOT NULL,
  `note` VARCHAR(120) DEFAULT '',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

INSERT INTO `menutype` VALUES (1,'開胃前菜','打開胃口用的餐點');
INSERT INTO `menutype` VALUES (2,'美味主餐','含有豐富油脂與高熱量的經典食物');
INSERT INTO `menutype` VALUES (3,'可口飲料','許多用來幫助進食的液體');
INSERT INTO `menutype` VALUES (4,'快樂點心','還沒吃飽嗎？');
INSERT INTO `menutype` VALUES (5,'超值套餐','為你搭配好的餐點，會比單點貴一些');
INSERT INTO `menutype` VALUES (6,'特價促銷','都是一些快要過期的美味食物');

DROP TABLE IF EXISTS `menuitem`;

CREATE TABLE `menuitem` (
  `id`         INT NOT NULL AUTO_INCREMENT,
  `menutypeid` INT NOT NULL,
  `name`       VARCHAR(120) NOT NULL,
  `price`      INT NOT NULL,
  `note`       VARCHAR(120) DEFAULT '',
  `imageid`    INT,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

INSERT INTO `menuitem` VALUES (1,1,'超級生菜沙拉',150,'一個大碗裡有很多不同的生菜，再淋上本店特製的沙拉醬，雖然生菜幾乎沒有熱量，不過我們特製的沙拉醬熱量卻非常高，所以千萬不要認為這道菜有減肥的效果',1);
INSERT INTO `menuitem` VALUES (2,1,'草莓生菜沙拉',150,'很多生菜加上少得可憐的草莓，讓這道前菜吃起來酸酸甜甜的，體驗與一般生菜沙拉完全不同的感受',2);
INSERT INTO `menuitem` VALUES (3,1,'沒味道生菜沙拉',100,'只有生菜，沒別的了，所以它真的幾乎沒有熱量，不過你還是可以加點超高熱量的沙拉醬，讓它變成超級生菜沙拉',3);
INSERT INTO `menuitem` VALUES (4,2,'頂級漢堡',120,'它有三片麵包、兩塊漢堡肉片、香濃的起司片，和一些你在其它漢堡裡會吃到的東西',4);
INSERT INTO `menuitem` VALUES (5,2,'香脆炸雞',150,'真的是又香又脆的炸雞，經過六個小時的油炸後，不只麵皮是脆的，連雞肉和雞骨頭都跟洋芋片一樣香脆',5);
INSERT INTO `menuitem` VALUES (6,2,'貴族潛艇堡',100,'長長的麵包裡面放了很多火腿、洋蔥、生菜和蕃茄，雖然名稱是貴族潛艇堡，不過一般的平民也可以享用',6);
INSERT INTO `menuitem` VALUES (7,3,'可樂',50,'黑色有汽泡的液體，如果一次服用太多的話，大約有79%的客人會出現打嗝的症狀',7);
INSERT INTO `menuitem` VALUES (8,3,'咖啡',50,'跟可樂同樣是黑色的液體，不過它不會有汽泡，可是你可以加點本店特製的發泡粉，可以把它變成咖啡汽水',8);
INSERT INTO `menuitem` VALUES (9,4,'起士蛋糕',60,'這個香甜濃郁的原味起士蛋糕，適合用來在吃完漢堡以後，補充大量的油脂與熱量',9);
INSERT INTO `menuitem` VALUES (10,4,'巧克力蛋糕',60,'呈現墨黑色的巧克力蛋糕，表示它真的有很多巧克力，油脂與熱量是所有甜點之冠',10);
INSERT INTO `menuitem` VALUES (11,4,'山峰霜淇淋',60,'非常普通和常見的霜淇淋，因為它的份量非常小，所以一次要購買三支',11);
INSERT INTO `menuitem` VALUES (12,5,'頂級漢堡套餐',220,'本店招牌頂級漢堡加上大約九到十二根炸薯條，最適合食量大的你來享用',12);
INSERT INTO `menuitem` VALUES (13,5,'黃金漢堡套餐',350,'本店招牌頂級漢堡、香脆炸雞、可樂和保證有二十根以上的炸薯條，真是過癮',13);
INSERT INTO `menuitem` VALUES (14,5,'黃金炸雞套餐',550,'無法形容的豐盛，香脆炸雞加上五種超高熱量的點心，還好本店隔壁就是減肥中心',14);
INSERT INTO `menuitem` VALUES (15,6,'起司洋蔥酸黃瓜熱狗堡',10,'這道特價熱狗堡的特色是有香脆口感的酸黃瓜，因為它快要過期了，所以呈現自然、濃郁和迷人的酸味',15);

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `id`       INT NOT NULL AUTO_INCREMENT,
  `time`     VARCHAR(24) NOT NULL,
  `userid`   INT NOT NULL,
  `tablesid` INT NOT NULL,
  `number`   INT NOT NULL,
  `status`   INT NOT NULL DEFAULT 0,
  `note`     VARCHAR(120) DEFAULT '',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

INSERT INTO `orders` VALUES (1,'2012-08-13 09:00',3,1,2,3,'');
INSERT INTO `orders` VALUES (2,'2012-08-13 10:00',3,2,2,3,'');
INSERT INTO `orders` VALUES (3,'2012-08-13 11:00',3,3,3,3,'');
INSERT INTO `orders` VALUES (4,'2012-08-13 12:00',3,1,3,3,'');
INSERT INTO `orders` VALUES (5,'2012-08-13 13:00',3,2,3,3,'');
INSERT INTO `orders` VALUES (6,'2012-08-13 14:00',3,3,2,3,'');
INSERT INTO `orders` VALUES (7,LEFT(NOW(), 16),3,3,2,0,'');
INSERT INTO `orders` VALUES (8,LEFT(NOW(), 16),4,5,3,1,'');
INSERT INTO `orders` VALUES (9,LEFT(NOW(), 16),4,2,1,2,'');

DROP TABLE IF EXISTS `orderitem`;

CREATE TABLE `orderitem` (
  `id`         INT NOT NULL AUTO_INCREMENT,
  `ordersid`   INT NOT NULL,
  `menuitemid` INT NOT NULL,
  `number`     INT NOT NULL,
  `status`     INT NOT NULL DEFAULT 0,
  `note`       VARCHAR(120) DEFAULT '',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

INSERT INTO `orderitem` VALUES (1,1,3,2,0,'');
INSERT INTO `orderitem` VALUES (2,1,4,2,0,'');
INSERT INTO `orderitem` VALUES (3,2,2,2,0,'');
INSERT INTO `orderitem` VALUES (4,3,2,3,0,'');
INSERT INTO `orderitem` VALUES (5,4,3,3,0,'');
INSERT INTO `orderitem` VALUES (6,5,3,3,0,'');
INSERT INTO `orderitem` VALUES (7,5,4,3,0,'');
INSERT INTO `orderitem` VALUES (8,6,1,2,0,'');
INSERT INTO `orderitem` VALUES (9,6,2,2,0,'');
INSERT INTO `orderitem` VALUES (10,6,3,2,0,'');
INSERT INTO `orderitem` VALUES (11,7,1,2,0,'');
INSERT INTO `orderitem` VALUES (12,7,2,2,0,'');
INSERT INTO `orderitem` VALUES (13,7,3,2,0,'');
INSERT INTO `orderitem` VALUES (14,8,2,3,0,'');
INSERT INTO `orderitem` VALUES (15,8,3,3,0,'');
INSERT INTO `orderitem` VALUES (16,8,5,3,0,'');
INSERT INTO `orderitem` VALUES (17,9,5,1,0,'');

DROP TABLE IF EXISTS `image`;

CREATE TABLE `image` (
  `id`         INT NOT NULL AUTO_INCREMENT,
  `filename`   VARCHAR(120) NOT NULL,
  `note`       VARCHAR(120) DEFAULT '',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

INSERT INTO `image` VALUES 
  (1,'20140313143709236.jpg',''),
  (2,'20140313143718974.jpg',''),
  (3,'20140313143726946.jpg',''),
  (4,'20140313143737164.jpg',''),
  (5,'20140313143745684.jpg',''),
  (6,'20140313143755698.jpg',''),
  (7,'20140313143807828.jpg',''),
  (8,'20140313143816937.jpg',''),
  (9,'20140313143827901.jpg',''),
  (10,'20140313143837146.jpg',''),
  (11,'20140313143846162.jpg',''),
  (12,'20140313143854991.jpg',''),
  (13,'20140313143906719.jpg',''),
  (14,'20140313143919083.jpg',''),
  (15,'20140313143928057.jpg','');
