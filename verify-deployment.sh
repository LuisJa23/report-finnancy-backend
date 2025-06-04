#!/bin/bash

# Script de verificación post-despliegue para Finnancy API
# Uso: ./verify-deployment.sh <EB_URL>

if [ -z "$1" ]; then
    echo "Uso: $0 <EB_URL>"
    echo "Ejemplo: $0 https://your-app.us-east-1.elasticbeanstalk.com"
    exit 1
fi

EB_URL=$1

echo "🔍 Verificando despliegue de Finnancy API en: $EB_URL"
echo "=================================================="

# Test 1: Health Check
echo "1. Probando endpoint de salud..."
HEALTH_RESPONSE=$(curl -s -w "%{http_code}" -o /tmp/health_response.json "$EB_URL/health")
HTTP_CODE=${HEALTH_RESPONSE: -3}

if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ Health check OK (200)"
    echo "📊 Respuesta:"
    cat /tmp/health_response.json | jq '.' 2>/dev/null || cat /tmp/health_response.json
else
    echo "❌ Health check falló (código: $HTTP_CODE)"
    cat /tmp/health_response.json
fi

echo ""

# Test 2: Root endpoint  
echo "2. Probando endpoint raíz..."
ROOT_RESPONSE=$(curl -s -w "%{http_code}" -o /tmp/root_response.json "$EB_URL/")
HTTP_CODE=${ROOT_RESPONSE: -3}

if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ Root endpoint OK (200)"
    echo "📊 Respuesta:"
    cat /tmp/root_response.json | jq '.' 2>/dev/null || cat /tmp/root_response.json
else
    echo "❌ Root endpoint falló (código: $HTTP_CODE)"
    cat /tmp/root_response.json
fi

echo ""

# Test 3: API endpoint de reports
echo "3. Probando endpoint de reports..."
REPORTS_RESPONSE=$(curl -s -w "%{http_code}" -o /tmp/reports_response.json "$EB_URL/api/reports?userId=test&interval=weekly")
HTTP_CODE=${REPORTS_RESPONSE: -3}

if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ Reports endpoint OK (200)"
    echo "📊 Respuesta disponible en /tmp/reports_response.json"
else
    echo "❌ Reports endpoint falló (código: $HTTP_CODE)"
    echo "📊 Error:"
    cat /tmp/reports_response.json
fi

echo ""
echo "🏁 Verificación completada."

# Cleanup
rm -f /tmp/health_response.json /tmp/root_response.json /tmp/reports_response.json
