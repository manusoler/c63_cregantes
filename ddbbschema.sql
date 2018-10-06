-- phpMyAdmin SQL Dump
-- version 4.4.12
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 15-09-2015 a las 21:36:20
-- Versión del servidor: 5.6.25
-- Versión de PHP: 5.6.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `cuaderno63`
--
CREATE DATABASE IF NOT EXISTS `cuaderno63` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `cuaderno63`;

DELIMITER $$
--
-- Funciones
--
DROP FUNCTION IF EXISTS `calculoApremio`$$
CREATE DEFINER=`cuaderno63`@`localhost` FUNCTION `calculoApremio`(`clave_recibo` INT(10), `f_hasta` DATE) RETURNS double
    NO SQL
BEGIN
DECLARE impApremio DOUBLE;
DECLARE importe DOUBLE;
DECLARE fec_fecha_noti DATE;
DECLARE intApremio DOUBLE;
DECLARE f_noti DATE;

SET f_noti =
(select f_notificacion
from recibos
where id_recibo = clave_recibo);

SET importe =
(select 
principal 
from recibos
where id_recibo = clave_recibo);


SET intApremio = (select 
if(estado = 'P', 
	 if(fase = 'V',
    	0,
        if(fase = 'A',
           (principal * 0.05),
    		if(estado_notificacion = '1',
				(principal * -1),
				(principal * 0.05)
		 )
        )
	) ,
 recargo	 
) 
from recibos
where id_recibo = clave_recibo);


IF (intApremio >= 0.0) THEN
  SET impApremio = intApremio;
ELSE

IF (day(f_noti) < '16') THEN

 SET fec_fecha_noti =  date_format(f_noti,'%Y-%m-20'); 
ELSE
 SET fec_fecha_noti = date_format(DATE_ADD(f_noti, INTERVAL 1 MONTH),'%Y-%m-05');
END IF;

IF (fec_fecha_noti >= f_hasta) THEN
  SET impApremio = importe * 0.10;
ELSE
	SET impApremio = importe * 0.20;
END IF;

END IF;
RETURN truncate(impApremio, 2);
END$$

DROP FUNCTION IF EXISTS `calculoDemora`$$
CREATE DEFINER=`cuaderno63`@`localhost` FUNCTION `calculoDemora`(`clave_recibo` INT(10), `f_hasta` DATE) RETURNS double
    NO SQL
BEGIN
DECLARE impDemora DOUBLE;
DECLARE importe DOUBLE;
DECLARE fec_fecha_noti DATE;
DECLARE f_desde DATE;
DECLARE intDemora DOUBLE;
DECLARE f_noti DATE;

SET f_noti =
(select f_notificacion
from recibos
where id_recibo = clave_recibo);

SET importe =
(select 
principal 
from recibos
where id_recibo = clave_recibo);

SET f_desde =
(select 
f_ejecutiva 
from recibos
where id_recibo = clave_recibo);


SET intDemora = (select 
if(estado = 'P', 
	 if(fase = 'V' or fase = 'A',
    	0,
    	if(estado_notificacion = '1',
			importe * -1,
			0 	
		 )
	) ,
 intereses	 
) 
from recibos
where id_recibo = clave_recibo);


IF (intDemora >= 0) THEN
  SET impDemora = intDemora;
ELSE

IF (day(f_noti) < 16) THEN
 SET fec_fecha_noti = date_format(f_noti,'%Y-%m-20');
ELSE
 SET fec_fecha_noti = 
date_format(DATE_ADD(f_noti, INTERVAL 1 MONTH),'%Y-%m-05');
END IF;

IF (fec_fecha_noti > f_hasta) THEN
  SET impDemora = 0;
ELSE

SET impDemora = (SELECT 
      SUM(
		(DATEDIFF(fechaMenor(fecha_hasta, f_hasta),fechaMayor(fecha_desde,f_desde))+1) 
		* 
		(porcentaje/(DATEDIFF(fecha_hasta, fecha_desde)))
		) 
       FROM intereses
      WHERE fecha_hasta > f_desde
    AND fecha_desde     < f_hasta);
    
END IF;
END IF;
RETURN TRUNCATE(impDemora * importe / 100, 2);
END$$

DROP FUNCTION IF EXISTS `fechaMayor`$$
CREATE DEFINER=`cuaderno63`@`localhost` FUNCTION `fechaMayor`(`fecha_1` DATE, `fecha_2` DATE) RETURNS date
    NO SQL
BEGIN
  IF fecha_1 > fecha_2 THEN
    RETURN fecha_1;
  ELSE
    RETURN fecha_2;
  END IF;
END$$

DROP FUNCTION IF EXISTS `fechaMenor`$$
CREATE DEFINER=`cuaderno63`@`localhost` FUNCTION `fechaMenor`(`fecha_1` DATE, `fecha_2` DATE) RETURNS date
    NO SQL
BEGIN
  IF fecha_1 < fecha_2 THEN
    RETURN fecha_1;
  ELSE
    RETURN fecha_2;
  END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bancosc63`
--

DROP TABLE IF EXISTS `bancosc63`;
CREATE TABLE IF NOT EXISTS `bancosc63` (
  `codigoentidad` varchar(3) NOT NULL,
  `c63banco` varchar(4) NOT NULL,
  `c63sucursal` varchar(4) DEFAULT NULL,
  `c63nombre` varchar(40) DEFAULT NULL,
  `c63codine` varchar(6) DEFAULT NULL,
  `c63iban` varchar(24) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contribuyentes`
--

