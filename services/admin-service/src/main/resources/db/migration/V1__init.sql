-- Flyway baseline for admin service

CREATE TABLE IF NOT EXISTS roles (
  id SERIAL PRIMARY KEY,
  name VARCHAR(128) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS audit_logs (
  id SERIAL PRIMARY KEY,
  actor VARCHAR(255),
  action VARCHAR(255),
  created_at TIMESTAMP DEFAULT NOW(),
  detail TEXT
);
