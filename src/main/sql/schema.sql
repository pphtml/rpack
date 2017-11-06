CREATE TABLE employee
(
  id SERIAL PRIMARY KEY NOT NULL,
  name VARCHAR NOT NULL,
  age INT NOT NULL
);
CREATE UNIQUE INDEX employee_id_uindex ON employee (id);
