DROP TABLE IF EXISTS products_in_warehouse;
DROP TABLE IF EXISTS warehouse;
DROP TABLE IF EXISTS product;
DROP SEQUENCE IF EXISTS global_seq;
CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE product
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    name          VARCHAR NOT NULL,
    purchasePrice FLOAT   NOT NULL,
    sellingPrice  FLOAT   NOT NULL
);

CREATE TABLE warehouse
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    name VARCHAR
);

CREATE TABLE products_in_warehouse
(
    warehouse_id     BIGINT  NOT NULL,
    product_id       BIGINT  NOT NULL,
    product_quantity INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (warehouse_id) REFERENCES warehouse (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);