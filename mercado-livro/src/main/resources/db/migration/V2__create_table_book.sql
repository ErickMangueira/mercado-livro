create table book(
    id int auto_increment primary key,
    nome varchar(255) not null,
    preco decimal(10, 2) not null,
    status varchar(255) not null,
    customer_id int not null,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);