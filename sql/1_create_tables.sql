CREATE TABLE if not exists inventory
(
    item_id       INTEGER PRIMARY KEY,
    item_name     TEXT    NOT NULL,
    item_quantity INTEGER NOT NULL,
    item_location TEXT    NOT NULL
);