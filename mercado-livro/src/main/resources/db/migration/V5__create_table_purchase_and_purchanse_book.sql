CREATE TABLE purchase(
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT not null,
    nfe VARCHAR(255) ,
    price DECIMAL(15,2) not null,
    created_at TIMESTAMP not null,
    FOREIGN KEY (customer_id) REFERENCES customer(id)

);

CREATE TABLE purchase_book(
    purchase_id INT not null,
    book_id INT not null,
    PRIMARY KEY (purchase_id, book_id),
    FOREIGN KEY (purchase_id) REFERENCES purchase(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);