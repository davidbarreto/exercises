DROP TABLE IF EXISTS workers;
CREATE TABLE workers(
    id INT AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS shift_types;
CREATE TABLE shift_types (
    id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT unique_name UNIQUE(name)
);

DROP TABLE IF EXISTS shifts;
CREATE TABLE shifts (
    id INT AUTO_INCREMENT,
    shift_date DATE NOT NULL,
    shift_type_id INT NOT NULL,
    CONSTRAINT fk_shift_type FOREIGN KEY (shift_type_id) REFERENCES shift_types(id),
    CONSTRAINT unique_shift_type_per_day UNIQUE (shift_date, shift_type_id),
    PRIMARY KEY(id)
);

DROP TABLE IF EXISTS workers_shifts;
CREATE TABLE workers_shifts (
    id INT AUTO_INCREMENT,
    worker_id INT NOT NULL,
    shift_id INT NOT NULL,
    CONSTRAINT fk_worker FOREIGN KEY (worker_id) REFERENCES workers(id),
    CONSTRAINT fk_shift FOREIGN KEY (shift_id) REFERENCES shifts(id),
    CONSTRAINT unique_shift_per_day_per_worker UNIQUE (shift_id, worker_id),
    PRIMARY KEY(id)
);

INSERT INTO workers(name) VALUES('Kenai');
INSERT INTO workers(name) VALUES('David');
INSERT INTO workers(name) VALUES('Kesia');

INSERT INTO shift_types (id, name, start_time, end_time) VALUES (1, 'NIGHT', '00:00:00', '08:00:00');
INSERT INTO shift_types (id, name, start_time, end_time) VALUES (2, 'DAY', '08:00:00', '16:00:00');
INSERT INTO shift_types (id, name, start_time, end_time) VALUES (3, 'EVENING', '16:00:00', '00:00:00');

INSERT INTO shifts (shift_date, shift_type_id) VALUES ('2023-05-02', 1);
INSERT INTO shifts (shift_date, shift_type_id) VALUES ('2023-05-03', 1);
INSERT INTO shifts (shift_date, shift_type_id) VALUES ('2023-05-03', 2);

INSERT INTO workers_shifts (worker_id, shift_id) VALUES (1, 1);
INSERT INTO workers_shifts (worker_id, shift_id) VALUES (1, 2);
INSERT INTO workers_shifts (worker_id, shift_id) VALUES (2, 1);
INSERT INTO workers_shifts (worker_id, shift_id) VALUES (2, 2);
INSERT INTO workers_shifts (worker_id, shift_id) VALUES (3, 1);
INSERT INTO workers_shifts (worker_id, shift_id) VALUES (3, 2);
