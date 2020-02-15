create table price (
id bigint not null,
product_code varchar(255),
depart integer not null,
price_begin timestamp,
price_end timestamp,
price_number integer not null,
price_value bigint,
primary key (id));

alter table price drop constraint if exists product_code_number_depard_unic;
alter table price add constraint product_code_price_number_depart_unic unique
(product_code, price_number, depart);

create sequence hibernate_sequence start with 3 increment by 1;