# Script de verificación post-despliegue para Finnancy API en PowerShell
# Uso: .\verify-deployment.ps1 -EbUrl "https://your-app.us-east-1.elasticbeanstalk.com"

param(
    [Parameter(Mandatory=$true)]
    [string]$EbUrl
)

Write-Host "🔍 Verificando despliegue de Finnancy API en: $EbUrl" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

# Test 1: Health Check
Write-Host "1. Probando endpoint de salud..." -ForegroundColor Yellow
try {
    $healthResponse = Invoke-RestMethod -Uri "$EbUrl/health" -Method GET -TimeoutSec 30
    Write-Host "✅ Health check OK (200)" -ForegroundColor Green
    Write-Host "📊 Respuesta:" -ForegroundColor Blue
    $healthResponse | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Health check falló: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 2: Root endpoint
Write-Host "2. Probando endpoint raíz..." -ForegroundColor Yellow
try {
    $rootResponse = Invoke-RestMethod -Uri "$EbUrl/" -Method GET -TimeoutSec 30
    Write-Host "✅ Root endpoint OK (200)" -ForegroundColor Green
    Write-Host "📊 Respuesta:" -ForegroundColor Blue
    $rootResponse | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Root endpoint falló: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 3: API endpoint de reports
Write-Host "3. Probando endpoint de reports..." -ForegroundColor Yellow
try {
    $reportsResponse = Invoke-RestMethod -Uri "$EbUrl/api/reports?userId=test&interval=weekly" -Method GET -TimeoutSec 30
    Write-Host "✅ Reports endpoint OK (200)" -ForegroundColor Green
    Write-Host "📊 Datos disponibles:" -ForegroundColor Blue
    Write-Host "Total de transacciones: $($reportsResponse.totalTransactions)" -ForegroundColor White
} catch {
    Write-Host "❌ Reports endpoint falló: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "🏁 Verificación completada." -ForegroundColor Cyan
