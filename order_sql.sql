create database ordersdb;
use database ordersdb;
create table orders
(
    id               bigint auto_increment
        primary key,
    user_id          int                            not null,
    restaurant_id    int                            not null,
    total_price      decimal(10, 2)                 not null,
    destination      varchar(255)                   not null,
    shipping_company varchar(100)                   null,
    status           varchar(255) default 'Pending' null
);

create table order_dishes
(
    id         bigint auto_increment
        primary key,
    order_id   bigint         not null,
    dish_id    bigint         not null,
    dish_name  varchar(255)   not null,
    quantity   int            not null,
    unit_price decimal(10, 2) not null,
    constraint fk_order_dishes_order
        foreign key (order_id) references orders (id)
);

create index idx_dish_id
    on order_dishes (dish_id);

create index idx_order_id
    on order_dishes (order_id);

create index idx_restaurant_id
    on orders (restaurant_id);

create index idx_user_id
    on orders (user_id);

