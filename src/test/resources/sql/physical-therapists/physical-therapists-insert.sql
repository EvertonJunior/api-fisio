insert into tb_users (id, username, password, role) values (100, 'ana@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (101, 'maria@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (102, 'joaquina@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (103, 'eduardo@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (104, 'jose@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (105, 'ezequiel@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (106, 'ramos@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (107, 'carlos@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (108, 'juvaneide@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');

insert into physical_therapists (id, name, cpf, id_user) values (20, 'Maria Silva', '27343573055', 101);
insert into physical_therapists (id, name, cpf, id_user) values (21, 'Joaquina Silva', '81858590000', 102);
insert into physical_therapists (id, name, cpf, id_user) values (22, 'Eduardo Silva', '71288012004', 103);
insert into physical_therapists (id, name, cpf, id_user) values (23, 'Jose Silva', '69001332072', 104);
insert into physical_therapists (id, name, cpf, id_user) values (24, 'Ezequiel Silva', '81397964090', 105);
insert into physical_therapists (id, name, cpf, id_user) values (25, 'Ramos Silva', '33213557013', 106);
insert into physical_therapists (id, name, cpf, id_user) values (26, 'Carlos Silva', '25228094075', 107);