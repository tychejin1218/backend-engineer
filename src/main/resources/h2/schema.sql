-- h2 테이블 생성
drop table if exists product;

create table product (
    id bigint auto_increment primary key,
    brand char(1) not null,
    category varchar(20) not null,
    price integer not null
);