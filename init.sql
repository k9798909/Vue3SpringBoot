CREATE SEQUENCE product_id_seq;
CREATE SEQUENCE user_id_seq;

CREATE TABLE product (
    id VARCHAR(20) PRIMARY KEY DEFAULT CONCAT('PROD_', LPAD(NEXTVAL('product_id_seq')::text, 10, '0')),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    crtuser VARCHAR(20),
    crttime TIMESTAMP DEFAULT NOW(),
    upduser VARCHAR(20),
    updtime TIMESTAMP 
);

CREATE TABLE product_image (
    id SERIAL PRIMARY KEY,
    image BYTEA,
    product_id VARCHAR(20) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE users (
   id TEXT PRIMARY KEY DEFAULT LPAD(nextval('user_id_seq')::TEXT, 11, '0'),
   username TEXT NOT NULL UNIQUE,
   password TEXT NOT NULL,
   name VARCHAR(255) NOT NULL,
   birthday DATE NOT NULL,
   email VARCHAR(255) NOT NULL,
   address VARCHAR(255)
);

CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    user_id TEXT REFERENCES users(id),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2),
    status VARCHAR(50)
);

CREATE TABLE order_details (
    detail_id INT ,
    order_id INT REFERENCES orders(order_id),
    product_id VARCHAR(20) REFERENCES product(id),
    quantity INT,
    price DECIMAL(10, 2),
	PRIMARY KEY (detail_id, order_id)
);

CREATE INDEX username_index ON users (username);

INSERT INTO product (name, description, price, quantity, crtuser, crttime) VALUES
('Product 1', 'This is product 1', 100.00, 10, 'user1', NOW()),
('Product 2', 'This is product 2', 200.00, 20, 'user2', NOW()),
('Product 3', 'This is product 3', 300.00, 30, 'user3', NOW()),
('Product 4', 'This is product 4', 400.00, 40, 'user4', NOW()),
('Product 5', 'This is product 5', 500.00, 50, 'user5', NOW());


INSERT INTO product_image (image, product_id)
VALUES (pg_read_binary_file('/docker-entrypoint-initdb.d/product1.png'),'PROD_0000000001');
INSERT INTO product_image (image, product_id)
VALUES (pg_read_binary_file('/docker-entrypoint-initdb.d/product2.jpg'),'PROD_0000000002');
INSERT INTO product_image (image, product_id)
VALUES (pg_read_binary_file('/docker-entrypoint-initdb.d/product3.jpg'),'PROD_0000000003');
INSERT INTO product_image (image, product_id)
VALUES (pg_read_binary_file('/docker-entrypoint-initdb.d/product1.png'),'PROD_0000000004');
INSERT INTO product_image (image, product_id)
VALUES (pg_read_binary_file('/docker-entrypoint-initdb.d/product2.jpg'),'PROD_0000000005');

INSERT INTO users (username, password, name, birthday, email, address)
VALUES ('test', '{bcrypt}$2a$10$QZmTXJBuZZ9G5X5zsNdlA.iiGCfIgB.ooEv7eYwWJBIi61vYFq05G', 'John Doe', '1990-01-15', 'john.doe@example.com', '123 Main St');

INSERT INTO orders (user_id, order_date, total_price, status)
VALUES
    ('00000000001', '2023-01-15', 79.98, '0'),
    ('00000000001', '2023-01-16', 99.98, '0');

INSERT INTO order_details (detail_id,order_id, product_id, quantity, price)
VALUES
    (1,1, 'PROD_0000000001', 2, 29.99),
    (1,2, 'PROD_0000000002', 1, 49.99),
    (2,2, 'PROD_0000000001', 3, 29.99);










