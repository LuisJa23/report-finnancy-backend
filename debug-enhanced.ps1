# Script para probar el endpoint de debug mejorado
# Ejecutar: .\debug-enhanced.ps1

$baseUrl = "http://finnnacy-reports-backend.us-east-2.elasticbeanstalk.com"
$userId = "NuWpzzFMShhLC8KBNCNMzrnk0Kt1"

Write-Host "=== DIAGNÓSTICO MEJORADO DE LA API ===" -ForegroundColor Green
Write-Host "Base URL: $baseUrl" -ForegroundColor Yellow
Write-Host "UserID a investigar: $userId" -ForegroundColor Yellow
Write-Host "Fecha: $(Get-Date)" -ForegroundColor Yellow
Write-Host ""

try {
    Write-Host "🔍 Ejecutando diagnóstico mejorado..." -ForegroundColor Cyan
    
    $url = "$baseUrl/api/reports/enhanced-debug/$userId"
    Write-Host "URL: $url" -ForegroundColor Gray
    
    $response = Invoke-RestMethod -Uri $url -Method GET -ContentType "application/json"
    
    Write-Host "✅ Respuesta recibida exitosamente!" -ForegroundColor Green
    Write-Host ""
    
    # Mostrar información de conectividad
    Write-Host "=== CONECTIVIDAD ===" -ForegroundColor Blue
    Write-Host "MongoDB Conectado: $($response.mongoConnected)" -ForegroundColor $(if($response.mongoConnected) {"Green"} else {"Red"})
    Write-Host "Estado: $($response.connectionStatus)" -ForegroundColor Gray
    Write-Host ""
    
    # Mostrar estadísticas de la base de datos
    Write-Host "=== ESTADÍSTICAS DE LA BASE DE DATOS ===" -ForegroundColor Blue
    Write-Host "Total documentos en colección: $($response.totalDocumentsInCollection)" -ForegroundColor White
    Write-Host "Usuarios únicos en DB: $($response.totalUniqueUsers)" -ForegroundColor White
    Write-Host ""
    
    # Mostrar resultados de búsqueda
    Write-Host "=== RESULTADOS DE BÚSQUEDA ===" -ForegroundColor Blue
    Write-Host "UserID buscado: $($response.searchedUserId)" -ForegroundColor White
    Write-Host "Longitud del UserID: $($response.userIdLength)" -ForegroundColor White
    Write-Host "Coincidencias exactas: $($response.exactMatchCount)" -ForegroundColor $(if($response.exactMatchCount -gt 0) {"Green"} else {"Red"})
    Write-Host "Coincidencias parciales: $($response.partialMatchCount)" -ForegroundColor White
    Write-Host ""
    
    # Mostrar UserIDs en la base de datos
    if ($response.uniqueUserIdsInDB -and $response.uniqueUserIdsInDB.Count -gt 0) {
        Write-Host "=== USERIDS ENCONTRADOS EN LA BASE DE DATOS ===" -ForegroundColor Blue
        $response.uniqueUserIdsInDB | ForEach-Object {
            $color = if ($_ -eq $userId) {"Green"} else {"White"}
            Write-Host "- $_" -ForegroundColor $color
        }
        Write-Host ""
    }
    
    # Mostrar UserIDs similares
    if ($response.similarUserIds -and $response.similarUserIds.Count -gt 0) {
        Write-Host "=== USERIDS SIMILARES ENCONTRADOS ===" -ForegroundColor Yellow
        $response.similarUserIds | ForEach-Object {
            Write-Host "- $_" -ForegroundColor Yellow
        }
        Write-Host ""
    }
    
    # Mostrar transacciones de ejemplo
    if ($response.sampleTransactions -and $response.sampleTransactions.Count -gt 0) {
        Write-Host "=== TRANSACCIONES DE EJEMPLO ===" -ForegroundColor Blue
        $response.sampleTransactions | ForEach-Object {
            Write-Host "ID: $($_.id)" -ForegroundColor White
            Write-Host "UserID: $($_.userId)" -ForegroundColor White
            Write-Host "Monto: $($_.amount)" -ForegroundColor White
            Write-Host "Tipo: $($_.type)" -ForegroundColor White
            Write-Host "Descripción: $($_.description)" -ForegroundColor White
            Write-Host "---" -ForegroundColor Gray
        }
        Write-Host ""
    }
    
    # Mostrar información completa en JSON para análisis detallado
    Write-Host "=== RESPUESTA COMPLETA (JSON) ===" -ForegroundColor Magenta
    $response | ConvertTo-Json -Depth 10 | Write-Host -ForegroundColor Gray
    
} catch {
    Write-Host "❌ Error al ejecutar el diagnóstico:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "Código de estado: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "=== FIN DEL DIAGNÓSTICO ===" -ForegroundColor Green
