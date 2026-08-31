# Ambientes de desarrollo y pruebas

Este documento describe cómo se configura, ejecuta y verifica la aplicación Newphone
(Spring Boot + Thymeleaf + SQLite) en los ambientes de **desarrollo** y **pruebas**.

## 1. Requisitos comunes

| Recurso | Versión / valor |
| --- | --- |
| JDK | 21 |
| Maven | Wrapper incluido (`mvnw.cmd` / `mvnw`) |
| Framework | Spring Boot 4.1.0 |
| Base de datos | SQLite (archivo local, sin servidor) |
| Puerto HTTP | `8080` en desarrollo; aleatorio (`0`) en pruebas |
| Sistema operativo | Windows, macOS o Linux |

Confirma el JDK:

```powershell
java -version
```

Debe reportar una versión 21.

## 2. Ambiente de desarrollo

### 2.1 Propósito

Permite levantar la tienda y el centro de operaciones en local, con datos de demostración
y recarga automática de plantillas Thymeleaf.

### 2.2 Configuración

Archivo: `src/main/resources/application.properties`

| Propiedad | Valor de desarrollo |
| --- | --- |
| `spring.application.name` | `Newphone` |
| `server.port` | `8080` |
| `spring.sql.init.mode` | `never` (el esquema lo crea `DatabaseBootstrap`) |
| `spring.thymeleaf.cache` | `false` |
| `logging.level.com.newpohone` | `INFO` |
| `newphone.inventory.low-stock-threshold` | `30` |

La base de datos se crea automáticamente en:

```text
%USERPROFILE%\.newphone\newphone-spring.db
```

Para usar otra ruta:

```powershell
$env:NEWPHONE_DATABASE = "C:\ruta\personalizada\newphone.db"
```

o:

```text
-Dnewphone.database=C:\ruta\personalizada\newphone.db
```

### 2.3 Cómo arrancar

Desde IntelliJ IDEA: ejecutar `com.newpohone.ProyectoGa722501096Application`.

Desde la terminal:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación queda en `http://localhost:8080`.

### 2.4 Acceso de demostración

```text
Correo: admin@newphone.com
Contraseña: admin123
```

Catálogo público (sin sesión): `http://localhost:8080/` o `/catalog`.

### 2.5 Bibliotecas por capa

| Capa | Paquete | Bibliotecas / frameworks |
| --- | --- | --- |
| Presentación | `modules.*.presentation` | Spring Web MVC, Thymeleaf, Validation |
| Aplicación | `modules.*.application` | Spring `@Service`, transacciones JDBC |
| Dominio | `modules.*.domain` y `shared.domain` | Java 21 (sin I/O) |
| Infraestructura | `modules.*.infrastructure` y `shared.infrastructure` | Spring JDBC, SQLite JDBC, HikariCP |
| Configuración | `config` | Spring Boot, DataSource, interceptores |

## 3. Ambiente de pruebas

### 3.1 Propósito

Verifica cada módulo de forma aislada (pruebas unitarias) y comprueba que el contexto
Spring arranca (prueba de integración).

### 3.2 Configuración

Archivo: `src/test/resources/application-test.properties`  
Perfil: `test` (`@ActiveProfiles("test")`)

| Propiedad | Valor de pruebas |
| --- | --- |
| `spring.application.name` | `Newphone-Test` |
| `server.port` | `0` (puerto aleatorio) |
| `spring.thymeleaf.cache` | `true` |
| `logging.level.com.newpohone` | `WARN` |
| Base de datos | archivo temporal en `java.io.tmpdir` |

La prueba de contexto (`ProyectoGa722501096ApplicationTests`) asigna:

```text
-Dnewphone.database=%TEMP%\newphone-test-<nano>.db
```

Así no se mezcla con la base de desarrollo.

### 3.3 Cómo ejecutar

Todas las pruebas:

```powershell
.\mvnw.cmd test
```

Un módulo concreto:

```powershell
.\mvnw.cmd -Dtest=CartServiceTest,CartTest,CartItemTest test
```

Desde IntelliJ: clic derecho sobre `src/test/java` → **Run 'All Tests'**.

### 3.4 Cobertura por módulo

| Módulo | Pruebas unitarias |
| --- | --- |
| Compartidos | `MoneyFormatterTest`, `TextNormalizerTest`, `RequestParsersTest`, `SessionUsersTest` |
| Autenticación | `PasswordHasherTest`, `AuthServiceTest` |
| Catálogo | `ProductPresenterTest` |
| Carrito | `CartTest`, `CartItemTest`, `CartServiceTest` |
| Checkout | `CheckoutValidatorTest`, `CheckoutIntentTest`, `CheckoutServiceTest` |
| Pedidos | `OrderStatusTest`, `OrderManagementServiceTest` |
| Inventario | `InventoryStatusTest` |
| Favoritos | `FavoriteRedirectsTest` |
| Dashboard | `DashboardRepositoryTest` |
| CRUD / módulos | `ModuleRegistryTest`, `NavItemTest` |
| Integración | `ProyectoGa722501096ApplicationTests` |

Las pruebas unitarias **no** levantan el servidor HTTP: usan Mockito y `MockHttpSession`.
Solo la prueba de integración carga Spring Boot.

## 4. Empaquetado para entrega

```powershell
.\mvnw.cmd clean package
java -jar target\proyecto-ga-722501096-1.0.0.jar
```

`clean package` ejecuta las pruebas antes de generar el JAR.
