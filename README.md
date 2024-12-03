# EDUCANTROL

## Descripción

EduCantrol es un sistema de gestión educativa diseñado para optimizar la administración de un centro educativo. Con EduCantrol se permite gestionar estudiantes, profesores, asignaturas, períodos académicos y horarios, facilitando la creación de expedientes académicos, los pagos y la programación de clases y materias. Está desarrollado utilizando Vaadin para la interfaz de usuario, junto con Spring Boot, JPA y Maven en el backend y la persistencia de datos.

## Características

+ Gestión de Estudiantes: Registro de estudiantes con datos personales y pagos realizados.
+ Gestión de Profesores: Administración del personal docente y sus asignaturas y periodos asignados.
+ Materias y Periodos: Control de materias por período académico.
+ Gestión de Horarios: Asignación de horarios para clases, incluyendo la materia, el profesor y el periodo en que se da la materia.


#Tecnologías Utilizadas

+ Frontend: Vaadin
+ Backend: Spring Boot
+ Persistencia: JPA 
+ Gestión de Dependencias: Maven
+ Base de Datos: MariaDB

## Instalación

Requisitos Previos
JDK 11 o superior
Maven instalado
MariaDB
IDE (NetBeans, IntelliJ, Eclipse)

## Uso

+ Pantallas de Mantenimiento. EduCantrol cuenta con diferentes pantallas de mantenimiento para administrar cada entidad.
+ Persona: clase principal para registrar, editar la información y dar de baja, de ella se derivan las clases Profesor y Estudiante.
+ Materia: Gestión de materias, incluyendo nombre y poder eliminarla
+ Periodo: Para definir los periodos de clases disponibles.
+ Horario: Asignar horarios a las materias de acuerdo con el período, día, y profesor.
+ Pago: Para realizar procesos de pago tanto de estudiantes como de profesores
+ Clase: permite crear, editar y eliminar las clases.
+ Grupo: Para crear un grupo,asignar la cantidad de integrantes y asignar estudiantes a dicho grupo
+ Asistencia: nos permite gestionar la asistencia del alumnado 

## Estructura del Proyecto

src/main/java: Contiene los paquetes de entidades, controladores, servicios y repositorios.
src/main/resources: Incluye el archivo de configuración application.properties para la base de datos y otros recursos.
docs/: Documentación adicional, como el manual del usuario y el diagrama de arquitectura del sistema.


## Integrantes

+ Katya Michelle Asencio Bernal – AB23007
+ Ángel Josué Cortez Zaldaña – CZ23002
+ José Daniel Mendez Sanchez – MS19059
+ Kevin Armando Rivera Henríquez – RH16042
