insert into tb_users (id, username, password, role) values (100, 'ana@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (101, 'maria@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (102, 'joaquina@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (107, 'carlos@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_users (id, username, password, role) values (108, 'juvaneide@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_FISIO');


insert into tb_managers (id, name, cpf, salary, employee_position, id_user) values (20, 'Maria Silva', '27343573055', 9000, 'Gerente', 101);
insert into tb_managers (id, name, cpf, salary, employee_position, id_user) values (21, 'Joaquina Silva', '81858590000', 6000, 'Coordenador', 102);
insert into tb_managers (id, name, cpf, salary, employee_position, id_user) values (22, 'Carlos Silva', '71288012004', 3500, 'Supervisor', 107);