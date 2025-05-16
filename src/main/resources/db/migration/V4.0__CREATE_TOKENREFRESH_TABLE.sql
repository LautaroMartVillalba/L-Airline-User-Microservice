create table entity_token (
id bigint not null,
create_at timestamp(6),
email varchar(255),
token varchar(255),
user_id bigint, primary key (id));

alter table if exists entity_token add constraint unique_email_constraint unique (email);
alter table if exists entity_token add constraint unique_token_constraint unique (token);
alter table if exists entity_token add constraint user_foreign_key foreign key (user_id) references entity_user;

