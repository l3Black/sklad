DELETE
FROM product;
DELETE
FROM warehouse;
DELETE
FROM products_in_warehouse;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO product (name, purchaseprice, sellingprice)
VALUES ('TV', 900, 1400),
       ('TabletPC', 800, 1500),
       ('Iphone', 500, 999),
       ('Laptop', 1000, 1700);

INSERT INTO warehouse (name)
VALUES ('podolsk'),
       ('domodedovo'),
       ('troizk');

INSERT INTO products_in_warehouse(warehouse_id, product_id, product_quantity)
VALUES (100004, 100000, 3),
       (100004, 100001, 2),
       (100004, 100002, 4),
       (100004, 100003, 5),
/*(100005, 100000, 0),*/
       (100005, 100001, 2),
       (100005, 100002, 1),
       (100005, 100003, 3),
       (100006, 100000, 13),
       (100006, 100001, 4),
       (100006, 100002, 8);
/*(100006, 100003, 0);*/