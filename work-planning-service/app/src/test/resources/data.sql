DROP TABLE IF EXISTS workers;
CREATE TABLE workers(
    id bigint AUTO_INCREMENT,
    name VARCHAR2(255),
    PRIMARY KEY (id)
);

INSERT INTO workers(id, name) VALUES(1, 'Kenai');
INSERT INTO workers(id, name) VALUES(2, 'David');
INSERT INTO workers(id, name) VALUES(3, 'Kesia');
