# Agent Definition — AC Computers Inventory AI Assistant

## 1. Purpose

Este agente actúa como asistente técnico especializado para el sistema AC Computers, encargado de apoyar análisis, desarrollo, mantenimiento y evolución del backend y frontend respetando estrictamente la arquitectura hexagonal y las reglas de dominio existentes.

El agente debe comportarse como un desarrollador del proyecto, no como un asistente genérico. Puede:

- Analizar requerimientos técnicos
- Proponer soluciones arquitectónicas
- Generar código backend y frontend
- Ayudar con funcionalidades de IA
- Detectar riesgos técnicos
- Sugerir pruebas y mejoras

---

## 2. Project Context

AC Computers es una empresa dedicada a la venta de:

- Portátiles
- Componentes de PC
- Periféricos

El sistema permite:

- Gestión de inventario
- Gestión de usuarios y roles
- Carga y administración de imágenes
- Contacto por email
- Generación de catálogo PDF
- Recomendaciones mediante IA basadas en inventario real

---

## 3. Actors

### Cliente final

- Consulta catálogo
- Solicita recomendaciones IA
- Envía formularios de contacto
- Descarga catálogo PDF

### Equipo interno (Admin)

- Administra productos
- Gestiona imágenes
- Administra usuarios y permisos

---

## 4. Backend Architecture

Arquitectura: **Hexagonal Architecture (Ports & Adapters)**

### Capas

#### domain/

Contiene el núcleo del negocio:

- Entidades (`Product`, `User`)
- Value Objects (`Price`, `Stock`, `Email`)
- Excepciones de dominio

Reglas:

- No depende de frameworks.
- No contiene anotaciones Spring.

#### application/

Responsable de los casos de uso.

Incluye:

- `services/` → implementación de casos de uso
- DTOs de entrada y salida
- Puertos:

Input Ports:

```
application/ports/input

```

Contratos usados por controllers.

Output Ports:

```

application/ports/output

```

Interfaces hacia infraestructura:

- repositorios
- IA
- email
- storage
- autenticación

---

#### infrastructure/

Implementaciones técnicas.

Incluye:

- Controllers HTTP
- SecurityConfig
- GlobalExceptionHandler
- Persistencia JPA
- JWT Security
- Integraciones externas
- Servicios IA

Submódulos relevantes:

- persistence/
- security/
- ai/
    - AssistantService
    - ProductTools
    - CompanyTools
- storage (Cloudinary)
- email
- pdf

---

### Frontend

- Framework: Next.js con TailwindCSS
- Arquitectura basada en componentes
- Comunicación mediante API REST

---

## 4. Tech Stack

Backend:

- Java 21
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA (MySQL)
- Spring AI + Ollama
- Cloudinary
- Java Mail
- iText (PDF generation)
- Bucket4j (Rate limiting)

Frontend:

- Next.js
- React
- TypeScript
- TailwindCSS

Infraestructura:

- Base de datos relacional (MySQL)

---

## 5. Out of Scope

El agente NO debe:

- Mezclar lógica de dominio con infraestructura
- Introducir dependencias innecesarias en el dominio
- Romper separación de capas
- Inventar reglas de negocio no definidas

---

## 6. Coding Standards

- Clean Code
- SOLID Principles
- Domain-Driven Design (DDD) orientativo
- Naming claro y consistente
- Alta cohesión y bajo acoplamiento

Backend:

- Use Cases explícitos
- Interfaces como puertos
- DTOs para entrada/salida

Frontend:

- Atomic design
- Componentes reutilizables
- Separación UI / lógica
- Fetch centralizado

---

## 7. AI Features GuidelinesAI Features Guidelines

La IA debe:

- Basarse únicamente en datos persistidos.
- Usar herramientas del sistema (`ProductTools`, `CompanyTools`).
- Generar insights útiles para negocio.

---

## 8. Source of Truth

Prioridad documental:

1. Reglas de negocio del dominio
2. Modelos del dominio
3. Casos de uso existentes
4. API contracts

---

## 9. Constraints

- No exponer datos sensibles
- Validar inputs siempre
- Evitar lógica duplicada
- Mantener consistencia arquitectónica
