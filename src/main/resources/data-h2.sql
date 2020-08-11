-- person table
insert into person (id, name, first_name) values (1, 'Muster', 'Hans');
insert into person (id, name, first_name) values (2, 'Meier', 'Peter');
insert into person (id, name, first_name) values (3, 'Maler', 'Ruedi');

-- user table
insert into user (id, login, administrator, person_id) values (1, 'loginMusterHans', TRUE, 1);
insert into user (id, login, administrator, person_id) values (2, 'loginMeierPeter', FALSE, 2);
insert into user (id, login, administrator, person_id) values (3, 'loginMalerPeter', FALSE, 3);

-- account table
insert into account (id, person_id, iban, account_type, balance, bank) values (1, 1, 'CH5489144226931961928', 'BusinessAccount', 99999.25, 'UBS');
insert into account (id, person_id, iban, account_type, balance, bank) values (2, 2, 'CH0989144668392457184', 'PrivateAccount', 12.25, 'Valiant');