-- Script de inicialización de bases de datos
-- Se ejecuta automáticamente cuando el contenedor MySQL arranca por primera vez.
-- El schema 'db_biblioteca' ya lo crea la variable MYSQL_DATABASE del compose.

CREATE DATABASE IF NOT EXISTS `ms-user`;
CREATE DATABASE IF NOT EXISTS `ms-autor`;
CREATE DATABASE IF NOT EXISTS `ms-libro`;