DROP TABLE IF EXISTS `contribuyentes`;
CREATE TABLE IF NOT EXISTS `contribuyentes` (
  `id_contribuyente` int(11) NOT NULL,
  `cif_nif` varchar(9) DEFAULT NULL,
  `nombre_razon` varchar(100) DEFAULT NULL,
  `domicilio` varchar(100) DEFAULT NULL,
  `poblacion` varchar(50) DEFAULT NULL,
  `cp` varchar(5) DEFAULT NULL,
  `provincia` varchar(50) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entidades`
--

DROP TABLE IF EXISTS `entidades`;
CREATE TABLE IF NOT EXISTS `entidades` (
  `codigoentidad` varchar(3) NOT NULL COMMENT 'Código de la Entidad (Comunidad de Regante)',
  `nombreentidad` varchar(50) DEFAULT '50',
  `domicilioentidad` varchar(50) DEFAULT NULL,
  `localidadentidad` varchar(40) DEFAULT NULL,
  `cpostalentidad` varchar(5) DEFAULT NULL,
  `provinciaentidad` varchar(30) DEFAULT NULL,
  `telefonoentidad` varchar(20) DEFAULT NULL,
  `emailentidad` varchar(30) DEFAULT NULL,
  `nifentidad` varchar(9) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `intereses`
--

DROP TABLE IF EXISTS `intereses`;
CREATE TABLE IF NOT EXISTS `intereses` (
  `fecha_desde` date NOT NULL,
  `fecha_hasta` date NOT NULL,
  `denominacion` varchar(40) DEFAULT NULL,
  `porcentaje` double(6,4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Tabla con los porcentajes de los intereses de demora a aplicar para recardo demora';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recibos`
--

DROP TABLE IF EXISTS `recibos`;
CREATE TABLE IF NOT EXISTS `recibos` (
  `id_recibo` int(11) NOT NULL,
  `num_regante` varchar(15) DEFAULT NULL,
  `cod_objeto` varchar(30) DEFAULT NULL,
  `domicilio_objeto` varchar(50) DEFAULT NULL,
  `concepto` varchar(45) DEFAULT NULL,
  `ano` int(11) DEFAULT NULL,
  `periodo` int(11) DEFAULT NULL,
  `texto_periodo` varchar(30) DEFAULT NULL,
  `f_asamblea` date DEFAULT NULL,
  `principal` double(8,2) DEFAULT NULL,
  `recargo` double(8,2) DEFAULT NULL COMMENT 'RECARGO APREMIO (5% o 10%) en función de si ha sido o no notificado',
  `intereses` double(8,2) DEFAULT NULL,
  `costas` double(8,2) DEFAULT NULL,
  `ingresos_cuenta` double(8,2) DEFAULT NULL COMMENT 'ingresos en cuenta',
  `estado` varchar(1) DEFAULT 'P' COMMENT 'P - Pdte, C- Cobrado, A - Anulado',
  `f_estado` date DEFAULT NULL,
  `fase` varchar(1) DEFAULT 'E' COMMENT 'V - Voluntario, E - Ejecutivo, A - Apremio, X - Expendiente',
  `f_providencia` date DEFAULT NULL,
  `lote_apremio` varchar(6) DEFAULT NULL,
  `estado_notificacion` varchar(1) DEFAULT '0' COMMENT '0 - NO NOTIFICADO\n1 - NOTIFICADO\n2 - NOTIF. DEVUELTA\n3 - NOTIF. BOLETÍN',
  `f_notificacion` date DEFAULT NULL,
  `f_devolucion` date DEFAULT NULL,
  `lote_boletin` varchar(6) DEFAULT NULL,
  `observaciones` varchar(100) DEFAULT NULL,
  `codigoentidad` varchar(3) DEFAULT NULL,
  `id_contribuyente` int(11) DEFAULT NULL,
  `f_ejecutiva` date DEFAULT NULL,
  `lote_notificacion` varchar(6) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `bancosc63`
--
ALTER TABLE `bancosc63`
  ADD PRIMARY KEY (`codigoentidad`,`c63banco`);

--
-- Indices de la tabla `contribuyentes`
--
ALTER TABLE `contribuyentes`
  ADD PRIMARY KEY (`id_contribuyente`);

--
-- Indices de la tabla `entidades`
--
ALTER TABLE `entidades`
  ADD PRIMARY KEY (`codigoentidad`);

--
-- Indices de la tabla `intereses`
--
ALTER TABLE `intereses`
  ADD UNIQUE KEY `pku_fecha_desde` (`fecha_desde`),
  ADD KEY `pku_fecha_hasta` (`fecha_hasta`);

--
-- Indices de la tabla `recibos`
--
ALTER TABLE `recibos`
  ADD PRIMARY KEY (`id_recibo`),
  ADD UNIQUE KEY `EntCodObjConcepAnoPer` (`codigoentidad`,`cod_objeto`,`concepto`,`ano`,`periodo`),
  ADD KEY `ReciboEntidad_idx` (`codigoentidad`),
  ADD KEY `ReciboContri_idx` (`id_contribuyente`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `contribuyentes`
--
ALTER TABLE `contribuyentes`
  MODIFY `id_contribuyente` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `recibos`
--
ALTER TABLE `recibos`
  MODIFY `id_recibo` int(11) NOT NULL AUTO_INCREMENT;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `bancosc63`
--
ALTER TABLE `bancosc63`
  ADD CONSTRAINT `BancoEntidad` FOREIGN KEY (`codigoentidad`) REFERENCES `entidades` (`codigoentidad`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Filtros para la tabla `recibos`
--
ALTER TABLE `recibos`
  ADD CONSTRAINT `ReciboContri` FOREIGN KEY (`id_contribuyente`) REFERENCES `contribuyentes` (`id_contribuyente`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `ReciboEntidad` FOREIGN KEY (`codigoentidad`) REFERENCES `entidades` (`codigoentidad`) ON DELETE CASCADE ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
