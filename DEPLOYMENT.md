# 🚀 Finnancy API - Deployment Troubleshooting

## 📋 Problemas Solucionados

### ✅ **Problema de Puerto**
- **Síntoma**: Nginx 502 Bad Gateway, aplicación corriendo en puerto 8085
- **Solución**: Configurado puerto 5000 fijo en todas las configuraciones
- **Archivos**: `application.properties`, `Procfile`, `.ebextensions/01-environment.config`

### ✅ **Configuración EB Inválida**
- **Síntoma**: `JVMOptions` error en logs de EB
- **Solución**: Cambiado a `JAVA_OPTS` en variables de entorno
- **Archivos**: `.ebextensions/01-environment.config`, `.ebextensions/02-app-config.config`

### ✅ **Health Checks**
- **Síntoma**: EB no puede verificar salud de la aplicación
- **Solución**: Endpoints `/health` y `/` con información detallada
- **Archivos**: `HealthController.java`

## 🔧 Verificación del Despliegue

### Usando PowerShell:
```powershell
.\verify-deployment.ps1 -EbUrl "https://your-app.us-east-1.elasticbeanstalk.com"
```

### Verificación Manual:
1. **Health Check**: `GET /health`
2. **Root**: `GET /`
3. **API Test**: `GET /api/reports?userId=test&interval=weekly`

## 📊 Endpoints Disponibles

- `GET /` - Información básica de la API
- `GET /health` - Estado de salud y conectividad MongoDB
- `GET /api/reports/financial-report/{userId}` - Reporte financiero completo
- `GET /api/reports?userId={userId}&interval={interval}` - Reportes por intervalo
- `GET /api/reports/debug-transactions/{userId}` - Debug de transacciones

## 🐛 Troubleshooting Común

### MongoDB No Conecta
1. Verificar que la instancia MongoDB esté corriendo
2. Comprobar security groups de AWS
3. Verificar conectividad de red entre EB y MongoDB
4. Revisar credentials en las variables de entorno

### Puerto Incorrecto
1. Verificar que `server.port=5000` en application.properties
2. Comprobar `PORT=5000` en variables de entorno de EB
3. Revisar Procfile usa puerto correcto

### 502 Bad Gateway
1. Verificar que la aplicación está corriendo en puerto 5000
2. Comprobar logs de aplicación en EB
3. Verificar health check endpoint responde correctamente

## 📝 Logs Importantes

### AWS Elastic Beanstalk:
- Environment logs: Buscar en EB Console > Logs
- Application logs: `/var/log/web.stdout.log`
- Nginx logs: `/var/log/nginx/error.log`

### Comandos de Debug:
```bash
# Ver status de la aplicación
curl https://your-app.com/health

# Test de conectividad
curl -v https://your-app.com/

# Logs de EB
eb logs
```

## 🔄 Pipeline CI/CD

El pipeline ejecuta:
1. **Install**: Java Corretto 17
2. **Pre-build**: `mvn clean`
3. **Build**: `mvn compile && mvn package -DskipTests`
4. **Artifacts**: JAR + Procfile + .ebextensions

## 📋 Configuración de Variables de Entorno en EB

```yaml
MONGODB_URI: "mongodb://admin:password@host:27017/database?authSource=admin"
PORT: "5000"
JAVA_OPTS: "-Xmx512m -Xms256m -Djava.net.preferIPv4Stack=true"
```

## ⚡ Quick Fixes

### Re-deploy Rápido:
```bash
git add .
git commit -m "Fix: description"
git push origin main
```

### Test Local:
```bash
mvn clean package -DskipTests
java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=5000
```

### Verificar Configuración:
```bash
# Verificar que todos los archivos están presentes
ls -la Procfile .ebextensions/
```
