CREATE DATABASE IF NOT EXISTS `miTienda` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `miTienda`;

CREATE TABLE IF NOT EXISTS `rol` (
  `id_rol` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(255) DEFAULT NULL,
  `nombre` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `categoria` (
  `id_categoria` INT(11) NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(100) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `formas_de_pago` (
  `id_forma_pago` INT(11) NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(100) NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_forma_pago`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `producto` (
  `id_producto` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(255) NOT NULL,
  `fecha_creacion` DATETIME(6) NOT NULL,
  `imagen` VARCHAR(255) DEFAULT NULL,
  `nombre` VARCHAR(255) NOT NULL,
  `precio` DECIMAL(38,2) NOT NULL,
  `stock` INT(11) NOT NULL,
  `id_categoria` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  KEY `FK_producto_categoria` (`id_categoria`),
  CONSTRAINT `FK_producto_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `contraseña` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `fecha_creacion` DATETIME(6) NOT NULL,
  `usuario` VARCHAR(255) NOT NULL,
  `id_rol` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  KEY `FK_usuario_rol` (`id_rol`),
  CONSTRAINT `FK_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `direccion` (
  `id_direccion` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `calle` VARCHAR(255) NOT NULL,
  `ciudad` VARCHAR(255) NOT NULL,
  `codigo_postal` VARCHAR(255) NOT NULL,
  `estado` VARCHAR(255) NOT NULL,
  `pais` VARCHAR(255) NOT NULL,
  `tipo` ENUM('CASA', 'FACTURACION', 'TRABAJO') NOT NULL,
  `id_usuario` BIGINT(20) NOT NULL,
  `condicion` enum('ACTIVO','ELIMINADO','INACTIVO') NOT NULL,
  PRIMARY KEY (`id_direccion`),
  KEY `FK_direccion_usuario` (`id_usuario`),
  CONSTRAINT `FK_direccion_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `carrito` (
  `id_carrito` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cantidad` INT(11) NOT NULL,
  `fecha_agregado` DATETIME(6) NOT NULL,
  `id_producto` BIGINT(20) NOT NULL,
  `id_usuario` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id_carrito`),
  KEY `FK_carrito_producto` (`id_producto`),
  KEY `FK_carrito_usuario` (`id_usuario`),
  CONSTRAINT `FK_carrito_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`),
  CONSTRAINT `FK_carrito_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `pedido` (
  `id_pedido` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `fecha` DATETIME(6) NOT NULL,
  `total` DOUBLE NOT NULL,
  `id_usuario` BIGINT(20) NOT NULL,
  `id_forma_pago` INT(11) NOT NULL,
  `id_direccion` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id_pedido`),
  KEY `FK_pedido_usuario` (`id_usuario`),
  KEY `FK_pedido_forma_pago` (`id_forma_pago`),
  KEY `FK_pedido_direccion` (`id_direccion`),
  CONSTRAINT `FK_pedido_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `FK_pedido_forma_pago` FOREIGN KEY (`id_forma_pago`) REFERENCES `formas_de_pago` (`id_forma_pago`),
  CONSTRAINT `FK_pedido_direccion` FOREIGN KEY (`id_direccion`) REFERENCES `direccion` (`id_direccion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `detalle_pedido` (
  `id_detalle` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cantidad` INT(11) NOT NULL,
  `precio` DOUBLE NOT NULL,
  `id_pedido` BIGINT(20) NOT NULL,
  `id_producto` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id_detalle`),
  KEY `FK_detalle_pedido` (`id_pedido`),
  KEY `FK_detalle_producto` (`id_producto`),
  CONSTRAINT `FK_detalle_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`),
  CONSTRAINT `FK_detalle_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
