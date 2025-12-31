-- TRUNCATE TABLES
DELETE FROM users;
DELETE FROM roles;

ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE roles AUTO_INCREMENT = 1;

INSERT INTO `roles` (`id`, `name`) VALUES 
(1, "superuser"),
(2, "administrator"),
(3, "viewer");

INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `password`, `role_id`) VALUES 
(1, "Andrés", "Gutiérrez Hurtado", "andres52885241@gmail.com", "$2a$12$kW8WFoPynAA1d7FNt/stU.qFzRclDfENJ9eIx7CNrSoKd2QfreUW.", 1);