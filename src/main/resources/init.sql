-- Initialize database schema and data

-- Create tables if they don't exist (though Hibernate will handle this with ddl-auto=update)
-- This is just for completeness

-- Students table
CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    age INTEGER
);

-- Grades table
CREATE TABLE IF NOT EXISTS grades (
    id SERIAL PRIMARY KEY,
    subject_name VARCHAR(255) NOT NULL,
    grade DOUBLE PRECISION NOT NULL,
    student_id BIGINT NOT NULL,
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students(id)
);

-- Insert sample students
INSERT INTO students (first_name, last_name, email, age) VALUES
    ('John', 'Doe', 'john.doe@example.com', 20),
    ('Jane', 'Smith', 'jane.smith@example.com', 22),
    ('Bob', 'Johnson', 'bob.johnson@example.com', 19),
    ('Alice', 'Williams', 'alice.williams@example.com', 21),
    ('Charlie', 'Brown', 'charlie.brown@example.com', 23);

-- Insert sample grades
-- For student1 (John Doe)
INSERT INTO grades (subject_name, grade, student_id) VALUES
    ('Mathematics', 85.5, 1),
    ('Physics', 78.0, 1),
    ('Computer Science', 92.0, 1);

-- For student2 (Jane Smith)
INSERT INTO grades (subject_name, grade, student_id) VALUES
    ('Mathematics', 90.0, 2),
    ('Biology', 88.5, 2),
    ('Chemistry', 85.0, 2);

-- For student3 (Bob Johnson)
INSERT INTO grades (subject_name, grade, student_id) VALUES
    ('History', 76.0, 3),
    ('English', 82.5, 3);

-- For student4 (Alice Williams)
INSERT INTO grades (subject_name, grade, student_id) VALUES
    ('Art', 95.0, 4),
    ('Music', 91.5, 4),
    ('Literature', 88.0, 4);

-- For student5 (Charlie Brown)
INSERT INTO grades (subject_name, grade, student_id) VALUES
    ('Physical Education', 87.0, 5),
    ('Geography', 79.5, 5);
