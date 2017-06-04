CREATE TABLE recosys_user (
  id BIGINT NOT NULL IDENTITY
);

CREATE TABLE recosys_product (
  id BIGINT NOT NULL IDENTITY
);

CREATE TABLE recosys_rating (
  id BIGINT NOT NULL IDENTITY,
  user BIGINT NOT NULL,
  product BIGINT NOT NULL,
  rating INTEGER NOT NULL
);
ALTER TABLE recosys_rating ADD FOREIGN KEY (user) REFERENCES recosys_user(id);
ALTER TABLE recosys_rating ADD FOREIGN KEY (product) REFERENCES recosys_product(id);

CREATE TABLE recosys_estimated_rating (
  id BIGINT NOT NULL IDENTITY,
  user BIGINT NOT NULL,
  product BIGINT NOT NULL,
  rating DOUBLE NOT NULL
);
ALTER TABLE recosys_estimated_rating ADD FOREIGN KEY (user) REFERENCES recosys_user(id);
ALTER TABLE recosys_estimated_rating ADD FOREIGN KEY (product) REFERENCES recosys_product(id);