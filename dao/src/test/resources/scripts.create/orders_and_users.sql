CREATE TABLE  gifts.users
(
    user_id        INT          NOT NULL AUTO_INCREMENT,
    user_name      VARCHAR(100) NULL,
    user_surname   VARCHAR(100) NULL,
    login        VARCHAR(100) NULL,
    password       VARCHAR(100) NULL,
    phone_number   VARCHAR(100) NULL,
    email          VARCHAR(100) NULL,
    operation      VARCHAR(45)  NULL,
    operation_time TIMESTAMP(6) NULL,
    PRIMARY KEY (user_id)
);


CREATE TABLE  gifts.orders
(
    order_id            INT          NOT NULL AUTO_INCREMENT,
    user_id             INT          NULL,
    gift_certificate_id INT          NULL,
    final_price         INT          NULL,
    creation_time       TIMESTAMP(6) NULL,
    comment             VARCHAR(150) NULL,
    operation           VARCHAR(45)  NULL,
    operation_time      TIMESTAMP(6) NULL,
    PRIMARY KEY (order_id),

    CONSTRAINT to_user
        FOREIGN KEY (user_id)
            REFERENCES gifts.users (user_id)
            ON DELETE CASCADE
            ON UPDATE NO ACTION,
    CONSTRAINT to_certificate
        FOREIGN KEY (gift_certificate_id)
            REFERENCES gifts.gift_certificates (gift_certificate_id)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
);
