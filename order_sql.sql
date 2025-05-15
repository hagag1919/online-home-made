create database ordersdb;
use database ordersdb;
create table Orders
(
    id               bigint auto_increment
        primary key,
    user_id          int                            not null,
    restaurant_id    int                            not null,
    total_price      decimal(10, 2)                 not null,
    destination      varchar(255)                   not null,
    shipping_company varchar(100)                   null,
    status           varchar(255) default 'Pending' null,
    dishes           json                           not null
);

create table order_dishes_link
(
    order_id bigint not null,
    dish_id  bigint not null,
    primary key (order_id, dish_id),
    constraint order_dishes_link_ibfk_1
        foreign key (order_id) references orders (id)
);

create index idx_order_id
    on order_dishes_link (order_id);

create index idx_restaurant_id
    on Orders (restaurant_id);

create index idx_user_id
    on Orders (user_id);

