# Guía de Despliegue Local - API de Gestión de Franquicias

Esta guía te permitirá ejecutar la aplicación en tu entorno local paso a paso, sin importar tu nivel de experiencia.

## Prueba Rápida en la Nube

**Antes de instalar localmente**, puedes [probar la aplicación en la nube](#cómo-probar-la-api-en-la-nube) que ya está desplegada y funcionando.

---

## Prerrequisitos

Antes de comenzar, necesitas instalar las siguientes herramientas:

### 1. Java Development Kit (JDK) 17

### 2. Git
- **Windows**: Descargar desde [git-scm.com](https://git-scm.com/)
- **Mac**: `brew install git`
- **Linux**: `sudo apt install git`

### 3. Docker (Opcional - solo si usarás contenedores)
- Descargar Docker Desktop desde [docker.com](https://www.docker.com/products/docker-desktop/)

### Verificar Instalación

Abre una terminal/consola y ejecuta:

```bash
# Verificar Java (debe mostrar versión 17 o superior)
java -version

# Verificar Git
git --version

# Verificar Docker (opcional)
docker --version
```

## Configuración Inicial

### Paso 1: Descargar el Código

```bash
# Clonar el repositorio
git clone https://github.com/javel3232/AppFranquicia.git

# Entrar al directorio del proyecto
cd appFranquicia
```

### Paso 2: Entender la Configuración de Base de Datos

La aplicación puede funcionar con dos tipos de base de datos:

#### Opción A: Base de Datos en AWS RDS (Recomendada - Ya configurada)
La aplicación ya está configurada para usar una base de datos MySQL en la nube de AWS. No necesitas instalar MySQL localmente.

#### Opción B: Base de Datos Local MySQL (Si prefieres todo local)
Si prefieres usar una base de datos local, sigue estos pasos adicionales:

1. **Instalar MySQL localmente:**
   - **Windows**: Descargar MySQL Installer desde [mysql.com](https://dev.mysql.com/downloads/installer/)
   - **Mac**: `brew install mysql`
   - **Linux**: `sudo apt install mysql-server`

2. **Crear la base de datos:**
   ```sql
   CREATE DATABASE franquicia_bd;
   ```

3. **Modificar configuración:**
   Edita el archivo `src/main/resources/application.properties` y cambia:
   ```properties
   # Cambiar de AWS RDS a local
   spring.r2dbc.url=r2dbc:mysql://localhost:3306/franquicia_bd
   spring.r2dbc.username=tu_usuario_mysql
   spring.r2dbc.password=tu_contraseña_mysql
   ```

## Métodos de Despliegue

Elige uno de los siguientes métodos según tu preferencia:

---

## MÉTODO 1: Ejecución Directa con Gradle (Más Simple)

Este es el método más directo.

### Paso 1: Construir la Aplicación

**En Windows:**
```cmd
gradlew.bat build
```

**En Mac/Linux:**
```bash
./gradlew build
```

### Paso 2: Ejecutar las Pruebas 

**En Windows:**
```cmd
gradlew.bat test
```

**En Mac/Linux:**
```bash
./gradlew test
```

### Paso 3: Iniciar la Aplicación

**En Windows:**
```cmd
gradlew.bat bootRun
```

**En Mac/Linux:**
```bash
./gradlew bootRun
```

### Paso 4: Verificar que Funciona

1. Abre tu navegador web
2. Ve a: `http://localhost:8080/api/franquicias`
3. Deberías ver una respuesta JSON (puede estar vacía al inicio)

**¡Listo! Tu aplicación está funcionando.**

---

## MÉTODO 2: Ejecución con Docker

Este método usa contenedores Docker para un entorno más controlado.

### Paso 1: Construir la Aplicación

```bash
# En Windows
gradlew.bat build

# En Mac/Linux
./gradlew build
```

### Paso 2: Construir y Ejecutar con Docker

```bash
# Construir y ejecutar en segundo plano
docker-compose up -d --build
```

### Paso 3: Verificar el Estado

```bash
# Ver si el contenedor está ejecutándose
docker-compose ps

# Ver los logs de la aplicación
docker-compose logs app
```

### Paso 4: Probar la Aplicación

Ve a: `http://localhost:8080/api/franquicias`

### Paso 5: Detener la Aplicación (Cuando termines)

```bash
docker-compose down
```

---

## Cómo Probar la API en la Nube


**¡Prueba la aplicación sin instalar nada!** La API ya está desplegada y funcionando en la nube.

**URL Base:** `http://44.209.140.88/api/franquicias`

### Usando Postman (Recomendado)

1. **Descargar** Postman desde [postman.com](https://www.postman.com/)

**1. FRANQUICIAS**

![Crear Franquicia](https://drive.google.com/uc?id=11PgLf2XSiitxquxKSY3pNnD94UL2jkAV)
```bash
# Crear Franquicia
POST http://44.209.140.88/api/franquicias
Content-Type: application/json

{"nombre": "McDonald's"}
```

![Obtener Franquicias](https://drive.google.com/uc?id=11Ao6t25OovtdkN0vFZalKjtVqZn2M2jG)
```bash
# Obtener Todas las Franquicias
GET http://44.209.140.88/api/franquicias

![obtener por id Franquicia](https://drive.google.com/uc?id=1eVZadtKCD8LUGQ8UP-7ww2PhmxWS2jZ0)

# Obtener Franquicia por ID
GET http://44.209.140.88/api/franquicias/{id de franquicia}
```

![Actualizar Franquicia](https://drive.google.com/uc?id=19FnVi5-vXB4q-VGnGOSQVwxW93yCicOG)
```bash
# Actualizar Nombre de Franquicia
PUT http://44.209.140.88/api/franquicias/{id de franquicia}/nombre
Content-Type: application/json

{"nombre": "McDonald's Premium"}
```

**2. SUCURSALES**

![Crear Sucursal](https://drive.google.com/uc?id=1sbAngLcNXr_5XC9RydGGJfnzYwGvQvEY)
```bash
# Crear Sucursal
POST http://44.209.140.88/api/franquicias/{id de franquicia}/sucursales
Content-Type: application/json

{"nombre": "Sucursal Centro"}
```

![Actualizar Sucursal](https://drive.google.com/uc?id=1rdPS1YhBBhJXPbvDZwJVV1Jac2TR-b6g)
```bash
# Actualizar Nombre de Sucursal
PUT http://44.209.140.88/api/franquicias/sucursales/{id de sucursal}/nombre
Content-Type: application/json

{"nombre": "Sucursal Norte"}
```

![Obtener Sucursal con Productos](https://drive.google.com/uc?id=18la6b_b_RWF7foETKpQkq-QqZTthXh_4)
```bash
# Obtener Sucursal con Todos sus Productos
GET http://44.209.140.88/api/franquicias/sucursales/{id de sucursal}/productos
```

**3. PRODUCTOS**

![Crear Producto](https://drive.google.com/uc?id=12c27ZkZtPaj6Thr5HTJK7n8OC_28cgOR)
```bash
# Crear Producto
POST http://44.209.140.88/api/sucursales/{id de sucursal}/productos
Content-Type: application/json

{"nombre": "Big Mac", "stock": 50}
```

![Actualizar Nombre Producto](https://drive.google.com/uc?id=1yFdbv5LSTGYUHwzPs2fldCdTecrYQtJ2)
```bash
# Actualizar Nombre de Producto
PUT http://44.209.140.88/api/sucursales/productos/{id de producto}/nombre
Content-Type: application/json

{"nombre": "Big Mac Deluxe"}
```

![Actualizar Stock](https://drive.google.com/uc?id=1YarfBHGa3hrRykfQwPhVwREKVLgSYe8j)
```bash
# Actualizar Stock de Producto
PUT http://44.209.140.88/api/sucursales/productos/{id de producto}/stock
Content-Type: application/json

{"stock": 75}
```

![Obtener Mayor Stock](https://drive.google.com/uc?id=1sgPZvPrAKVxXDQ9ixaUqH-WhqJ6joJ0w)
```bash
# Obtener Producto con Mayor Stock por Sucursal
GET http://44.209.140.88/api/sucursales/franquicias/{id de franquicia}/productos/mayor-stock
```

![Eliminar Producto](https://drive.google.com/uc?id=1UBQHhyry7tgmryzDMJtRel0hlmN9o3Tg)
```bash
# Eliminar Producto
DELETE http://44.209.140.88/api/sucursales/productos/{id de producto}
```

---

## Probar la API Local

Una vez que la aplicación esté ejecutándose localmente, puedes probar todos los endpoints cambiando `44.209.140.88` por `localhost:8080` en los ejemplos de arriba.

**RECOMENDACIÓN**: Para una mejor experiencia probando la API, se recomienda usar **Postman** ya que proporciona una interfaz gráfica intuitiva para enviar peticiones HTTP, ver respuestas formateadas y gestionar colecciones de endpoints.

---

## MÉTODO 3: Ejecución con Archivo JAR (Para Servidores)

Este método genera un archivo ejecutable independiente.

### Paso 1: Generar el Archivo JAR

**En Windows:**
```cmd
gradlew.bat bootJar
```

**En Mac/Linux:**
```bash
./gradlew bootJar
```

### Paso 2: Ejecutar el JAR

```bash
java -jar build/libs/franquicia_app-0.0.1-SNAPSHOT.jar
```

### Paso 3: Verificar

Ve a: `http://localhost:8080/api/franquicias`

## Solución de Problemas Comunes

### Error: "java: command not found"
**Solución**: Java no está instalado o no está en el PATH del sistema.
- Reinstala Java JDK 17
- En Windows, agrega Java al PATH del sistema

### Error: "Port 8080 was already in use"
**Solución**: Otro programa está usando el puerto 8080.
- Detén otros servicios que usen el puerto 8080
- O cambia el puerto en `application.properties`: `server.port=8081`

### Error de conexión a base de datos
**Solución**: 
- Si usas AWS RDS: Verifica tu conexión a internet
- Si usas MySQL local: Asegúrate de que MySQL esté ejecutándose

### La aplicación se inicia pero no responde
**Solución**:
- Espera 30-60 segundos para que la aplicación termine de inicializar
- Verifica los logs en la consola para errores específicos

### Gradle no funciona
**Solución**:
- Usa `./gradlew` en lugar de `gradle` (usa el wrapper incluido)
- En Windows, usa `gradlew.bat` en lugar de `gradlew`

## Detener la Aplicación

### Si usaste Gradle o JAR:
- Presiona `Ctrl + C` en la terminal donde está ejecutándose

### Si usaste Docker:
```bash
docker-compose down
```