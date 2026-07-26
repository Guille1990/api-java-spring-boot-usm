# Documentación API - Arriendos Ya

## Base URL
```
http://localhost:3000/api
```

---

## Tabla de Contenidos
1. [Propietarios](#propietarios)
2. [Arrendatarios](#arrendatarios)
3. [Propiedades](#propiedades)
4. [Arriendos](#arriendos)
5. [Eventos](#eventos)
6. [Movimientos](#movimientos)

---

## Propietarios

### Listar todos los propietarios
```bash
curl -X GET "http://localhost:3000/api/propietarios" \
  -H "Content-Type: application/json"
```

### Obtener propietario por RUT
```bash
curl -X GET "http://localhost:3000/api/propietarios/12345678-9" \
  -H "Content-Type: application/json"
```

### Crear propietario
```bash
curl -X POST "http://localhost:3000/api/propietarios" \
  -H "Content-Type: application/json" \
  -d '{
    "rut": "12345678-9",
    "nombre": "Ana",
    "apellido": "Gonzalez",
    "telefono": "+56912345678"
  }'
```

### Actualizar propietario
```bash
curl -X PUT "http://localhost:3000/api/propietarios/12345678-9" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana Maria",
    "apellido": "Gonzalez",
    "telefono": "+56987654321"
  }'
```

### Eliminar propietario
```bash
curl -X DELETE "http://localhost:3000/api/propietarios/12345678-9"
```

---

## Arrendatarios

### Listar todos los arrendatarios
```bash
curl -X GET "http://localhost:3000/api/arrendatarios" \
  -H "Content-Type: application/json"
```

### Obtener arrendatario por RUT
```bash
curl -X GET "http://localhost:3000/api/arrendatarios/98765432-1" \
  -H "Content-Type: application/json"
```

### Crear arrendatario
```bash
curl -X POST "http://localhost:3000/api/arrendatarios" \
  -H "Content-Type: application/json" \
  -d '{
    "rut": "98765432-1",
    "nombre": "Luis",
    "apellido": "Perez",
    "telefono": "+56911223344"
  }'
```

### Actualizar arrendatario
```bash
curl -X PUT "http://localhost:3000/api/arrendatarios/98765432-1" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Luis Alberto",
    "apellido": "Perez",
    "telefono": "+56944332211"
  }'
```

### Eliminar arrendatario
```bash
curl -X DELETE "http://localhost:3000/api/arrendatarios/98765432-1"
```

---

## Propiedades

### Listar todas las propiedades
```bash
curl -X GET "http://localhost:3000/api/propiedades" \
  -H "Content-Type: application/json"
```

### Obtener propiedad por ID
```bash
curl -X GET "http://localhost:3000/api/propiedades/1" \
  -H "Content-Type: application/json"
```

### Crear propiedad
```bash
curl -X POST "http://localhost:3000/api/propiedades" \
  -H "Content-Type: application/json" \
  -d '{
    "direccion": "Av. Libertad 123",
    "comuna": "Vina del Mar",
    "ciudad": "Vina del Mar",
    "region": "Valparaiso",
    "numeroHabitaciones": 3,
    "numeroBanos": 2,
    "precioArriendo": 650000,
    "disponible": true,
    "propietario": {
      "rut": "12345678-9"
    }
  }'
```

### Actualizar propiedad
```bash
curl -X PUT "http://localhost:3000/api/propiedades/1" \
  -H "Content-Type: application/json" \
  -d '{
    "direccion": "Av. Libertad 456",
    "comuna": "Vina del Mar",
    "ciudad": "Vina del Mar",
    "region": "Valparaiso",
    "numeroHabitaciones": 4,
    "numeroBanos": 2,
    "precioArriendo": 720000,
    "disponible": true
  }'
```

### Asignar propietario a propiedad
```bash
curl -X PUT "http://localhost:3000/api/propiedades/1/asignar-propietario/12345678-9"
```

### Eliminar propiedad
```bash
curl -X DELETE "http://localhost:3000/api/propiedades/1"
```

---

## Arriendos

### Listar todos los arriendos
```bash
curl -X GET "http://localhost:3000/api/arriendos" \
  -H "Content-Type: application/json"
```

### Listar arriendos por propiedad
```bash
curl -X GET "http://localhost:3000/api/arriendos/propiedad/1" \
  -H "Content-Type: application/json"
```

### Listar arriendos por arrendatario
```bash
curl -X GET "http://localhost:3000/api/arriendos/arrendatario/98765432-1" \
  -H "Content-Type: application/json"
```

### Crear arriendo
```bash
curl -X POST "http://localhost:3000/api/arriendos" \
  -H "Content-Type: application/json" \
  -d '{
    "propiedad": {
      "id": 1
    },
    "arrendatario": {
      "rut": "98765432-1"
    },
    "fechaInicio": "2026-07-01",
    "diaPago": "DIA_10",
    "reajusteSemestral": 5,
    "activo": true
  }'
```

### Actualizar arriendo
```bash
curl -X PUT "http://localhost:3000/api/arriendos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "diaPago": "DIA_15",
    "reajusteSemestral": 3,
    "activo": true
  }'
```

### Finalizar arriendo
```bash
curl -X PUT "http://localhost:3000/api/arriendos/1/finalizar?fechaTermino=2026-12-31"
```

---

## Eventos

### Listar todos los eventos
```bash
curl -X GET "http://localhost:3000/api/eventos" \
  -H "Content-Type: application/json"
```

### Listar eventos por propiedad
```bash
curl -X GET "http://localhost:3000/api/eventos/propiedad/1" \
  -H "Content-Type: application/json"
```

### Crear evento
```bash
curl -X POST "http://localhost:3000/api/eventos" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "MANTENCION",
    "descripcion": "Revision preventiva de la propiedad",
    "fecha": "2026-07-25T17:55:36-04:00",
    "url": "https://example.com/documentos/mantencion",
    "propiedad": {
      "id": 1
    }
  }'
```

---

## Movimientos

### Listar todos los movimientos
```bash
curl -X GET "http://localhost:3000/api/movimientos" \
  -H "Content-Type: application/json"
```

### Obtener movimiento por ID
```bash
curl -X GET "http://localhost:3000/api/movimientos/1" \
  -H "Content-Type: application/json"
```

### Listar movimientos por propiedad
```bash
curl -X GET "http://localhost:3000/api/movimientos/propiedad/1" \
  -H "Content-Type: application/json"
```

### Crear movimiento (sin comprobante)
```bash
curl -X POST "http://localhost:3000/api/movimientos" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "INGRESO",
    "concepto": "Pago arriendo mes julio",
    "monto": 350000,
    "fecha": "2026-07-25T12:00:00-04:00",
    "propiedad": {
      "id": 1
    }
  }'
```

### Crear movimiento con comprobante (Azure Blob Storage)
```bash
curl -X POST "http://localhost:3000/api/movimientos/con-comprobante" \
  -F "movimiento={\"tipo\":\"INGRESO\",\"concepto\":\"Pago arriendo\",\"monto\":350000,\"propiedad\":{\"id\":1}}" \
  -F "comprobante=@/ruta/a/archivo.pdf"
```

### Actualizar movimiento
```bash
curl -X PUT "http://localhost:3000/api/movimientos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "EGRESO",
    "concepto": "Reparacion gasifero",
    "monto": 85000,
    "fecha": "2026-07-25T15:00:00-04:00"
  }'
```

### Actualizar comprobante de movimiento
```bash
curl -X PUT "http://localhost:3000/api/movimientos/1/comprobante" \
  -F "comprobante=@/ruta/a/nuevo_archivo.pdf"
```

### Eliminar movimiento
```bash
curl -X DELETE "http://localhost:3000/api/movimientos/1"
```

---

## Enums y Valores Permitidos

### TipoMovimiento
```
INGRESO
EGRESO
```

### DiaPago
```
DIA_5
DIA_10
DIA_15
DIA_20
DIA_25
DIA_30
```

### Rango Reajuste Semestral
- Mínimo: 1
- Máximo: 100

---

## Códigos de Respuesta

| Código | Descripción |
|--------|-------------|
| 200    | OK - Solicitud exitosa |
| 201    | Created - Recurso creado |
| 204    | No Content - Eliminado exitosamente |
| 400    | Bad Request - Datos inválidos |
| 404    | Not Found - Recurso no encontrado |
| 500    | Server Error - Error del servidor |

---

## Ejemplos de Scripts

### Crear flujo completo de arriendo
```bash
#!/bin/bash

BASE_URL="http://localhost:3000/api"

# 1. Crear propietario
PROPIETARIO=$(curl -s -X POST "$BASE_URL/propietarios" \
  -H "Content-Type: application/json" \
  -d '{"rut":"12345678-9","nombre":"Juan","apellido":"Perez","telefono":"+56912345678"}')

# 2. Crear arrendatario
ARRENDATARIO=$(curl -s -X POST "$BASE_URL/arrendatarios" \
  -H "Content-Type: application/json" \
  -d '{"rut":"98765432-1","nombre":"Maria","apellido":"Gonzalez","telefono":"+56911223344"}')

# 3. Crear propiedad
PROPIEDAD=$(curl -s -X POST "$BASE_URL/propiedades" \
  -H "Content-Type: application/json" \
  -d '{"direccion":"Calle Principal 100","comuna":"Santiago","ciudad":"Santiago","region":"RM","numeroHabitaciones":3,"numeroBanos":2,"precioArriendo":650000,"disponible":true,"propietario":{"rut":"12345678-9"}}')

# 4. Crear arriendo
ARRIENDO=$(curl -s -X POST "$BASE_URL/arriendos" \
  -H "Content-Type: application/json" \
  -d '{"propiedad":{"id":1},"arrendatario":{"rut":"98765432-1"},"fechaInicio":"2026-07-01","diaPago":"DIA_10","reajusteSemestral":5,"activo":true}')

# 5. Registrar movimiento de pago
curl -s -X POST "$BASE_URL/movimientos/con-comprobante" \
  -F "movimiento={\"tipo\":\"INGRESO\",\"concepto\":\"Pago arriendo julio\",\"monto\":650000,\"propiedad\":{\"id\":1}}" \
  -F "comprobante=@comprobante.pdf"

echo "Flujo completado!"
```

---

## Notas Importantes

- **Validación de RUT**: Los RUTs deben incluir guion (ej: 12345678-9)
- **Fechas**: Usar formato ISO 8601 (ej: 2026-07-25T12:00:00-04:00)
- **Almacenamiento de imágenes**: Los comprobantes se suben a Azure Blob Storage
- **Enums**: Enviar en mayúsculas (ej: "INGRESO", "DIA_10")
- **Errores**: Revisar el body de la respuesta para detalles de validación
