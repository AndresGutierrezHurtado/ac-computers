# AGENTS.md - AC Computers (Backend Java)

Este archivo define el contexto minimo para que agentes de IA trabajen en este repositorio sin romper contratos.

## 1) Objetivo del sistema

AC Computers es una plataforma de gestion de inventario para:
- Portatiles.
- Componentes de PC.
- Perifericos.

El backend tambien expone un chatbot de recomendaciones conectado al inventario real.

## 2) Estado real del backend

- Stack backend actual: Java 21 + Spring Boot + Spring Security + JPA (MySQL) + Spring AI (Ollama).
- Arquitectura: `domain` + `application` + `infrastructure`.
- Context path API: `/api`.

Nota importante:
- `README.md` y `README.es.md` describen un backend Node/Express historico.
- Para cambios backend, tomar como fuente de verdad `server/`.

## 3) Reglas de trabajo para agentes

- No cambiar contratos HTTP sin actualizar documentacion y pruebas.
- No inventar datos de productos para respuestas IA.
- Mantener separacion por capas (no saltar puertos directamente desde controladores a JPA).
- Reutilizar DTOs existentes y `ResponseDTO`/`PaginatedResponseDTO`.
- Respetar reglas de seguridad por rol en `SecurityConfig`.
- Cualquier cambio en seed/config debe reflejarse en `docs/ai-agents/`.

## 4) Flujo recomendado para tareas

1. Leer `docs/ai-agents/PROJECT_CONTEXT.md`.
2. Leer `docs/ai-agents/BACKEND_ARCHITECTURE.md`.
3. Verificar endpoints en `docs/ai-agents/API_CONTRACT.md`.
4. Si la tarea toca recomendaciones/chatbot, leer `docs/ai-agents/AI_CHATBOT_GUIDE.md`.
5. Implementar cambios pequenos y con pruebas.

## 5) Comandos utiles (backend)

Desde `server/`:

```bash
./gradlew bootRun
./gradlew test
```

Windows:

```bash
gradlew.bat bootRun
gradlew.bat test
```

## 6) Variables de entorno clave

Definidas en `application-dev.properties`:
- `SERVER_PORT`
- `DB_URL`, `DB_USER`, `DB_PASSWORD`
- `JWT_SECRET`, `JWT_EXPIRATION`
- `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`
- `EMAIL_HOST`, `EMAIL_PORT`, `EMAIL_USERNAME`, `EMAIL_PASSWORD`

