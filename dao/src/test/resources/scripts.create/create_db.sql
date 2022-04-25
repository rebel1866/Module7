CREATE SCHEMA IF NOT EXISTS gifts
    with gifts;


CREATE TABLE gifts.gift_certificates
(
    gift_certificate_id INT           NOT NULL auto_increment,
    certificate_name    VARCHAR(250)  NULL,
    description         VARCHAR(1000) NULL,
    price               INT           NULL,
    duration            INT           NULL,
    creation_date       TIMESTAMP(6)  NULL,
    last_update_time    TIMESTAMP(6)  NULL,
    PRIMARY KEY (gift_certificate_id)
);



CREATE TABLE gifts.tags
(
    tag_id   INT          NOT NULL auto_increment,
    tag_name VARCHAR(100) NULL,
    PRIMARY KEY (tag_id)
);



CREATE TABLE gifts.cert_tags
(
    cert_tag_id         INT auto_increment,
    gift_certificate_id INT NULL,
    tag_id              INT NULL,
    PRIMARY KEY (cert_tag_id),
    CONSTRAINT tag_id_fk
        FOREIGN KEY (tag_id)
            REFERENCES gifts.tags (tag_id)
            ON DELETE CASCADE,

    CONSTRAINT gift_certificate_fk
        FOREIGN KEY (gift_certificate_id)
            REFERENCES gifts.gift_certificates (gift_certificate_id)
            on delete cascade

);
