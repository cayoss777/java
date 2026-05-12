-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-05-2026 a las 17:10:07
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bd_dos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `table_cliente`
--

CREATE TABLE `table_cliente` (
  `idCliente` int(11) NOT NULL,
  `Nombre_Cliente` varchar(45) NOT NULL,
  `Apellido_Cliente` varchar(45) NOT NULL,
  `Cedula_Cliente` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `table_cliente`
--

INSERT INTO `table_cliente` (`idCliente`, `Nombre_Cliente`, `Apellido_Cliente`, `Cedula_Cliente`) VALUES
(1, 'Isaias', 'Bravo', 2367),
(2, 'Claudio', 'Huancahuire', 7892),
(3, 'Elizabeth', 'Chipani', 4571);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `table_facturas`
--

CREATE TABLE `table_facturas` (
  `No_Facturas` int(11) NOT NULL,
  `cliente` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `vendedor` int(11) NOT NULL,
  `totals` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `table_facturas`
--

INSERT INTO `table_facturas` (`No_Facturas`, `cliente`, `fecha`, `vendedor`, `totals`) VALUES
(1, 2, '2026-05-02', 2, 2340.00),
(6, 1, '2026-05-10', 2, 11636.00),
(7, 1, '2026-05-12', 2, 4836.00),
(8, 2, '2026-05-12', 2, 4692.00),
(9, 2, '2026-05-12', 1, 5552.00),
(10, 1, '2026-05-12', 1, 1856.00),
(11, 3, '2026-05-12', 1, 60.00),
(12, 3, '2026-05-12', 2, 424.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `table_productos`
--

CREATE TABLE `table_productos` (
  `idProductos` int(11) NOT NULL,
  `nombreProductos` varchar(45) NOT NULL,
  `preciosProductos` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `table_productos`
--

INSERT INTO `table_productos` (`idProductos`, `nombreProductos`, `preciosProductos`) VALUES
(1, 'Impresora Laser', 800),
(2, 'Equipo medición Oxigeno', 1000),
(3, 'Tensiometro', 340),
(4, 'Rayos x', 2000),
(6, 'Linterna', 12);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `table_vendedor`
--

CREATE TABLE `table_vendedor` (
  `idVendedor` int(11) NOT NULL,
  `nombreVendedor` varchar(55) NOT NULL,
  `password` varchar(100) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `table_vendedor`
--

INSERT INTO `table_vendedor` (`idVendedor`, `nombreVendedor`, `password`) VALUES
(1, 'isaias', '1234'),
(2, 'yimi', 'abcd');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `table_ventas`
--

CREATE TABLE `table_ventas` (
  `idVentas` int(11) NOT NULL,
  `No_Facturas` int(11) NOT NULL,
  `Productos` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `importe` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `table_ventas`
--

INSERT INTO `table_ventas` (`idVentas`, `No_Facturas`, `Productos`, `cantidad`, `importe`) VALUES
(1, 6, 4, 3, 6000.00),
(2, 6, 4, 2, 4000.00),
(3, 1, 3, 1, 340.00),
(4, 1, 2, 1, 1000.00),
(5, 1, 2, 1, 1000.00),
(6, 7, 1, 1, 800.00),
(7, 7, 4, 2, 4000.00),
(8, 7, 6, 3, 36.00),
(9, 8, 6, 1, 12.00),
(10, 8, 2, 2, 2000.00),
(11, 8, 3, 2, 680.00),
(12, 8, 4, 1, 2000.00),
(13, 9, 1, 1, 800.00),
(14, 9, 4, 2, 4000.00),
(15, 9, 3, 2, 680.00),
(16, 9, 6, 2, 24.00),
(17, 9, 6, 4, 48.00),
(18, 10, 1, 1, 800.00),
(19, 10, 6, 3, 36.00),
(20, 10, 3, 3, 1020.00),
(21, 11, 6, 5, 60.00),
(22, 12, 6, 5, 60.00),
(23, 12, 3, 1, 340.00),
(24, 12, 6, 2, 24.00);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `table_cliente`
--
ALTER TABLE `table_cliente`
  ADD PRIMARY KEY (`idCliente`),
  ADD UNIQUE KEY `unique_cliente2` (`idCliente`);

--
-- Indices de la tabla `table_facturas`
--
ALTER TABLE `table_facturas`
  ADD PRIMARY KEY (`No_Facturas`),
  ADD UNIQUE KEY `unique_facturas` (`No_Facturas`,`vendedor`,`cliente`),
  ADD KEY `cliente_1` (`cliente`),
  ADD KEY `vendedor_1` (`vendedor`);

--
-- Indices de la tabla `table_productos`
--
ALTER TABLE `table_productos`
  ADD PRIMARY KEY (`idProductos`),
  ADD UNIQUE KEY `unique_producto2` (`idProductos`);

--
-- Indices de la tabla `table_vendedor`
--
ALTER TABLE `table_vendedor`
  ADD PRIMARY KEY (`idVendedor`),
  ADD UNIQUE KEY `unique_vendedor2` (`idVendedor`);

--
-- Indices de la tabla `table_ventas`
--
ALTER TABLE `table_ventas`
  ADD PRIMARY KEY (`idVentas`),
  ADD UNIQUE KEY `unique_ventas2` (`idVentas`,`No_Facturas`,`Productos`),
  ADD KEY `nrofacturas_1` (`No_Facturas`),
  ADD KEY `productos_1` (`Productos`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `table_ventas`
--
ALTER TABLE `table_ventas`
  MODIFY `idVentas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `table_facturas`
--
ALTER TABLE `table_facturas`
  ADD CONSTRAINT `table_facturas_ibfk_1` FOREIGN KEY (`cliente`) REFERENCES `table_cliente` (`idCliente`),
  ADD CONSTRAINT `table_facturas_ibfk_2` FOREIGN KEY (`vendedor`) REFERENCES `table_vendedor` (`idVendedor`);

--
-- Filtros para la tabla `table_ventas`
--
ALTER TABLE `table_ventas`
  ADD CONSTRAINT `table_ventas_ibfk_1` FOREIGN KEY (`No_Facturas`) REFERENCES `table_facturas` (`No_Facturas`),
  ADD CONSTRAINT `table_ventas_ibfk_2` FOREIGN KEY (`Productos`) REFERENCES `table_productos` (`idProductos`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
