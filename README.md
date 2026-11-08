# Sistema de Gestion Escolar
 > Sistema integral para la administración escolar que optimiza los procesos académicos, administrativos y de comunicación entre directivos, profesores, alumnos y padres de familia.
  Permite gestionar usuarios, roles, estudiantes, docentes, acudientes, estructura academica, cursos, materias, matriculas, asistencia, calificaciones, convivencia y notificaciones.

## Tabla de contenido

- [Tecnologias](#tecnologias)
- [Arquitectura y conceptos tecnicos](#arquitectura-y-conceptos-tecnicos)
- [Roles y permisos](#roles-y-permisos)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Modelo de datos](#modelo-de-datos)
- [Requisitos previos](#requisitos-previos)
- [Configuracion de MySQL local](#configuracion-de-mysql-local)
- [Importar la base de datos](#importar-la-base-de-datos)
- [Configurar la conexion Java](#configurar-la-conexion-java)
- [Ejecutar el sistema](#ejecutar-el-sistema)
- [Usuario inicial](#usuario-inicial)
- [Diagramas](#diagramas)
- [Solucion de problemas](#solucion-de-problemas)


## Tecnologias

- Java 17
- Java Swing
- Maven
- MySQL 8
- JDBC
- MySQL Connector/J
- Spring Security Crypto / BCrypt
- FlatLaf
- JUnit 5 y Mockito
- Jakarta Mail
- iText 7
- JCalendar

## Arquitectura y conceptos tecnicos

El proyecto utiliza una arquitectura por capas inspirada en MVC:

```text
Vista Swing
    |
    v
Controladores
    |
    v
DAO + Modelos
    |
    v
MySQL mediante JDBC
```

- **View:** contiene las ventanas, paneles, formularios y componentes Swing.
- **Controller:** recibe eventos de la interfaz y coordina operaciones entre la vista y los DAO.
- **DAO (Data Access Object):** ejecuta consultas SQL y transforma resultados con JDBC.
- **Model:** representa las entidades utilizadas por la aplicacion.
- **Util:** contiene funciones compartidas como permisos y cifrado.
- **Config:** contiene la conexion centralizada a MySQL.

Conceptos y componentes principales:

- **MVC:** separa la presentacion, la coordinacion y el acceso a datos.
- **JDBC:** comunica Java con MySQL mediante `Connection`, `PreparedStatement` y `ResultSet`.
- **PreparedStatement:** usa parametros y evita construir SQL mediante concatenacion directa.
- **BCrypt:** cifra las contrasenas sin almacenar el texto plano original.
- **Roles y permisos:** limita los modulos visibles y disponibles segun el usuario autenticado.
- **Event Dispatch Thread:** SwingUtilities inicia los componentes Swing desde el hilo correcto.
- **Eventos Swing:** ActionListener y otros listeners procesan acciones de botones, tablas y formularios.
- **POO:** usa clases, encapsulacion, constructores, colecciones y relaciones entre entidades.
- **JUnit 5 y Mockito:** permiten realizar pruebas unitarias con dependencias simuladas.
- **FlatLaf:** proporciona una apariencia moderna a la interfaz.
- **Jakarta Mail:** permite enviar copias opcionales de notificaciones por correo.
- **iText 7:** genera o exporta documentos PDF.
- **JCalendar:** proporciona componentes visuales para la seleccion de fechas.

## Roles y permisos

Los roles disponibles son:

| Rol | Alcance principal |
|---|---|
| ADMIN | Administracion completa |
| REGISTRO ACADEMICO | Gestion estudiantil, academica y administrativa |
| DOCENTE | Estudiantes, notas, asistencia, convivencia y comunicaciones |
| PADRE | Consulta de notas, boletines, asistencia, convivencia y comunicaciones |
| ESTUDIANTE | Consulta de notas, boletines, asistencia y comunicaciones |

| Modulo | ADMIN | REGISTRO ACADEMICO | DOCENTE | PADRE | ESTUDIANTE |
|---|:---:|:---:|:---:|:---:|:---:|
| Inicio | Si | Si | Si | Si | Si |
| Usuarios | Si | No | No | No | No |
| Estudiantes | Si | Si | Si | No | No |
| Docentes | Si | Si | No | No | No |
| Acudientes | Si | Si | No | No | No |
| Academico | Si | Si | No | No | No |
| Cursos y materias | Si | Si | No | No | No |
| Matriculas | Si | Si | No | No | No |
| Asignaciones docentes | Si | Si | No | No | No |
| Notas | Si | Si | Si | Si | Si |
| Boletines | Si | Si | Si | Si | Si |
| Asistencia | Si | Si | Si | Si | Si |
| Convivencia | Si | Si | Si | Si | No |
| Notificaciones | Si | Si | Si | Si | Si |

## Estructura del proyecto

```text
campus-sie/
├── database/                  Scripts SQL del modelo de datos
├── docs/                      Documentacion y diagramas
├── src/main/java/
│   └── com/integrador/campus/
│       ├── Main.java          Punto de entrada
│       ├── config/            Conexion a MySQL
│       ├── controller/        Coordinacion de acciones
│       ├── dao/               Consultas JDBC
│       ├── model/             Entidades del dominio
│       ├── service/           Servicios auxiliares
│       ├── Util/              Permisos y cifrado
│       └── view/              Ventanas y paneles Swing
└── pom.xml                    Configuracion Maven y dependencias
```

## Modelo de datos

La base de datos local se llama `escuela`.

- **Usuarios y roles:** usuario, rol.
- **Perfiles escolares:** estudiante, docente, padre.
- **Relacion familiar:** rel_padre_estudiante.
- **Organizacion academica:** anio_lectivo, periodos, nivel, grado, jornada, curso, materia.
- **Procesos academicos:** matricula, calificacion, boletines, asistencia, convivencia.
- **Asignacion docente:** doc_materia_curso.
- **Comunicaciones:** notificaciones y correo_envio.

Los archivos completos estan en [DiagramaBaseDatos.md](DiagramaBaseDatos.md) y [DiagramClases.md](DiagramClases.md).

## Requisitos previos

- JDK 17 o superior.
- Maven 3.x.
- MySQL Server 8.x.
- MySQL Workbench o acceso al cliente `mysql`.
- Un IDE compatible con Java y Maven.
- Opcional: cuenta SMTP para enviar copias de correo.

## Configuracion de MySQL local

La aplicacion esta preparada para conectarse a una instancia local de MySQL con estos valores:

```text
Servidor: localhost
Puerto: 3306
Base de datos: escuela
Usuario: root
Contrasena: la configurada en su instalacion local
```

### Iniciar MySQL

En Windows:

1. Abra **Servicios** de Windows.
2. Busque `MySQL80` o el nombre asignado durante la instalacion.
3. Inicie el servicio.
4. Compruebe que MySQL este disponible en el puerto `3306`.

### Crear o verificar el usuario local

Abra MySQL Workbench, conectese a `Local instance MySQL80` y ejecute:

```sql
CREATE DATABASE IF NOT EXISTS escuela;

SELECT VERSION() AS version_mysql;
SHOW DATABASES;
USE escuela;
```

## Importar la base de datos

Ejecute los scripts desde [database/](database) en el siguiente orden. El primer script crea la base `escuela` y algunas ejecuciones posteriores dependen de las tablas anteriores.

```sql
USE escuela;
SOURCE database/escuela_rol.sql;
SOURCE database/escuela_usuario.sql;
SOURCE database/escuela_estudiante.sql;
SOURCE database/escuela_docente.sql;
SOURCE database/escuela_padre.sql;
SOURCE database/escuela_rel_padre_estudiante.sql;
SOURCE database/escuela_nivel.sql;
SOURCE database/escuela_grado.sql;
SOURCE database/escuela_anio_lectivo.sql;
SOURCE database/escuela_periodos.sql;
SOURCE database/escuela_jornada.sql;
SOURCE database/escuela_curso.sql;
SOURCE database/escuela_materia.sql;
SOURCE database/escuela_matricula.sql;
SOURCE database/escuela_doc_materia_curso.sql;
SOURCE database/escuela_calificacion.sql;
SOURCE database/escuela_boletines.sql;
SOURCE database/escuela_asistencia.sql;
SOURCE database/escuela_convivencia.sql;
SOURCE database/escuela_notificacion.sql;
SOURCE database/escuela_correo_envio.sql;
```

## Configurar la conexion Java

La conexion se carga desde [config.properties](src/main/resources/config.properties) mediante [BDConexion.java](src/main/java/com/integrador/campus/config/BDConexion.java). El archivo debe tener la siguiente estructura:

```properties
db.url=jdbc:mysql://localhost:3306/escuela?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=SU_CONTRASENA_MYSQL
```

Para crear el archivo en una copia nueva:

```powershell
Copy-Item src/main/resources/config.properties.example src/main/resources/config.properties
```

Luego abra `src/main/resources/config.properties` y cambie los datos reales:

```text
db.url    = URL JDBC hacia la base escuela
db.user   = usuario MySQL local
db.password = contraseña del usuario MySQL
```

La plantilla contiene valores de ejemplo, por lo que `db.password` debe reemplazarse por la contraseña de su instalación local.

`BDConexion.java` lee las claves:

```java
private static final Properties CONFIG = cargarConfiguracion();
private static final String URL = obtenerPropiedad("db.url");
private static final String USER = obtenerPropiedad("db.user");
private static final String PASSWORD = obtenerPropiedad("db.password");
```

El archivo real se excluye de Git mediante `.gitignore`. No escriba la contraseña real dentro de `config.properties.example`.

Si cambia el puerto, el nombre de la base o el usuario, ajuste también `db.url` o `db.user`, respectivamente.

## Ejecutar el sistema

### Desde un IDE

1. Abra la carpeta raiz como proyecto Maven.
2. Espere a que Maven descargue las dependencias.
3. Inicie MySQL.
4. Cree e importe la base `escuela`.
5. Ajuste la conexion en `BDConexion.java`.
6. Abra [Main.java](src/main/java/com/integrador/campus/Main.java).
7. Ejecute el metodo `main`.

El programa inicia con FlatLaf Light, crea `LoginFrame`, instancia `UsuarioDao` y configura `LoginController`.

## Usuario inicial

El script [escuela_usuario.sql](database/escuela_usuario.sql) crea una cuenta administrativa:

```text
Correo: admin@campus.com
Contrasena: $1234
Rol: ADMIN
```

## Diagramas

- [Diagrama de clases](docs/diagrama-clases.md): vista de arquitectura por capas:estructura de clases, metodos y relaciones.

## Solucion de problemas

| Problema | Posible causa | Solucion |
|---|---|---|
| `Communications link failure` | MySQL no esta iniciado o el puerto es incorrecto | Inicie el servicio MySQL y confirme el puerto `3306` |
| `Access denied for user` | Credenciales o permisos incorrectos | Corrija usuario y contraseña en `BDConexion.java` y conceda permisos al usuario |
| `Unknown database escuela` | La base no fue creada | Ejecute `escuela_rol.sql`, que contiene `CREATE DATABASE IF NOT EXISTS escuela` |
| `Table ... doesn't exist` | Falto importar una tabla | Ejecute todos los scripts en el orden indicado |
| `Foreign key constraint fails` | Las tablas referenciadas no existen o tienen datos invalidos | Revise el orden de importacion y los identificadores cargados |
| Maven no reconoce el proyecto | Falta JDK 17+ o el proyecto no fue cargado como Maven | Verifique Java 17+ y recargue el proyecto con el archivo `pom.xml` |
| El inicio de sesion falla | Usuario inactivo o contraseña incompatible | Verifique el usuario, el campo `activo` y el cifrado almacenado |
| No aparece el tema FlatLaf | Falta restaurar dependencias Maven | Ejecute `mvn clean compile` |
---
> **Proyecto Integrador 2026** | Desarrollado para *Programación II, Base de Datos II e Ingeniería de Software* por: **Eva Maria Diaz** , **Lauren Sofia Valderrama** y **Juan Sebastian Diaz**.
