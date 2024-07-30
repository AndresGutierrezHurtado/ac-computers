DROP DATABASE IF EXISTS `ac-computers`;
CREATE DATABASE `ac-computers`;
USE `ac-computers`;


DROP TABLE IF EXISTS `products`;


CREATE TABLE `products` (
    `product_id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `product_name` VARCHAR(255) NOT NULL,
    `product_description` VARCHAR(255) NOT NULL,
    `product_price` DECIMAL(10, 0) NOT NULL,
    `product_discount` INT,
    `product_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `product_image_url` VARCHAR(255) NOT NULL DEFAULT '/images/products/bg1.png'
);

INSERT INTO `products` (`product_name`, `product_description`, `product_price`, `product_discount`, `product_date`) VALUES
('Motherboard Gigabyte Z490 Aorus Elite Ac Wifi 1200 Gamer Pc', 'Placa base para jugadores con Wi-Fi integrado y soporte para procesadores Intel de décima generación.', 1200000, 10, '2024-01-05 10:15:00'),
('Tarjeta de video Nvidia Gigabyte Eagle GeForce RTX 40 Series RTX 4060 8GB', 'Tarjeta gráfica de alta gama con 8GB de VRAM para juegos y aplicaciones profesionales.', 2296000, 15, '2024-01-15 14:30:00'),
('Gabinete Gamer Vidrio Templado lhc-31', 'Gabinete con diseño moderno y panel de vidrio templado para mostrar los componentes internos.', 450000, NULL, '2024-01-25 08:45:00'),
('Procesador Ryzen 5500G', 'Procesador de alto rendimiento con gráficos integrados, ideal para multitarea y juegos ligeros.', 676000, 5, '2024-02-05 09:00:00'),
('MOUSE COUGAR 700M', 'Mouse ergonómico y ajustable con precisión de 16,000 DPI, ideal para juegos.', 185000, NULL, '2024-02-15 12:15:00'),
('CAPTURADORA DE VIDEO EL GATO 4K60 PRO MK.2', 'Capturadora de video para grabación en 4K a 60 FPS, perfecta para streamers y creadores de contenido.', 1266000, 20, '2024-02-25 11:00:00'),
('COMBO VSG CRUX TECLADO + MOUSE + DIADEMA + PAD MOUSE', 'Set completo de periféricos gaming con iluminación LED.', 174000, NULL, '2024-03-05 10:30:00'),
('MONITOR LG 24″ ULTRAGEAR 24GN60R', 'Monitor gaming de 24 pulgadas con tasa de refresco de 144Hz y tecnología FreeSync.', 881000, 10, '2024-03-15 15:00:00'),
('FUENTE COOLER MASTER MWE 750W', 'Fuente de poder de 750W con certificación 80 PLUS Bronze, ideal para configuraciones de alto rendimiento.', 387000, NULL, '2024-03-25 13:45:00'),
('Memoria Ram DDR5 RGB 2800MHZ', 'Módulo de RAM DDR5 con iluminación RGB y velocidad de 2800MHz, ideal para gaming y tareas intensivas.', 570000, NULL, '2024-04-05 16:00:00'),
('Webcam Logitech C920 HD Pro', 'Webcam de alta definición con enfoque automático y micrófono estéreo integrado.', 250000, NULL, '2024-04-25 11:45:00'),
('Teclado mecánico Corsair K70 RGB MK.2', 'Teclado mecánico con iluminación RGB y switches Cherry MX, ideal para gamers.', 380000, 10, '2024-05-05 09:15:00'),
('Duericulares inalámbricos SteelSeries Arctis 7', 'Auriculares inalámbricos con sonido envolvente 7.1 y micrófono retráctil.', 650000, NULL, '2024-05-15 12:30:00'),
('Tarjeta de video ASUS ROG Strix GeForce RTX 3080', 'Tarjeta gráfica de alta gama con 10GB de VRAM, ideal para juegos en 4K y realidad virtual.', 5500000, 15, '2024-05-25 10:45:00'),
('SSD NVMe Western Digital SN750 1TB', 'SSD NVMe con velocidad de lectura/escritura ultra rápida, ideal para almacenamiento de alto rendimiento.', 550000, 5, '2024-06-05 11:15:00'),
('Monitor Curvo Samsung Odyssey G7 27" QLED', 'Monitor curvo de 27 pulgadas con tecnología QLED y resolución WQHD.', 1800000, NULL, '2024-06-15 14:30:00'),
('Tarjeta madre MSI B550 Tomahawk', 'Placa base con soporte para procesadores AMD Ryzen de tercera generación y PCIe 4.0.', 650000, NULL, '2024-06-25 16:00:00'),
('Mousepad Gaming Corsair MM800 RGB Polaris', 'Mousepad con iluminación RGB personalizable y superficie de microtextura para precisión en los movimientos.', 180000, NULL, '2024-07-05 09:45:00'),
('Sistema de altavoces Logitech Z623 2.1', 'Sistema de altavoces con subwoofer y sonido envolvente, certificado por THX.', 550000, NULL, '2024-07-15 13:00:00'),
('Microsfonos de condensador Blue Yeti USB', 'Micrófono de condensador USB con múltiples patrones de captura de audio, ideal para podcast y streaming.', 500000, 10, '2024-07-25 15:15:00'),
('Silla Gamer DXRacer Formula Series', 'Silla ergonómica para gaming con soporte lumbar y ajuste de altura.', 1200000, NULL, '2024-01-12 17:30:00'),
('Adaptador Wi-Fi USB TP-Link Archer T3U', 'Adaptador USB de doble banda para mejorar la conectividad Wi-Fi de tu PC o laptop.', 80000, NULL, '2024-02-18 08:45:00'),
('Tarjeta de video MSI GeForce GTX 1650 Super Gaming X', 'Tarjeta gráfica con 4GB de VRAM, ideal para juegos en Full HD.', 900000, NULL, '2024-03-22 11:00:00'),
('Kit de refrigeración líquida Corsair Hydro Series H100i RGB Platinum', 'Sistema de refrigeración líquida con iluminación RGB y radiador de 240mm.', 800000, NULL, '2024-04-28 13:15:00');