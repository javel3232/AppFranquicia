# Docker Setup para Aplicación de Franquicias

## Construcción y Ejecución

### 1. Construir la aplicación
```bash
gradlew build
```

### 2. Construir imagen Docker
```bash
docker build -t franquicia-app .
```

### 3. Ejecutar con Docker Compose
```bash
docker-compose up -d
```

### 4. Verificar contenedores
```bash
docker-compose ps
```

### 5. Ver logs
```bash
docker-compose logs app
docker-compose logs mysql
```

### 6. Detener servicios
```bash
docker-compose down
```

## Endpoints disponibles
- API: http://localhost:8080
- MySQL: localhost:3306

## Comandos útiles
```bash
# Reconstruir solo la app
docker-compose up --build app

# Ejecutar en modo interactivo
docker-compose up

# Limpiar volúmenes
docker-compose down -v
```