DROP TABLE IF EXISTS workers;
CREATE TABLE workers(
    id bigint AUTO_INCREMENT,
    name VARCHAR2(255),
    PRIMARY KEY (id)
);

INSERT INTO workers(name) VALUES('Kenai');
INSERT INTO workers(name) VALUES('David');
INSERT INTO workers(name) VALUES('Kesia');
