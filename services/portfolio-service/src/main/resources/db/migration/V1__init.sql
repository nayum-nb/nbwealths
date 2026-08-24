-- Flyway baseline for portfolio service

CREATE TABLE IF NOT EXISTS portfolios (
  id SERIAL PRIMARY KEY,
  owner VARCHAR(255) NOT NULL,
  name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS positions (
  id SERIAL PRIMARY KEY,
  portfolio_id INT REFERENCES portfolios(id),
  symbol VARCHAR(32),
  quantity NUMERIC(19,4),
  avg_price NUMERIC(19,6)
);
