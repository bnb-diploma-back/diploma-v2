-- Migration: Replace syllabi.department (String) with syllabi.department_id (FK to departments)

-- Step 1: Add the new column
ALTER TABLE syllabi ADD COLUMN department_id BIGINT;

-- Step 2: Populate department_id from existing department name values (if any match)
UPDATE syllabi s
SET department_id = d.id
FROM departments d
WHERE s.department = d.name;

-- Step 3: Drop the old string column
ALTER TABLE syllabi DROP COLUMN department;

-- Step 4: Add foreign key constraint
ALTER TABLE syllabi
    ADD CONSTRAINT fk_syllabi_department
    FOREIGN KEY (department_id) REFERENCES departments(id);