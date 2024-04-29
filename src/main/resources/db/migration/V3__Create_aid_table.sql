CREATE TABLE IF NOT EXISTS aids (
    id SERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    manufacturer VARCHAR(120) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT false
);
