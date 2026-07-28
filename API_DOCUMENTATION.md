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
7. [Reportes](#reportes)

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
    "telefono": "+56912345678",
    "email": "ana.gonzalez@example.com"
  }'
```

### Actualizar propietario
```bash
curl -X PUT "http://localhost:3000/api/propietarios/12345678-9" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana Maria",
    "apellido": "Gonzalez",
    "telefono": "+56987654321",
    "email": "ana.maria@example.com"
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
    "telefono": "+56911223344",
    "email": "luis.perez@example.com"
  }'
```

### Actualizar arrendatario
```bash
curl -X PUT "http://localhost:3000/api/arrendatarios/98765432-1" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Luis Alberto",
    "apellido": "Perez",
    "telefono": "+56944332211",
    "email": "luis.alberto@example.com"
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

#### Explicación del campo `diaPago`

El campo `diaPago` es un **enum** que define el día del mes en que el arrendatario debe realizar el pago del arriendo.

**Valores permitidos:**
- `DIA_5` → Pago el día 5 de cada mes
- `DIA_10` → Pago el día 10 de cada mes
- `DIA_15` → Pago el día 15 de cada mes
- `DIA_20` → Pago el día 20 de cada mes
- `DIA_25` → Pago el día 25 de cada mes
- `DIA_30` → Pago el día 30 de cada mes

**Notas:**
- El valor debe enviarse en **MAYÚSCULAS** (ej: `DIA_10`, no `dia_10`)
- No se puede usar `DIA_31` porque no todos los meses tienen 31 días
- Se almacena en la BD como un número ordinal (0-5)
- Es **obligatorio** al crear un arriendo

**Ejemplos válidos:**
```json
{
  "diaPago": "DIA_5",
  ...
}
```

```json
{
  "diaPago": "DIA_30",
  ...
}
```

**Ejemplo inválido (generará error):**
```json
{
  "diaPago": "dia_15",
  ...
}
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

## Reportes

### Reporte mensual por propiedad
```bash
curl -X GET "http://localhost:3000/api/reportes/propiedad/1/mensual?anio=2026&mes=7" \
  -H "Content-Type: application/json"
```

### Respuesta esperada (ejemplo)
```json
{
  "propiedadId": 1,
  "direccion": "Av. Libertad 123",
  "comuna": "Vina del Mar",
  "ciudad": "Vina del Mar",
  "region": "Valparaiso",
  "mes": 7,
  "anio": 2026,
  "totalIngresos": 5920000.0,
  "totalEgresos": 400000.0,
  "balance": 5520000.0,
  "diasTotalesMes": 31,
  "diasOcupados": 31,
  "porcentajeOcupacion": 100.0,
  "movimientos": [
    {
      "id": 9,
      "concepto": "Otro concepto",
      "tipo": "INGRESO",
      "monto": 70000.0,
      "fecha": "2026-07-26",
      "urlComprobante": "https://..."
    }
  ],
  "eventos": [
    {
      "id": 2,
      "tipo": "MANTENCION",
      "descripcion": "Revision preventiva",
      "fecha": "2026-07-25",
      "url": "https://..."
    }
  ]
}
```

### Enviar reporte mensual por correo
```bash
curl -X POST "http://localhost:3000/api/reportes/propiedad/1/mensual/enviar?anio=2026&mes=7" \
  -H "Content-Type: application/json" \
  -d '{
    "destinatarios": [
      "dueno@example.com",
      "admin@example.com"
    ]
  }'
```

Respuesta exitosa:
```json
{
  "message": "Reporte enviado correctamente"
}
```

El correo se envia en formato HTML y adjunta un PDF del reporte mensual.

Variables de entorno requeridas para correo SMTP:
- `MAIL_HOST`
- `MAIL_PORT`
- `MAIL_USER`
- `MAIL_PASSWORD`
- `MAIL_FROM` (opcional, por defecto `no-reply@arriendosya.local`)

**Notas:**
- `mes` debe estar entre `1` y `12`.
- Si la propiedad no existe, responde `404 Not Found`.
- Si el mes es inválido, responde `400 Bad Request`.

### Reporte anual por propietarios (uno o todos)
`propietarioRut` es opcional:
- Si se envia, retorna el reporte de ese propietario.
- Si no se envia, retorna el consolidado de todos los propietarios.

Todos los propietarios:
```bash
curl -X GET "http://localhost:3000/api/reportes/propietarios/anual?anio=2026" \
  -H "Content-Type: application/json"
```

Un propietario en particular:
```bash
curl -X GET "http://localhost:3000/api/reportes/propietarios/anual?anio=2026&propietarioRut=12345678-9" \
  -H "Content-Type: application/json"
