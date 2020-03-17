DELETE
FROM doc_entity;
DELETE
FROM product;
DELETE
FROM store;
DELETE
FROM products_in_store;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO product (name, purchaseprice, sellingprice)
VALUES ('TV', 900, 1400),
       ('TabletPC', 800, 1500),
       ('Iphone', 500, 999),
       ('Laptop', 1000, 1700);

INSERT INTO store (name)
VALUES ('podolsk'),
       ('domodedovo'),
       ('troizk');

INSERT INTO products_in_store(store_id, product_id, product_quantity)
VALUES (100004, 100000, 13),
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

INSERT INTO doc_entity (type, enter_store_id, write_off_store_id, products)
VALUES ('ENTER', 100006, null, '{"100000":12,"100001":13,"100002":14,"100003":15}'),
       ('SALE', null, 100004, '{"100000":2,"100001":1,"100002":3,"100003":2}'),
       ('MOVE', 100005, 100004, '{"100000":2,"100001":1,"100002":1,"100003":3}');
