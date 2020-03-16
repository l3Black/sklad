DROP TABLE IF EXISTS doc_entity;
DROP TABLE IF EXISTS products_in_warehouse CASCADE;
DROP TABLE IF EXISTS warehouse CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP SEQUENCE IF EXISTS global_seq CASCADE;

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

CREATE TABLE doc_entity
(
    id                 BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    type               VARCHAR(35),
    enter_store_id     BIGINT,
    write_off_store_id BIGINT,
    products           VARCHAR NOT NULL,
    FOREIGN KEY (enter_store_id) REFERENCES warehouse (id),
    FOREIGN KEY (enter_store_id) REFERENCES warehouse (id)
);