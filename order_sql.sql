create database ordersdb;
use database ordersdb;
create table orders
(
    id               int auto_increment
        primary key,
    user_id          int                            not null,
    restaurant_id    int                            not null,
    total_price      decimal(10, 2)                 not null,
    destination      varchar(255)                   not null,
    shipping_company varchar(100)                   null,
    status           varchar(255) default 'Pending' null
);

create table orderdishes
(
    id          int auto_increment
        primary key,
    order_id    int            not null,
    name        varchar(100)   not null,
    amount      int            not null,
    price       decimal(10, 2) not null,
    description text           null,
    constraint orderdishes_ibfk_1
        foreign key (order_id) references orders (id)
);

create index order_id
    on orderdishes (order_id);