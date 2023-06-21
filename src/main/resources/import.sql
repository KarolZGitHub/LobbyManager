-- Tabela "ranks"
INSERT INTO ranks (name) VALUES ('Silver 1'), ('Silver 2'), ('Silver 3'), ('Silver 4'), ('Silver 5'), ('Gold 1'), ('Gold 2'), ('Gold 3'), ('Gold 4'), ('Gold 5');
-- Tabela "games"
INSERT INTO games (id, rank_id, name) VALUES (1, 1, 'League of Legends'), (2, 2, 'League of Legends'), (3, 3, 'League of Legends'), (4, 4, 'League of Legends'), (5, 5, 'League of Legends'), (6, 1, 'CS:GO'), (7, 2, 'CS:GO'), (8, 3, 'CS:GO'), (9, 4, 'CS:GO'), (10, 5, 'CS:GO'), (11, 1, 'Apex Legends'), (12, 2, 'Apex Legends'), (13, 3, 'Apex Legends'), (14, 4, 'Apex Legends'), (15, 5, 'Apex Legends'), (16, 1, 'FIFA 22'), (17, 2, 'FIFA 22'), (18, 3, 'FIFA 22'), (19, 4, 'FIFA 22'), (20, 5, 'FIFA 22');
INSERT INTO roles (id, name) VALUES (1, 'ADMIN'), (2,'USER');
INSERT INTO users (created, id, userName,password, email) VALUES ('2023-06-20 17:43:19.720676',1 , 'admin', '$2a$10$gNRzHOipT7fngvy8CqMfIOFJR7QFn4IG1D41JTYRwuBrA/J3RZwiK', 'admin@wp.pl'), ('2023-06-20 17:43:19.720676',2 , 'Karol', '$2a$10$gNRzHOipT7fngvy8CqMfIOFJR7QFn4IG1D41JTYRwuBrA/J3RZwiK', 'karol@wp.pl');
INSERT INTO user_role (ROLE_ID, USER_ID) VALUES (1,1), (2,2);