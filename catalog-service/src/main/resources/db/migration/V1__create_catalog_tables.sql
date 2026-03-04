CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1024)
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2048),
    sku VARCHAR(255) NOT NULL UNIQUE,
    price NUMERIC(12,2) NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories(id)
);

CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_description ON products(description);
