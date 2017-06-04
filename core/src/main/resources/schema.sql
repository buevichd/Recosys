CREATE TABLE `recosys`.`recosys_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);
-- --------------------------------------------------------
CREATE TABLE `recosys`.`recosys_product` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);
-- --------------------------------------------------------
CREATE TABLE `recosys`.`recosys_rating` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user` BIGINT(20) NOT NULL,
  `product` BIGINT(20) NOT NULL,
  `rating` INT NOT NULL,
  PRIMARY KEY (`id`)
);
ALTER TABLE `recosys`.`recosys_rating`
  ADD CONSTRAINT `FK_RATING_USER`
  FOREIGN KEY (`user`)
  REFERENCES `recosys`.`recosys_user` (`id`);
ALTER TABLE `recosys`.`recosys_rating`
  ADD CONSTRAINT `FK_RATING_PRODUCT`
  FOREIGN KEY (`product`)
  REFERENCES `recosys`.`recosys_product` (`id`);
-- --------------------------------------------------------
CREATE TABLE `recosys`.`recosys_estimated_rating` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user` BIGINT(20) NOT NULL,
  `product` BIGINT(20) NOT NULL,
  `rating` DOUBLE NOT NULL,
  PRIMARY KEY (`id`)
);
ALTER TABLE `recosys`.`recosys_estimated_rating`
  ADD CONSTRAINT `FK_ESTIMATED_RATING_USER`
  FOREIGN KEY (`user`)
  REFERENCES `recosys`.`recosys_user` (`id`);
ALTER TABLE `recosys`.`recosys_estimated_rating`
  ADD CONSTRAINT `FK_ESTIMATED_RATING_PRODUCT`
  FOREIGN KEY (`product`)
  REFERENCES `recosys`.`recosys_product` (`id`);