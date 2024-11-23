-- h2 테이블 생성
drop table if exists product;

create table product (
    id int auto_increment primary key,
    brand varchar(1) not null,
    category varchar(50) not null,
    price int not null
);