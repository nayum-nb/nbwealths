-- Flyway baseline for account service

CREATE TABLE IF NOT EXISTS accounts (
  id SERIAL PRIMARY KEY,
  owner VARCHAR(255) NOT NULL,
  type VARCHAR(50),
  currency VARCHAR(10),
  balance NUMERIC(19,4) DEFAULT 0
);
