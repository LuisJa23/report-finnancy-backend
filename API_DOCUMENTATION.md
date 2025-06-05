# 📋 Documentación Completa de URLs de la API Financiera

**Base URL:** `http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com`

## 📊 Controlador de Reportes (`/api/reports`)

### 1. **Reporte Financiero Completo**
- **URL:** `GET /api/reports/financial-report/{userId}`
- **Ejemplo:** `http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/reports/financial-report/user123`
- **Descripción:** Genera un reporte financiero completo para un usuario específico
- **Parámetros:**
  - `userId` (path): ID del usuario
- **Respuesta:** `ReportResponseDTO` con gastos por categoría, ahorros totales, ingresos vs gastos, y resumen

### 2. **Reporte Genérico con Filtros**
- **URL:** `GET /api/reports`
- **Ejemplo:** `http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/reports?userId=user123&interval=weekly&global=false&type=expense`
- **Descripción:** Genera reportes personalizados con varios filtros
- **Parámetros:**
  - `userId` (query, opcional): ID del usuario
  - `interval` (query, default="weekly"): Intervalo de tiempo (weekly, monthly, yearly)
  - `global` (query, default="false"): Si incluir datos globales
  - `type` (query, opcional): Tipo de reporte específico
- **Respuesta:** `ReportResponseDTO`

### 3. **Debug de Transacciones de Usuario**
- **URL:** `GET /api/reports/debug-transactions/{userId}`
- **Ejemplo:** `http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/reports/debug-transactions/user123`
- **Descripción:** Endpoint de debug que muestra todas las transacciones de un usuario
- **Parámetros:**
  - `userId` (path): ID del usuario
- **Respuesta:** JSON con resumen de transacciones, agrupaciones por tipo y categoría

## 📈 Controlador de Tendencias Financieras (`/api/insights`)

### 4. **Tendencias de Gasto**
- **URL:** `GET /api/insights/spending-trends`
- **Ejemplo:** `http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/insights/spending-trends?userId=user123&startDate=2024-01-01&endDate=2024-12-31`
- **Descripción:** Analiza las tendencias de gasto por categoría y período
- **Parámetros:**
  - `userId` (query, requerido): ID del usuario
  - `startDate` (query, opcional): Fecha de inicio (formato: YYYY-MM-DD)
  - `endDate` (query, opcional): Fecha de fin (formato: YYYY-MM-DD)
  - *Si no se proporcionan fechas, analiza los últimos 12 meses*
- **Respuesta:** Lista de `SpendingTrend`

### 5. **Tendencias Financieras Completas**
- **URL:** `GET /api/insights/financial-trends`
- **Ejemplo:** `http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/insights/financial-trends?userId=user123&startDate=2024-01-01&endDate=2024-12-31`
- **Descripción:** Analiza tendencias completas (gastos e ingresos) por período
- **Parámetros:**
  - `userId` (query, requerido): ID del usuario
  - `startDate` (query, opcional): Fecha de inicio (formato: YYYY-MM-DD)
  - `endDate` (query, opcional): Fecha de fin (formato: YYYY-MM-DD)
  - *Si no se proporcionan fechas, analiza los últimos 12 meses*
- **Respuesta:** Lista de `SpendingTrend`

## 🎯 Ejemplos de URLs Completas para Pruebas

### URLs Básicas (sin parámetros opcionales):
```
GET http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/reports/financial-report/user123
GET http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/reports/debug-transactions/user123
GET http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/insights/spending-trends?userId=user123
GET http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/insights/financial-trends?userId=user123
```

### URLs con Filtros Avanzados:
```
GET http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/reports?userId=user123&interval=monthly&global=true
GET http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/insights/spending-trends?userId=user123&startDate=2024-06-01&endDate=2024-06-30
GET http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com/api/insights/financial-trends?userId=user123&startDate=2024-01-01&endDate=2024-12-31
```

## 🔧 Endpoints de Sistema (si están habilitados)

Spring Boot puede incluir endpoints adicionales como:
- `/actuator/health` - Estado de salud de la aplicación
- `/actuator/info` - Información de la aplicación

## 📝 Notas Importantes

1. **Formatos de Fecha:** Usar formato ISO 8601 (YYYY-MM-DD)
2. **Códigos de Estado HTTP:**
   - `200 OK` - Respuesta exitosa
   - `400 Bad Request` - Parámetros inválidos
   - `500 Internal Server Error` - Error del servidor
3. **Contenido Type:** `application/json`
4. **Puerto configurado:** La aplicación usa el puerto 5000 internamente (configurado por Procfile)

## 🧪 Script de Prueba

Para probar todas las URLs ejecuta:
```powershell
.\test-simple.ps1
```
