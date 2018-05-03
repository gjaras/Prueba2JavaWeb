CREATE DATABASE RRHH;
USE RRHH;

CREATE TABLE usuario
  (
    login VARCHAR (10) ,
    password VARCHAR (40) ,
    nombre VARCHAR (50) 
  ) ;
ALTER TABLE usuario ADD CONSTRAINT usuario_PK PRIMARY KEY ( login ) ;

CREATE TABLE personal
  (
    rut VARCHAR (10),
    nombre VARCHAR (50) NOT NULL,
	fecnac DATE,
	genero char(1),
	estado BOOLEAN,
	codpro INT,
	codcomuna INT
  ) ;
ALTER TABLE personal ADD CONSTRAINT personal_PK PRIMARY KEY ( rut ) ;

CREATE TABLE profesion
  (
    cod_pro INT,
    descripcion VARCHAR (30) 
  ) ;
ALTER TABLE profesion ADD CONSTRAINT profesion_PK PRIMARY KEY ( cod_pro ) ;

CREATE TABLE comuna
  (
    cod_comuna INT,
    nombre VARCHAR (30),
	codregion INT
  ) ;
ALTER TABLE comuna ADD CONSTRAINT comuna_PK PRIMARY KEY ( cod_comuna ) ;

CREATE TABLE region
  (
    cod_region INT,
    nombre VARCHAR (30)
  ) ;
ALTER TABLE region ADD CONSTRAINT region_PK PRIMARY KEY ( cod_region ) ;

ALTER TABLE personal ADD CONSTRAINT personal_profesion_FK FOREIGN KEY ( codpro ) REFERENCES profesion ( cod_pro ) ;
ALTER TABLE personal ADD CONSTRAINT personal_comuna_FK FOREIGN KEY ( codcomuna ) REFERENCES comuna ( cod_comuna ) ;
ALTER TABLE comuna ADD CONSTRAINT comuna_region_FK FOREIGN KEY ( codregion ) REFERENCES region ( cod_region ) ;

insert into usuario values ('duoc',null,'Usuario DUOC');
update usuario set password=SHA1('duoc') where login='duoc';
insert into usuario values ('duoc2',null,'Usuario DUOC 2');
update usuario set password=SHA1('duoc2') where login='duoc2';
