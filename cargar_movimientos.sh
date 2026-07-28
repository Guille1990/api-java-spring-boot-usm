#!/usr/bin/env bash
BASE_URL="http://localhost:3000/api/movimientos"

# 1) Gasto Común mes julio
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "concepto": "Gasto Común mes julio",
    "fecha": "2026-07-25T16:00:00Z",
    "monto": 350000,
    "tipo": "EGRESO",
    "urlComprobante": "https://example.com/comprobantes/julio.pdf",
    "propiedad": { "id": 8 }
  }'

# 2) Ingreso muy genial
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "concepto": "Ingreso muy genial",
    "fecha": "2026-07-25T16:00:00Z",
    "monto": 5000000,
    "tipo": "INGRESO",
    "urlComprobante": "https://example.com/comprobantes/julio.pdf",
    "propiedad": { "id": 8 }
  }'

# 3) Pago genial
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "concepto": "Pago genial",
    "fecha": "2026-07-26T17:38:17.856778Z",
    "monto": 500000,
    "tipo": "INGRESO",
    "urlComprobante": "https://arriendosya.blob.core.windows.net/movimientos/b6a5a0df-42b2-4b7b-886d-20866961cd29_Captura%20desde%202026-07-26%2012-38-23.png",
    "propiedad": { "id": 8 }
  }'

# 4) Pago arriendo julio (fecha NULL)
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "concepto": "Pago arriendo julio",
    "fecha": "2026-07-26T17:38:17.856778Z",
    "monto": 350000,
    "tipo": "INGRESO",
    "urlComprobante": "https://arriendosya.blob.core.windows.net/movimientos/d1483745-6873-4ccc-a4d3-f71ee33fe99d_Captura%20desde%202026-07-26%2012-38-23.png",
    "propiedad": { "id": 8 }
  }'

# 5) egreso penca
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "concepto": "egreso penca",
    "fecha": "2026-07-25T16:00:00Z",
    "monto": 50000,
    "tipo": "EGRESO",
    "urlComprobante": "https://arriendosya.blob.core.windows.net/movimientos/e246c7a6-2b76-411d-ad5a-888b82fe331b_Captura%20desde%202026-07-26%2012-38-23.png",
    "propiedad": { "id": 8 }
  }'

# 6) Otro concepto
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "concepto": "Otro concepto",
    "fecha": "2026-07-26T17:52:04.133728Z",
    "monto": 70000,
    "tipo": "INGRESO",
    "urlComprobante": "https://arriendosya.blob.core.windows.net/movimientos/c60a38f7-4015-4668-b586-40b73bdf8228_Captura%20desde%202026-07-26%2012-38-23.png",
    "propiedad": { "id": 8 }
  }'
