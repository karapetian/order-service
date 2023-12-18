CREATE DATABASE if not exists ordersDB;

create table if not exists ordersDB.customer
(
    id           bigint auto_increment
    primary key,
    email        varchar(255) not null,
    name         varchar(255) not null,
    phone_number varchar(255) not null,
    surname      varchar(255) null
    );

create table if not exists ordersDB.orders
(
    id                  bigint auto_increment
    primary key,
    current_status      enum ('CANCELED', 'DELIVERED', 'PAID', 'PENDING', 'PROCESSING', 'REFUNDED', 'SHIPPED') null,
    delivered_date      datetime(6)                                                                            null,
    order_creation_date datetime(6)                                                                            null,
    payment_date        datetime(6)                                                                            null,
    payment_details     varchar(255)                                                                           null,
    shipped_date        datetime(6)                                                                            null,
    shipping_address    varchar(255)                                                                           null,
    customer_id         bigint                                                                                 not null,
    constraint FK624gtjin3po807j3vix093tlf
    foreign key (customer_id) references ordersDB.customer (id)
    );


create table if not exists ordersDB.order_item
(
    id       bigint auto_increment
    primary key,
    product  varchar(255) not null,
    quantity int          not null,
    order_id bigint       not null,
    constraint FKt4dc2r9nbvbujrljv3e23iibt
    foreign key (order_id) references ordersDB.orders (id)
    );

create table if not exists ordersDB.order_history
(
    id            bigint auto_increment
    primary key,
    modified_date datetime(6)                                                                            null,
    order_status  enum ('CANCELED', 'DELIVERED', 'PAID', 'PENDING', 'PROCESSING', 'REFUNDED', 'SHIPPED') not null,
    order_id      bigint                                                                                 not null,
    constraint FKnw2ljd8jnpdc9y2ild52e79t2
    foreign key (order_id) references ordersDB.orders (id)
    );
