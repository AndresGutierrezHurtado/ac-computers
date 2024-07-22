DROP DATABASE IF EXISTS `ac-computers`;
CREATE DATABASE `ac-computers`;
USE `ac-computers`;

CREATE TABLE `products` (
    `product_id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `product_name` VARCHAR(255) NOT NULL,
    `product_price` DECIMAL(10, 0) NOT NULL,
    `product_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `image_url` VARCHAR(255) NOT NULL DEFAULT '/images/no-image.jpg'
);

INSERT INTO `products` (`product_name`, `product_price`, `product_date`) VALUES
('Motherboard Gigabyte Z490 Aorus Elite Ac Wifi 1200 Gamer Pc', 1200000, '2024-01-05 10:15:00'),
('Tarjeta de video Nvidia Gigabyte Eagle GeForce RTX 40 Series RTX 4060 8GB', 2296000, '2024-01-15 14:30:00'),
('Gabinete Gamer Vidrio Templado lhc-31', 450000, '2024-01-25 08:45:00'),
('Procesador Ryzen 5500G', 676000, '2024-02-05 09:00:00'),
('MOUSE COUGAR 700M', 185000, '2024-02-15 12:15:00'),
('CAPTURADORA DE VIDEO EL GATO 4K60 PRO MK.2', 1266000, '2024-02-25 11:00:00'),
('COMBO VSG CRUX TECLADO + MOUSE + DIADEMA + PAD MOUSE', 174000, '2024-03-05 10:30:00'),
('MONITOR LG 24″ ULTRAGEAR 24GN60R', 881000, '2024-03-15 15:00:00'),
('FUENTE COOLER MASTER MWE 750W', 387000, '2024-03-25 13:45:00'),
('Memoria Ram DDR5 RGB 2800MHZ', 570000, '2024-04-05 16:00:00'),
('Webcam Logitech C920 HD Pro', 250000, '2024-04-25 11:45:00'),
('Teclado mecánico Corsair K70 RGB MK.2', 380000, '2024-05-05 09:15:00'),
('Duericulares inalámbricos SteelSeries Arctis 7', 650000, '2024-05-15 12:30:00'),
('Tarjeta de video ASUS ROG Strix GeForce RTX 3080', 5500000, '2024-05-25 10:45:00'),
('SSD NVMe Western Digital SN750 1TB', 550000, '2024-06-05 11:15:00'),
('Monitor Curvo Samsung Odyssey G7 27" QLED', 1800000, '2024-06-15 14:30:00'),
('Tarjeta madre MSI B550 Tomahawk', 650000, '2024-06-25 16:00:00'),
('Mousepad Gaming Corsair MM800 RGB Polaris', 180000, '2024-07-05 09:45:00'),
('Sistema de altavoces Logitech Z623 2.1', 550000, '2024-07-15 13:00:00'),
('Microsfonos de condensador Blue Yeti USB', 500000, '2024-07-25 15:15:00'),
('Silla Gamer DXRacer Formula Series', 1200000, '2024-01-12 17:30:00'),
('Adaptador Wi-Fi USB TP-Link Archer T3U', 80000, '2024-02-18 08:45:00'),
('Tarjeta de video MSI GeForce GTX 1650 Super Gaming X', 900000, '2024-03-22 11:00:00'),
('Kit de refrigeración líquida Corsair Hydro Series H100i RGB Platinum', 800000, '2024-04-28 13:15:00');