CREATE TABLE if not exists orders
(
    order_id      INTEGER PRIMARY KEY,
    order_date    DATE NOT NULL,
    customer_name TEXT NOT NULL,
    order_status  TEXT NOT NULL
);