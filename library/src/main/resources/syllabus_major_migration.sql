-- Migration: Add major_id FK to syllabi table

ALTER TABLE syllabi ADD COLUMN major_id BIGINT;

ALTER TABLE syllabi
    ADD CONSTRAINT fk_syllabi_major
    FOREIGN KEY (major_id) REFERENCES majors(id);