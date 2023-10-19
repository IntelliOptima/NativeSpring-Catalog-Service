create table book (
    price float(53) not null,
    version integer not null,
    created_date timestamp(6) with time zone not null,
    id bigserial not null,
    last_modified_date timestamp(6) with time zone not null,
    isbn varchar(13) not null unique,
    author varchar(255) not null,
    publisher varchar(255),
    title varchar(255) not null,
    primary key (id)
);