```

Respuesta esperada (ejemplo):
```json
{
  "anio": 2026,
  "propietarioRutFiltro": null,
  "cantidadPropietarios": 2,
  "totalIngresos": 820000.0,
  "totalEgresos": 160000.0,
  "balance": 660000.0,
  "resumenMensualGlobal": [
    {
      "mes": 1,
      "totalIngresos": 100000.0,
      "totalEgresos": 20000.0,
      "balance": 80000.0
    }
  ],
  "propietarios": [
    {
      "propietarioRut": "12345678-9",
      "propietarioNombreCompleto": "Ana Gonzalez",
      "anio": 2026,
      "cantidadPropiedades": 2,
      "totalIngresos": 500000.0,
      "totalEgresos": 120000.0,
      "balance": 380000.0,
      "resumenMensual": [
        {
          "mes": 1,
          "totalIngresos": 60000.0,
          "totalEgresos": 10000.0,
          "balance": 50000.0
        }
      ],
      "propiedades": [
        {
          "propiedadId": 1,
          "direccion": "Av. Libertad 123",
          "comuna": "Vina del Mar",
          "ciudad": "Vina del Mar",
          "region": "Valparaiso",
          "totalIngresos": 350000.0,
          "totalEgresos": 60000.0,
          "balance": 290000.0
        }
      ]
    }
  ]
}
```

**Notas:**
- Si se envia `propietarioRut` y no existe, responde `404 Not Found`.
- El desglose mensual entrega 12 filas (meses 1 a 12).

### Exportar reporte anual por propietarios en PDF
```bash
curl -X GET "http://localhost:3000/api/reportes/propietarios/anual/exportar/pdf?anio=2026" \
  -H "Content-Type: application/json" \
  --output reporte-propietarios.pdf
```

Con propietario especifico:
```bash
curl -X GET "http://localhost:3000/api/reportes/propietarios/anual/exportar/pdf?anio=2026&propietarioRut=12345678-9" \
  -H "Content-Type: application/json" \
  --output reporte-propietario-12345678-9.pdf
```

### Exportar reporte anual por propietarios en Excel
```bash
curl -X GET "http://localhost:3000/api/reportes/propietarios/anual/exportar/excel?anio=2026" \
  -H "Content-Type: application/json" \
  --output reporte-propietarios.xlsx
```

Con propietario especifico:
```bash
curl -X GET "http://localhost:3000/api/reportes/propietarios/anual/exportar/excel?anio=2026&propietarioRut=12345678-9" \
  -H "Content-Type: application/json" \
  --output reporte-propietario-12345678-9.xlsx
```

---

## Enums y Valores Permitidos

### TipoMovimiento
Define el tipo de movimiento financiero de una propiedad.

```
INGRESO  → Dinero que entra (ej: pago de arriendo)
EGRESO   → Dinero que sale (ej: reparaciones, servicios)
```

**Ejemplo:**
```json
{
  "tipo": "INGRESO",
  "concepto": "Pago arriendo julio",
  "monto": 350000
}
```

### DiaPago
Define el día del mes en que debe pagarse el arriendo.

```
DIA_5   → Día 5 de cada mes
DIA_10  → Día 10 de cada mes
DIA_15  → Día 15 de cada mes
DIA_20  → Día 20 de cada mes
DIA_25  → Día 25 de cada mes
DIA_30  → Día 30 de cada mes
```

**Ejemplo:**
```json
{
  "diaPago": "DIA_15",
  "propiedad": { "id": 1 },
  "arrendatario": { "rut": "98765432-1" }
}
```

**¿Por qué solo hasta DIA_30?**
- No todos los meses tienen 31 días
- Febrero solo tiene 28/29 días
- Usar DIA_30 garantiza que funcione en todos los meses

### Rango Reajuste Semestral
Porcentaje de aumento que se aplica cada 6 meses al arriendo.

- **Mínimo:** 1
- **Máximo:** 100
- **Tipo:** Número entero (Integer)

**Ejemplo:**
```json
{
  "reajusteSemestral": 5
}
```
Esto significa un aumento del 5% cada semestre.

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
  -d '{"rut":"12345678-9","nombre":"Juan","apellido":"Perez","telefono":"+56912345678","email":"juan.perez@example.com"}')

# 2. Crear arrendatario
ARRENDATARIO=$(curl -s -X POST "$BASE_URL/arrendatarios" \
  -H "Content-Type: application/json" \
  -d '{"rut":"98765432-1","nombre":"Maria","apellido":"Gonzalez","telefono":"+56911223344","email":"maria.gonzalez@example.com"}')

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
- **Email obligatorio**: En propietarios y arrendatarios el campo `email` es requerido
- **Fechas**: Usar formato ISO 8601 (ej: 2026-07-25T12:00:00-04:00)
- **Almacenamiento de imágenes**: Los comprobantes se suben a Azure Blob Storage
- **Enums**: Enviar en mayúsculas (ej: "INGRESO", "DIA_10")
- **Errores**: Revisar el body de la respuesta para detalles de validación
