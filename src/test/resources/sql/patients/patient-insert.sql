insert into tb_users (id, username, password, role) values (100, 'ana@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (101, 'maria@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (102, 'joaquina@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (103, 'eduardo@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (104, 'jose@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (105, 'ezequiel@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (106, 'ramos@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (107, 'carlos@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');
insert into tb_users (id, username, password, role) values (108, 'juvaneide@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');

insert into hospitals (id, name, cnpj) values (10, 'Santa Casa', '12345678910111');
insert into hospitals (id, name, cnpj) values (11, 'Santa Marcelina', '12345678910112');
insert into hospitals (id, name, cnpj) values (12, 'Albert Einstein', '12345678910113');

insert into patients (id, name, cpf, id_hospital) values (30, 'Angelina Silva', '82539082025', 10);
insert into patients (id, name, cpf, id_hospital) values (31, 'Oswaldo Silva', '10674026080', 10);
insert into patients (id, name, cpf, id_hospital) values (32, 'Diniz Silva', '32525556038', 10);

