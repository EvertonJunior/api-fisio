insert into tb_users (id, username, password, role) values (100, 'ana@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (101, 'maria@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (102, 'joaquina@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (103, 'eduardo@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (104, 'jose@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (105, 'ezequiel@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (106, 'ramos@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (107, 'carlos@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (108, 'juvaneide@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');

insert into physical_therapists (id, name, cpf, payment, id_user) values (2, 'Maria Silva', '27343573055', 10.00, 101);
insert into physical_therapists (id, name, cpf, payment, id_user) values (3, 'Joaquina Silva', '81858590000', 20.00, 102);
insert into physical_therapists (id, name, cpf, payment, id_user) values (4, 'Eduardo Silva', '71288012004', 30.00, 103);
insert into physical_therapists (id, name, cpf, payment, id_user) values (5, 'Jose Silva', '69001332072', 40.00, 104);
insert into physical_therapists (id, name, cpf, payment, id_user) values (6, 'Ezequiel Silva', '81397964090', 50.00, 105);
insert into physical_therapists (id, name, cpf, payment, id_user) values (7, 'Ramos Silva', '33213557013', 10.00, 106);
insert into physical_therapists (id, name, cpf, payment, id_user) values (8, 'Carlos Silva', '25228094075', 60.00, 107);

insert into care_types (id, name, code, price) values (20, 'Care 1', 'A-13', 10.00);
insert into care_types (id, name, code, price) values (21, 'Care 2', 'A-14', 20.00);
insert into care_types (id, name, code, price) values (22, 'Care 3', 'A-15', 30.00);
insert into care_types (id, name, code, price) values (23, 'Care 4', 'A-16', 40.00);

insert into hospitals (id, name, cnpj) values (10, 'Santa Casa', '12345678910111');
insert into hospitals (id, name, cnpj) values (11, 'Santa Marcelina', '12345678910112');
insert into hospitals (id, name, cnpj) values (12, 'Albert Einstein', '12345678910113');

insert into patients (id, name, cpf, id_hospital) values (30, 'Angelina Silva', '27343573055', 10);
insert into patients (id, name, cpf, id_hospital) values (31, 'Oswaldo Silva', '27343573056', 10);
insert into patients (id, name, cpf, id_hospital) values (32, 'Diniz Silva', '27343573057', 10);

insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (40, '2024-10-01 10:00:00', 20, 'Care 1',10.00,8,10,30,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (41, '2024-10-02 10:00:00', 20, 'Care 1',10.00,8,10,31,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (42, '2024-10-05 10:00:00', 20, 'Care 1',10.00,8,10,32,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (43, '2024-10-02 10:00:00', 21, 'Care 2',20.00,7,11,30,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (44, '2024-10-03 10:00:00', 21, 'Care 2',20.00,7,11,31,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (45, '2024-10-07 10:00:00', 21, 'Care 2',20.00,7,11,32,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (46, '2024-10-03 10:00:00', 22, 'Care 3',30.00,6,12,30,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (47, '2024-10-07 10:00:00', 22, 'Care 3',30.00,6,12,31,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (48, '2024-10-07 10:00:00', 22, 'Care 3',30.00,6,12,32,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (49, '2024-10-04 10:00:00', 20, 'Care 1',10.00,8,10,30,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (50, '2024-10-04 10:00:00', 20, 'Care 1',10.00,8,10,30,'paciente com dores');
insert into cares (id, care_date, id_care_type, name, price, id_physical_therapist, id_hospital, id_patient, description) values (51, '2024-10-05 10:00:00', 20, 'Care 1',10.00,8,10,30,'paciente com dores');

insert into slip_payment(id, numero_contrato) values (80, '3128557');
insert into slip_payment(id, numero_contrato) values (81, '3128557');

insert into invoices (id, date_first_care, date_last_care, id_hospital, total_value, code, payment_status) values (70, '2024-10-01',  '2024-10-07', 10, 60.00, '2024100120241007-12345678910111','AWAITING_PAYMENT');
insert into invoices (id, date_first_care, date_last_care, id_hospital, total_value, code, payment_status) values (71, '2024-10-01',  '2024-10-07', 10, 60.00, '2024100120241007-12345678910112','AWAITING_PAYMENT');