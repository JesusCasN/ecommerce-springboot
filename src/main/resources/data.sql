INSERT INTO `categoria` (`id_categoria`, `descripcion`, `nombre`) VALUES
	(1, 'Bebidas carbonatadas y refrescantes', 'Refrescos'),
	(2, 'Aceites de cocina y alimentos', 'Aceite'),
	(3, 'Cereales para el desayuno y snacks', 'Cereales'),
	(4, 'Lácteos y productos derivados', 'Leche');

INSERT INTO `formas_de_pago` (`id_forma_pago`, `descripcion`, `nombre`) VALUES
	(1, 'Pago mediante depósito bancario o transferencia', 'Depósito o Transferencia');

INSERT INTO `producto` (`id_producto`, `descripcion`, `fecha_creacion`, `imagen`, `nombre`, `precio`, `stock`, `id_categoria`) VALUES
	(1, 'Refresco de 2L', '2025-02-02 00:37:19.000000', 'producto1.png', 'Pepsi', 36.00, 99, 1),
	(2, 'Aceite de 750ml', '2025-02-02 00:37:19.000000', 'producto2.png', 'Ave', 30.00, 88, 2),
	(3, 'Cereal de 600gr', '2025-02-02 00:37:19.000000', 'producto3.png', 'Zucaritas', 65.00, 94, 3),
	(4, 'Leche de 1L', '2025-02-02 00:37:19.000000', 'producto4.png', 'Santa Clara', 32.00, 97, 4),
	(5, 'Refresco de 2.5L', '2025-02-02 00:37:19.000000', 'producto5.png', 'Coca Cola', 55.00, 97, 1),
	(6, 'Sprite de 600ml', '2025-02-27 18:48:39.000000', 'producto6.png', 'Sprite', 20.00, 99, 1),
	(7, 'Red Cola de 600ml', '2025-02-27 18:48:39.000000', 'producto7.png', 'Red Cola', 20.00, 100, 1),
	(8, 'Cereal de 240g', '2025-02-27 18:48:39.000000', 'producto8.png', 'Choco Krispis', 45.00, 100, 3),
	(9, 'Cereal 500g', '2025-02-27 18:48:39.000000', 'producto9.png', 'Pan Integral', 70.00, 100, 3),
	(10, 'Leche de 3L', '2025-02-27 18:48:39.000000', 'producto10.png', 'Lala entera', 85.00, 100, 4),
	(11, 'Leche de 1L', '2025-02-27 18:48:39.000000', 'producto11.png', 'Nutri Fresa', 32.00, 100, 4),
	(17, 'Refresco de 600ml', '2025-02-27 23:33:18.724697', '3c819547-3017-4e36-93f9-4910ab39bc74_manzanitaSol.png', 'Manzanita Sol', 24.00, 100, 1),
	(18, 'Aceite de 500ml', '2025-02-27 23:39:37.886099', 'a3e24703-aba2-4e4a-b89b-47e2366a66e1_producto15.png', 'Aceite 123', 27.00, 98, 2);

INSERT INTO `rol` (`id_rol`, `descripcion`, `nombre`) VALUES
	(1, 'Administrador del sistema', 'ADMIN'),
	(2, 'Usuario regular del sistema', 'USER');

INSERT INTO `usuario` (`id_usuario`, `contraseña`, `email`, `fecha_creacion`, `usuario`, `id_rol`) VALUES
	(1, '$2a$12$SwKaYd8mezSlhEbKXeKVYuhiZN2aFgl1kQFSoKj4IUK2LGkKi/Jly', 'admin@example.com', '2025-03-02 18:43:12.000000', 'Jesus Castillo', 1);