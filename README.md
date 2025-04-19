# Tienda Online - Sistema Web de Compras

Proyecto desarrollado por **José de Jesús Castillo Nolasco** como parte de una plataforma completa de comercio en línea.

---

## 📄 Descripción
**TiendaOnline** es una aplicación web construida con Spring Boot que permite a los usuarios explorar productos, agregarlos a un carrito, realizar pedidos y descargar comprobantes de pago en PDF. Además, incluye un panel administrativo para la gestión de usuarios, productos, pedidos y reportes.

---

## 🚀 Tecnologías utilizadas
- **Java 17** y **Spring Boot**
- **Spring Security** para autenticación segura
- **Thymeleaf** para renderizado de vistas
- **MariaDB** como sistema gestor de base de datos
- **Spring Data JPA** para persistencia
- **iText PDF** para generación de comprobantes
- **Apache POI** para exportación de reportes en Excel
- **Bootstrap + CSS personalizado** para la interfaz gráfica

---

## 📊 Características principales
### Cliente
- Registro y login seguro 
- Navegación de productos
- Carrito de compras
- Gestión de direcciones de envío
- Realización de pedidos
- Descarga de comprobante de pedido en PDF

### Administrador
- Gestión de productos (alta, edición, eliminación)
- Gestión de usuarios
- Visualización de pedidos con filtros
- Reportes de inventario y ventas (PDF y Excel)

---

## 🔧 Instalación y ejecución
1. Clonar el repositorio:
```bash
git clone https://github.com/usuario/TiendaOnline.git
```

2. Importar como proyecto Maven en tu IDE (recomendado: IntelliJ IDEA)

3. Configurar la base de datos en `application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/miTienda
spring.datasource.username=usuario
spring.datasource.password=contraseña
```

4. Ejecutar la aplicación:
```bash
./mvnw spring-boot:run
```

5. Acceder desde el navegador:
```
http://localhost:8080
```
## 🛠️ Inicialización de la Base de Datos

Este proyecto incluye la inicialización automática de la base de datos utilizando los archivos `schema.sql` y `data.sql` ubicados en el directorio `src/main/resources`.

### ✅ ¿Cómo funciona?

Spring Boot está configurado para ejecutar automáticamente estos archivos al iniciar la aplicación. Esto crea las tablas necesarias (`schema.sql`) y carga datos iniciales (`data.sql`) para que puedas probar el sistema de inmediato.

### 📄 Configuración en `application.properties`

Asegúrate de tener estas líneas en tu archivo `application.properties` para que la inicialización funcione correctamente:

```properties
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
```

> 🔍 *Nota:* Ten configurado tu motor de base de datos MariaDB, asegúrate de crear la base de datos "miTienda" para que la inicialización funcione correctamente
> y los parámetros de conexión (`spring.datasource.url`, `username`, `password`, etc.)
---
## 🔐 Usuario Administrador por Defecto

Al iniciar la aplicación por primera vez, se inserta automáticamente un usuario administrador para facilitar las pruebas del sistema.

- **Correo:** `admin@example.com`
- **Contraseña:** `Admin123`
---

## 🌐 Roles y accesos
- **CLIENTE**: Puede navegar, comprar y gestionar su información.
- **ADMIN**: Accede a paneles de administración, reportes y mantenimiento del sistema.

---

## 📷 Capturas de pantalla

### 🏠 Página de inicio de sesión
![Página de inicio de sesión](capturas/login.png)

### 👤 Registro de usuario
![Registro de usuario](capturas/registro.png)

### 🛍️ Página principal de inicio
![Página de inicio](capturas/interfaz.png)

### 🧺 Página principal de productos
![Página de productos](capturas/productos.png)

### 🛒 Carrito de compras
![Carrito de compras](capturas/carrito.png)

### 💳 Confirmar carrito
![Confirmar carrito](capturas/confirmar-carrito.png)

### 💵 Confirmación de pedido
![Confirmacion de pedido](capturas/Confirmacion-pedido.png)

### 🖨️ Comprobante de pago
![Comprobante de pago](capturas/comrpobante-pago.png)

### 💁‍♂️ Perfil de usuario
![Perfil de usuario](capturas/perfil.png)

### 🙋‍♂️️️ Editar perfil
![Editar perfil](capturas/editar-perfil.png)

### 🚗 Direcciones de envío
![Direcciones de envío](capturas/direcciones.png)

### 🚁 Editar direcciones
![Editar direcciones](capturas/editar-direccion.png)

### 🧾 Historial de pedidos
![Historial de pedidos](capturas/pedidos-historial.png)

### 👀 Detalles de pedido
![Detalles de pedido](capturas/detalles-pedido.png)

### 👥 Gestion de usuarios
![Gestion de usuarios](capturas/usuarios.png)

### 🖱️ Editar usuarios
![Editar usuarios](capturas/editar-usuario.png)

### 📦 Gestión de productos
![Gestión de productos](capturas/gestion-productos.png)

### 🏷️ Agregar producto
![Agregar producto](capturas/nuevo-producto.png)

### 📋 Reportes de inventario
![Reportes de inventario](capturas/reporte-inventario.png)
![Reportes de inventario](capturas/inventario-excel.png)

### 📊 Gestión de ventas
![Gestión de ventas](capturas/ventas.png)

### 📈 Reporte de ventas
![Reporte de ventas](capturas/reporte-ventas.png)
![Reporte de ventas](capturas/ventas-excel.png)

### 📋 Gestión pedidos
![Gestión pedidos](capturas/gestion-pedidos.png)
---
## ✍️ Autor

<div style="display: flex; align-items: center; justify-content: space-between;">
  <div>
    <strong>José de Jesús Castillo Nolasco</strong> 👨‍💻<br>
    Desarrollador Backend Java Junior 💻 ☕ 🚀
  </div>
  <img src="https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExazY2NXNoOXBxNGIxa2czYTVpNXBqbTVuaWFrZGkxdHoyNmJ2Z3dsMCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/78XCFBGOlS6keY1Bil/giphy.gif" width="200" alt="Developer gif">
</div>

[LinkedIn](https://www.linkedin.com/in/jose-castillo-nolasco/)
[Portafolio](#)
---
