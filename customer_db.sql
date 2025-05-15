create table users
(
    id       bigint auto_increment
        primary key,
    username varchar(50)  not null,
    password varchar(255) not null,
    constraint username
        unique (username)
);

create table order_status
(
    id         bigint auto_increment
        primary key,
    user_id    bigint                              not null,
    payment_id varchar(100)                        null,
    amount     decimal(10, 2)                      null,
    currency   varchar(10)                         null,
    status     varchar(50)                         null,
    reason     text                                null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    constraint order_status_ibfk_1
        foreign key (user_id) references users (id)
);

create index user_id
    on order_status (user_id);