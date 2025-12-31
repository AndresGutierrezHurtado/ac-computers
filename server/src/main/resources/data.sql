-- Roles
INSERT INTO `roles` (`id`, `name`) VALUES
    (1, "superuser"),
    (2, "admin"),
    (3, "viewer");

-- Users with password: 12345
INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `password`, `role_id`) VALUES
    (1, "Amalia", "Castro Ardila", "amalia@gmail.com", "$2a$12$BOPUR994vYf2VCTU1lfSM.IBxVklIp/R6rVhIp1tIH48IYG6hoQe.", 1),
    (2, "Andrés", "Gutiérrez Hurtado", "andres52885241@gmail.com", "$2a$12$BOPUR994vYf2VCTU1lfSM.IBxVklIp/R6rVhIp1tIH48IYG6hoQe.", 2),
    (3, "Test", "User", "test@gmail.com", "$2a$12$BOPUR994vYf2VCTU1lfSM.IBxVklIp/R6rVhIp1tIH48IYG6hoQe.", 3);
