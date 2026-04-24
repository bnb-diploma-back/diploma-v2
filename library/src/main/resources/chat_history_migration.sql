-- Migration: Add chat_history table for persisting AI chat conversations

CREATE TABLE chat_history (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id),
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP
);

CREATE INDEX idx_chat_history_student_id ON chat_history(student_id);