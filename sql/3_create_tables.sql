CREATE TABLE if not exists shipments
(
    shipment_id     INTEGER PRIMARY KEY,
    destination     TEXT NOT NULL,
    shipment_date   DATE NOT NULL,
    shipment_status TEXT NOT NULL
);