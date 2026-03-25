CREATE EXTENSION IF NOT EXISTS vector;

-- TRUNCATE TABLES
TRUNCATE TABLE
product_specifications,
images,
products,
specification_values,
specifications,
sub_categories,
categories,
brands,
users,
roles
RESTART IDENTITY CASCADE;

-- ROLES
INSERT INTO roles (id, name) VALUES
(1, 'superuser'),
(2, 'administrator'),
(3, 'viewer');

-- USERS
INSERT INTO users (id, first_name, last_name, email, password, role_id) VALUES
(1, 'Andrés', 'Gutiérrez Hurtado', 'andres52885241@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 1),
(2, 'Wendy Alejandra', 'Navarro Arias', 'nwendy798@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 2),
(3, 'Amalia', 'Castro Ardila', 'amalia.castro.ardila@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 2),
(4, 'Andres Felipe', 'Quevedo Vega', 'andres.quevedo.vega@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 3),
(5, 'Jennifer', 'Fajardo', 'jennifer.fajardo@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 2),
(6, 'Carlos Alberto', 'Rodríguez Martínez', 'carlos.rodriguez.martinez@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 3),
(7, 'María Fernanda', 'López González', 'maria.lopez.gonzalez@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 2),
(8, 'Juan Pablo', 'Sánchez Torres', 'juan.sanchez.torres@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 3),
(9, 'Laura Camila', 'García Ramírez', 'laura.garcia.ramirez@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 2),
(10, 'Diego Alejandro', 'Morales Herrera', 'diego.morales.herrera@gmail.com', '$2a$10$Dvtv6WTlj3jSDzmdu5BzOO3wEOCTGVTz7cmjPi0X3uOXUhxBk/N6u', 3);

-- BRANDS
INSERT INTO brands (id, name) VALUES
(1, 'Otro'),
(2, 'Dell'),
(3, 'Lenovo'),
(4, 'ASUS'),
(5, 'Acer'),
(6, 'MSI'),
(7, 'Apple'),
(8, 'Samsung'),
(9, 'LG'),
(10, 'Logitech'),
(11, 'Razer'),
(12, 'Corsair'),
(13, 'Intel'),
(14, 'AMD'),
(15, 'NVIDIA'),
(16, 'Kingston'),
(17, 'Crucial'),
(18, 'Samsung'),
(19, 'Micron'),
(20, 'Kingston'),
(21, 'Crucial'),
(22, 'HP');

-- CATEGORIES
INSERT INTO categories (id, name, slug, description) VALUES
(1, 'Computadoras', 'computadoras', 'Laptops, desktops y todo tipo de computadoras'),
(2, 'Componentes', 'componentes', 'Procesadores, tarjetas gráficas, memorias y más'),
(3, 'Periféricos', 'perifericos', 'Monitores, teclados, mouse y accesorios');

-- SUB_CATEGORIES
INSERT INTO sub_categories (id, name, slug, description, category_id) VALUES
(1, 'Laptops', 'laptops', 'Computadoras portátiles', 1),
(2, 'Desktops', 'desktops', 'Computadoras de escritorio', 1),
(3, 'Monitores', 'monitores', 'Pantallas y monitores', 3),
(4, 'Teclados', 'teclados', 'Teclados mecánicos y de membrana', 3),
(5, 'Mouse', 'mouse', 'Ratones y mouse inalámbricos', 3);

-- SPECIFICATIONS FOR LAPTOPS
INSERT INTO specifications (id, name, slug, specification_type, unit, is_filterable, is_mandatory, sub_category_id) VALUES
(1, 'Procesador', 'procesador', 'text', NULL, true, true, 1),
(2, 'Memoria RAM', 'memoria-ram', 'number', 'GB', true, true, 1),
(3, 'Almacenamiento', 'almacenamiento', 'number', 'GB', true, true, 1),
(4, 'Tipo de Almacenamiento', 'tipo-almacenamiento', 'select', NULL, true, true, 1),
(5, 'Pantalla', 'pantalla', 'number', 'pulgadas', true, true, 1),
(6, 'Tarjeta Gráfica', 'tarjeta-grafica', 'text', NULL, true, false, 1),
(7, 'Sistema Operativo', 'sistema-operativo', 'select', NULL, true, false, 1);

-- SPECIFICATIONS FOR DESKTOPS
INSERT INTO specifications (id, name, slug, specification_type, unit, is_filterable, is_mandatory, sub_category_id) VALUES
(8, 'Procesador', 'procesador-desktop', 'text', NULL, true, true, 2),
(9, 'Memoria RAM', 'memoria-ram-desktop', 'number', 'GB', true, true, 2),
(10, 'Almacenamiento', 'almacenamiento-desktop', 'number', 'GB', true, true, 2),
(11, 'Tipo de Almacenamiento', 'tipo-almacenamiento-desktop', 'select', NULL, true, true, 2),
(12, 'Tarjeta Gráfica', 'tarjeta-grafica-desktop', 'text', NULL, true, false, 2),
(13, 'Fuente de Poder', 'fuente-poder-desktop', 'number', 'W', false, false, 2);

-- SPECIFICATIONS FOR MONITORES
INSERT INTO specifications (id, name, slug, specification_type, unit, is_filterable, is_mandatory, sub_category_id) VALUES
(14, 'Tamaño', 'tamano', 'number', 'pulgadas', true, true, 3),
(15, 'Resolución', 'resolucion', 'select', NULL, true, true, 3),
(16, 'Tasa de Refresco', 'tasa-refresco', 'number', 'Hz', true, false, 3),
(17, 'Tipo de Panel', 'tipo-panel', 'select', NULL, true, false, 3);

-- SPECIFICATIONS FOR TECLADOS
INSERT INTO specifications (id, name, slug, specification_type, unit, is_filterable, is_mandatory, sub_category_id) VALUES
(18, 'Tipo', 'tipo', 'select', NULL, true, true, 4),
(19, 'Conectividad', 'conectividad-keyboard', 'select', NULL, true, true, 4),
(20, 'Iluminación', 'iluminacion', 'select', NULL, false, false, 4);

-- SPECIFICATIONS FOR MOUSE
INSERT INTO specifications (id, name, slug, specification_type, unit, is_filterable, is_mandatory, sub_category_id) VALUES
(21, 'DPI', 'dpi', 'number', NULL, true, false, 5),
(22, 'Conectividad', 'conectividad-mouse', 'select', NULL, true, true, 5),
(23, 'Botones', 'botones', 'number', NULL, false, false, 5);

-- SPECIFICATION VALUES
INSERT INTO specification_values (id, specification_id, value, specification_order) VALUES
(1, 4, 'SSD', 1),
(2, 4, 'HDD', 2),
(3, 4, 'SSD NVMe', 3),
(4, 7, 'Windows 11', 1),
(5, 7, 'Windows 10', 2),
(6, 7, 'Linux', 3),
(7, 7, 'Sin Sistema Operativo', 4),
(8, 11, 'SSD', 1),
(9, 11, 'HDD', 2),
(10, 11, 'SSD NVMe', 3),
(11, 15, '1920x1080 (Full HD)', 1),
(12, 15, '2560x1440 (QHD)', 2),
(13, 15, '3840x2160 (4K UHD)', 3),
(14, 17, 'IPS', 1),
(15, 17, 'VA', 2),
(16, 17, 'TN', 3),
(17, 18, 'Mecánico', 1),
(18, 18, 'De Membrana', 2),
(19, 19, 'USB', 1),
(20, 19, 'Inalámbrico', 2),
(21, 19, 'Bluetooth', 3),
(22, 20, 'RGB', 1),
(23, 20, 'Unicolor', 2),
(24, 20, 'Sin Iluminación', 3),
(25, 22, 'USB', 1),
(26, 22, 'Inalámbrico', 2),
(27, 22, 'Bluetooth', 3);

-- PRODUCTS
INSERT INTO products (id, name, description, price, product_condition, discount, brand_id, sub_category_id, deleted_at, created_at, updated_at) VALUES
(1, 'HP Pavilion 15', 'Laptop HP Pavilion 15 con procesador Intel Core i5, ideal para trabajo y entretenimiento', 3899000, 'NEW', 0.0, 22, 1, NULL, NOW(), NOW()),
(2, 'Dell XPS 13', 'Laptop ultrabook Dell XPS 13 con pantalla InfinityEdge, perfecta para profesionales', 5899000, 'NEW', 5.0, 3, 1, NULL, NOW(), NOW()),
(3, 'Lenovo ThinkPad E14', 'Laptop empresarial Lenovo ThinkPad E14, robusta y confiable', 3499000, 'NEW', 0.0, 3, 1, NULL, NOW(), NOW()),
(4, 'ASUS ROG Strix G15', 'Laptop gaming ASUS ROG Strix G15 con tarjeta gráfica dedicada', 6899000, 'NEW', 10.0, 4, 1, NULL, NOW(), NOW()),
(5, 'HP Pavilion Desktop', 'Computadora de escritorio HP Pavilion con excelente rendimiento', 2999000, 'NEW', 0.0, 22, 2, NULL, NOW(), NOW()),
(6, 'Dell OptiPlex 7090', 'Desktop empresarial Dell OptiPlex 7090, compacta y potente', 3599000, 'REFURBISHED', 15.0, 2, 2, NULL, NOW(), NOW()),
(7, 'Samsung Odyssey G7', 'Monitor gaming Samsung Odyssey G7 de 32 pulgadas con 240Hz', 2599000, 'NEW', 0.0, 8, 3, NULL, NOW(), NOW()),
(8, 'LG UltraWide 34', 'Monitor ultrawide LG de 34 pulgadas, ideal para productividad', 2199000, 'NEW', 5.0, 9, 3, NULL, NOW(), NOW()),
(9, 'Logitech MX Keys', 'Teclado inalámbrico Logitech MX Keys con retroiluminación', 499000, 'NEW', 0.0, 10, 4, NULL, NOW(), NOW()),
(10, 'Razer BlackWidow V3', 'Teclado mecánico gaming Razer BlackWidow V3 con switches Razer', 649000, 'NEW', 10.0, 11, 4, NULL, NOW(), NOW()),
(11, 'Logitech MX Master 3', 'Mouse inalámbrico Logitech MX Master 3 con sensor de alta precisión', 489000, 'NEW', 0.0, 10, 5, NULL, NOW(), NOW()),
(12, 'Razer DeathAdder V2', 'Mouse gaming Razer DeathAdder V2 con sensor óptico de 20,000 DPI', 329000, 'NEW', 0.0, 11, 5, NULL, NOW(), NOW());

-- IMAGES
INSERT INTO images (id, url, is_main, product_id) VALUES
(1, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528048/ac-computers/medias/t3ewp0ezfrl99egnuwgv.png', true, 1),
(2, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775704932/ac-computers/medias/frdp456grlknoruazz05.png', false, 1),
(3, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528210/ac-computers/medias/sbrtgtrk3pjgcqarzctb.png', true, 2),
(4, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528244/ac-computers/medias/vfjz5zrnfeq89hepvycf.avif', true, 3),
(5, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528318/ac-computers/medias/nhtrbu6rzvgfanv5eghy.png', true, 4),
(6, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528369/ac-computers/medias/q8kkifemnoagxku8heh3.avif', true, 5),
(7, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528410/ac-computers/medias/p3pjucgnh9otmu7jqtj4.avif', true, 6),
(8, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528481/ac-computers/medias/vy9uccgk9bue5dhulm6o.webp', true, 7),
(9, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528520/ac-computers/medias/hg3bewzf609d1exrmnzh.png', true, 8),
(10, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528617/ac-computers/medias/cjlas23nuuqsw60yil73.png', true, 9),
(11, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528641/ac-computers/medias/v69gjclvwvn7jyj94pbi.png', true, 10),
(12, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528691/ac-computers/medias/utevj7wztkb3frrhwvqr.png', true, 11),
(13, 'https://res.cloudinary.com/dyuh7jesr/image/upload/v1775528714/ac-computers/medias/f6cxvdoqxjem7uo7x082.png', true, 12);

-- PRODUCT SPECIFICATIONS FOR LAPTOPS
-- HP Pavilion 15 (Product 1)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(1, 1, 1, 'Intel Core i5-1135G7', NULL),
(2, 1, 2, '8', NULL),
(3, 1, 3, '512', NULL),
(4, 1, 4, NULL, 3),
(5, 1, 5, '15.6', NULL),
(6, 1, 6, 'Intel Iris Xe Graphics', NULL),
(7, 1, 7, NULL, 4);

-- Dell XPS 13 (Product 2)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(8, 2, 1, 'Intel Core i7-1165G7', NULL),
(9, 2, 2, '16', NULL),
(10, 2, 3, '512', NULL),
(11, 2, 4, NULL, 3),
(12, 2, 5, '13.4', NULL),
(13, 2, 6, 'Intel Iris Xe Graphics', NULL),
(14, 2, 7, NULL, 4);

-- Lenovo ThinkPad E14 (Product 3)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(15, 3, 1, 'AMD Ryzen 5 5500U', NULL),
(16, 3, 2, '8', NULL),
(17, 3, 3, '256', NULL),
(18, 3, 4, NULL, 3),
(19, 3, 5, '14', NULL),
(20, 3, 6, 'AMD Radeon Graphics', NULL),
(21, 3, 7, NULL, 4);

-- ASUS ROG Strix G15 (Product 4)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(22, 4, 1, 'AMD Ryzen 7 5800H', NULL),
(23, 4, 2, '16', NULL),
(24, 4, 3, '1000', NULL),
(25, 4, 4, NULL, 3),
(26, 4, 5, '15.6', NULL),
(27, 4, 6, 'NVIDIA GeForce RTX 3060', NULL),
(28, 4, 7, NULL, 4);

-- PRODUCT SPECIFICATIONS FOR DESKTOPS
-- HP Pavilion Desktop (Product 5)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(29, 5, 8, 'Intel Core i5-10400', NULL),
(30, 5, 9, '8', NULL),
(31, 5, 10, '1000', NULL),
(32, 5, 11, NULL, 2),
(33, 5, 12, 'Intel UHD Graphics 630', NULL),
(34, 5, 13, '300', NULL);

-- Dell OptiPlex 7090 (Product 6)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(35, 6, 8, 'Intel Core i7-11700', NULL),
(36, 6, 9, '16', NULL),
(37, 6, 10, '512', NULL),
(38, 6, 11, NULL, 3),
(39, 6, 12, 'Intel UHD Graphics 750', NULL),
(40, 6, 13, '500', NULL);

-- PRODUCT SPECIFICATIONS FOR MONITORES
-- Samsung Odyssey G7 (Product 7)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(41, 7, 14, '32', NULL),
(42, 7, 15, NULL, 12),
(43, 7, 16, '240', NULL),
(44, 7, 17, NULL, 15);

-- LG UltraWide 34 (Product 8)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(45, 8, 14, '34', NULL),
(46, 8, 15, NULL, 12),
(47, 8, 16, '75', NULL),
(48, 8, 17, NULL, 14);

-- PRODUCT SPECIFICATIONS FOR TECLADOS
-- Logitech MX Keys (Product 9)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(49, 9, 18, NULL, 18),
(50, 9, 19, NULL, 21),
(51, 9, 20, NULL, 23);

-- Razer BlackWidow V3 (Product 10)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(52, 10, 18, NULL, 17),
(53, 10, 19, NULL, 19),
(54, 10, 20, NULL, 22);

-- PRODUCT SPECIFICATIONS FOR MOUSE
-- Logitech MX Master 3 (Product 11)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(55, 11, 21, '4000', NULL),
(56, 11, 22, NULL, 26),
(57, 11, 23, '7', NULL);

-- Razer DeathAdder V2 (Product 12)
INSERT INTO product_specifications (id, product_id, specification_id, value, id_value) VALUES
(58, 12, 21, '20000', NULL),
(59, 12, 22, NULL, 25),
(60, 12, 23, '8', NULL);

-- PostgreSQL: INSERT ... (id, ...) does not advance SERIAL/IDENTITY sequences. Without this, the next
-- JPA INSERT reuses e.g. id=1 and hits duplicate key (e.g. on images after product update).
SELECT setval(pg_get_serial_sequence('roles', 'id'), COALESCE((SELECT MAX(id) FROM roles), 0));
SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE((SELECT MAX(id) FROM users), 0));
SELECT setval(pg_get_serial_sequence('brands', 'id'), COALESCE((SELECT MAX(id) FROM brands), 0));
SELECT setval(pg_get_serial_sequence('categories', 'id'), COALESCE((SELECT MAX(id) FROM categories), 0));
SELECT setval(pg_get_serial_sequence('sub_categories', 'id'), COALESCE((SELECT MAX(id) FROM sub_categories), 0));
SELECT setval(pg_get_serial_sequence('specifications', 'id'), COALESCE((SELECT MAX(id) FROM specifications), 0));
SELECT setval(pg_get_serial_sequence('specification_values', 'id'), COALESCE((SELECT MAX(id) FROM specification_values), 0));
SELECT setval(pg_get_serial_sequence('products', 'id'), COALESCE((SELECT MAX(id) FROM products), 0));
SELECT setval(pg_get_serial_sequence('images', 'id'), COALESCE((SELECT MAX(id) FROM images), 0));
SELECT setval(pg_get_serial_sequence('product_specifications', 'id'), COALESCE((SELECT MAX(id) FROM product_specifications), 0));
