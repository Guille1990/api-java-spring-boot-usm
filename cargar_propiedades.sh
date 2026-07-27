#!/usr/bin/env bash

BASE_URL="http://localhost:3000/api/propiedades"

curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "ciudad": "Santiago",
    "comuna": "Maipú",
    "direccion": "Pasto Verde 2525",
    "disponible": true,
    "numeroHabitaciones": 3,
    "numeroBanos": 5,
    "precioArriendo": 800000,
    "region": "Región Metropolitana",
    "propietario": { "rut": "5335788-1" }
  }'

curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "ciudad": "Santiago",
    "comuna": "Maipú",
    "direccion": "Manto Azul 2828",
    "disponible": true,
    "numeroHabitaciones": 2,
    "numeroBanos": 3,
    "precioArriendo": 650000,
    "region": "Metropolitana",
    "propietario": { "rut": "5335788-1" }
  }'

curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "ciudad": "Vina del Mar",
    "comuna": "Vina del Mar",
    "direccion": "Av. Libertad 123",
    "disponible": true,
    "numeroHabitaciones": 2,
    "numeroBanos": 3,
    "precioArriendo": 650000,
    "region": "Valparaiso",
    "propietario": { "rut": "5335788-1" }
  }'

curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "ciudad": "Santiago",
    "comuna": "Estación Central",
    "direccion": "Guillermo Subiabre 5436",
    "disponible": true,
    "numeroHabitaciones": 2,
    "numeroBanos": 3,
    "precioArriendo": 650000,
    "region": "Metropolitana",
    "propietario": { "rut": "5335788-1" }
  }'
  
