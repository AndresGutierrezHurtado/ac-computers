# AC Computers - API Backend

Este es el núcleo del sistema **AC Computers**, diseñado bajo una arquitectura robusta y escalable para la gestión de inventario, usuarios y servicios asistidos por Inteligencia Artificial.

---

## Arquitectura del Proyecto

El backend sigue estrictamente el patrón de **Arquitectura Hexagonal (Ports & Adapters)**, asegurando que el núcleo del negocio sea independiente de frameworks, bases de datos y servicios externos. Esta separación permite una alta testabilidad y adaptabilidad ante cambios técnicos.

### 1. Domain (Núcleo del Negocio)

Es la capa más interna y pura del sistema. No tiene dependencias de infraestructura ni anotaciones de Spring.

- **Entidades**: Modelos principales como `Product` y `User`.
- **Value Objects**: Objetos que representan conceptos del negocio como `Price`, `Stock` o `Email`.
- **Excepciones de Dominio**: Errores específicos del negocio que no dependen de protocolos técnicos.

### 2. Application (Casos de Uso)

Define lo que el sistema puede hacer y orquesta el flujo de datos.

- **Servicios de Aplicación**: Implementan los casos de uso específicos del sistema.
- **Puertos (Ports)**:
    - **Input Ports**: Interfaces (contratos) que los controladores consumen para interactuar con la lógica.
    - **Output Ports**: Interfaces que definen los contratos para interactuar con el mundo exterior (persistencia, IA, email).
- **DTOs**: Objetos de transferencia para entrada y salida, asegurando el desacoplamiento total del modelo interno.

### 3. Infrastructure (Adaptadores Técnicos)

Implementaciones concretas de los contratos definidos en la capa de aplicación.

- **Controladores REST**: Adaptadores de entrada para peticiones HTTP.
- **Persistencia**: Implementaciones de repositorios con JPA y configuración de MySQL.
- **Seguridad**: Gestión de JWT, cifrado de contraseñas y filtros de seguridad.
- **Integraciones Externas**:
    - **IA**: Servicios basados en Ollama y Spring AI.
    - **Storage**: Gestión de archivos con Cloudinary.
    - **E-mail**: Integración con Java Mail.
    - **Generación PDF**: Implementación con iText.
- **Global Exception Handler**: Captura de errores técnicos y de dominio para respuestas estandarizadas.

---

## Controladores y Endpoints

A continuación se detallan los controladores disponibles y sus respectivas funcionalidades:

### Autenticación (`/auth`)

Gestiona el acceso y la identidad de los usuarios en el sistema.

| Método | Endpoint                | Descripción                                                                    |
| :----- | :---------------------- | :----------------------------------------------------------------------------- |
| `POST` | `/auth/login`           | Inicia sesión y devuelve las credenciales del usuario (JWT).                   |
| `POST` | `/auth/register`        | Registra un nuevo usuario en la plataforma.                                    |
| `POST` | `/auth/google`          | Inicia sesión con Google y retorna la sesión creada.                           |
| `GET`  | `/auth/session`         | Recupera la información del usuario en la sesión actual.                       |
| `POST` | `/auth/logout`          | Finaliza la sesión activa de forma segura.                                     |
| `POST` | `/auth/set-password`    | Define o actualiza la contraseña de un usuario.                                |
| `POST` | `/auth/forgot-password` | Solicita el restablecimiento de contraseña y dispara el flujo de recuperación. |

### Productos (`/products`)

El corazón del inventario, gestionando el catálogo y recomendaciones inteligentes.

| Método   | Endpoint                  | Descripción                                                                                |
| :------- | :------------------------ | :----------------------------------------------------------------------------------------- |
| `GET`    | `/products`               | Lista productos con filtrado y paginación (categoría, marca, precio, descuento, etc.).     |
| `GET`    | `/products/{id}`          | Obtiene el detalle completo de un producto específico.                                     |
| `GET`    | `/products/{id}/overview` | Genera un resumen con IA del producto para vista rápida.                                   |
| `POST`   | `/products`               | Crea un nuevo producto (incluye carga de imágenes y selección de principal).               |
| `PUT`    | `/products/{id}`          | Actualiza la información técnica y comercial de un producto (incluye gestión de imágenes). |
| `DELETE` | `/products/{id}`          | Elimina un producto del inventario.                                                        |
| `POST`   | `/products/sales-chat`    | Devuelve una respuesta del asistente IA basada en el inventario y el contexto del cliente. |

### Usuarios (`/users`)

Administración de perfiles y roles para el equipo interno.

| Método   | Endpoint        | Descripción                                                            |
| :------- | :-------------- | :--------------------------------------------------------------------- |
| `GET`    | `/users`        | Lista todos los usuarios con soporte para búsqueda y filtrado por rol. |
| `GET`    | `/users/{id}`   | Obtiene el perfil detallado de un usuario por su ID.                   |
| `POST`   | `/users/invite` | Invita un usuario y crea el registro inicial con su rol.               |
| `PUT`    | `/users/{id}`   | Actualiza los datos de un usuario existente.                           |
| `DELETE` | `/users/{id}`   | Elimina un usuario del sistema (solo administradores).                 |

### Marcas (`/brands`)

Catálogo de marcas asociadas a los productos.

| Método | Endpoint  | Descripción                         |
| :----- | :-------- | :---------------------------------- |
| `GET`  | `/brands` | Lista todas las marcas registradas. |

### Categorías (`/categories`)

Listado de categorías principales del catálogo.

| Método | Endpoint      | Descripción                             |
| :----- | :------------ | :-------------------------------------- |
| `GET`  | `/categories` | Lista todas las categorías registradas. |

### Subcategorías (`/subcategories`)

Subdivisiones de categorías usadas para clasificar productos.

| Método | Endpoint                         | Descripción                                                 |
| :----- | :------------------------------- | :---------------------------------------------------------- |
| `GET`  | `/subcategories`                 | Lista todas las subcategorías disponibles.                  |
| `GET`  | `/subcategories?categoryId={id}` | Lista subcategorías filtrando por una categoría específica. |

### Especificaciones (`/specifications`)

Metadatos técnicos y sus posibles valores, usados para describir productos.

| Método | Endpoint                                     | Descripción                                                       |
| :----- | :------------------------------------------- | :---------------------------------------------------------------- |
| `GET`  | `/specifications`                            | Lista especificaciones, opcionalmente filtradas por subcategoría. |
| `GET`  | `/specification-values?specificationId={id}` | Lista valores para una especificación específica.                 |

### Gestión de Imágenes (`/images`)

Control persistente de los recursos visuales alojados en la nube.

| Método   | Endpoint       | Descripción                                                                    |
| :------- | :------------- | :----------------------------------------------------------------------------- |
| `POST`   | `/images`      | Carga una nueva imagen asociada a un producto (incluye `productId` e `image`). |
| `DELETE` | `/images/{id}` | Elimina permanentemente una imagen del almacenamiento.                         |

### Catálogos PDF (`/pdf`)

Generación dinámica de documentos listos para su distribución.

| Método | Endpoint        | Descripción                                                                       |
| :----- | :-------------- | :-------------------------------------------------------------------------------- |
| `POST` | `/pdf/generate` | Genera y retorna un catálogo en PDF (acepta filtros por `type` y/o `categoryId`). |

### Contacto (`/contact`)

Puente de comunicación con los clientes finales.

| Método | Endpoint   | Descripción                                                                   |
| :----- | :--------- | :---------------------------------------------------------------------------- |
| `POST` | `/contact` | Envía feedback o solicitudes de contacto procesadas por el sistema de correo. |

---

## Stack Tecnológico

- **Lenguaje:** Java 21
- **Framework:** Spring Boot 4
- **Persistencia:** MySQL (Spring Data JPA)
- **Seguridad:** Spring Security + JWT
- **IA:** Spring AI + Ollama
- **Multitareas:** Cloudinary (Imágenes), iText (PDF), Java Mail.

---

## Guía de Instalación

Sigue estos pasos para configurar y ejecutar el servidor localmente:

### 1. Requisitos Previos

- **Java 21** instalado.
- **MySQL** (recomendado vía XAMPP).
- **Ollama** (para las funcionalidades de IA).

### 2. Configuración de la Base de Datos (XAMPP)

1. Inicia el panel de control de **XAMPP** y activa los módulos **Apache** y **MySQL**.
2. Accede a `phpMyAdmin` (usualmente en `http://localhost/phpmyadmin`).
3. Crea una nueva base de datos llamada `ac_computers_db`.
4. El sistema generará las tablas automáticamente al iniciar (consulte `DB_URL` en las variables de entorno).

### 3. Configuración de Inteligencia Artificial (Ollama)

1. Instala [Ollama](https://ollama.com/).
2. Abre una terminal y descarga el modelo requerido por el sistema:
    ```bash
    ollama pull ministral-3:8b
    ```
3. Asegúrate de que Ollama se esté ejecutando en el puerto predeterminado (11434).

### 4. Variables de Entorno

Crea un archivo `.env` en la raíz del directorio `/server` (puedes basarte en `.env.example`) y configura los siguientes valores:

```env
# Servidor
SERVER_PORT=8081

# Base de Datos
DB_URL=jdbc:mysql://localhost:3306/ac_computers_db
DB_USER=root
DB_PASSWORD=

# Seguridad y JWT
JWT_SECRET=tu_secreto_seguro_para_jwt
JWT_EXPIRATION=3600000

# Cloudinary (Almacenamiento de Imágenes)
CLOUDINARY_CLOUD_NAME=tu_cloud_name
CLOUDINARY_API_KEY=tu_api_key
CLOUDINARY_API_SECRET=tu_api_secret

# E-mail (Configuración de Java Mail - Opcional para dev)
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=tu_email@gmail.com
SPRING_MAIL_PASSWORD=tu_password_de_aplicacion
```

### 5. Ejecución del Servidor

Desde la raíz del proyecto `/server`, ejecuta el siguiente comando:

```bash
./gradlew bootRun
```

Una vez iniciado, el backend estará disponible en: **`http://localhost:8081/api`**

---

## Ejecución de Pruebas (Testing)

Para ejecutar los tests del sistema de manera segura sin afectar los datos de desarrollo:

1. **Base de Datos de Pruebas**: Crea una base de datos independiente en XAMPP llamada `ac_computers_test_db`.
2. **Variables de Entorno para Tests**: Asegúrate de tener configurado un archivo `.env` o las variables necesarias para el entorno de pruebas, apuntando a la nueva base de datos:
    ```env
    DB_URL=jdbc:mysql://localhost:3306/ac_computers_test_db
    ```
3. **Ejecutar Tests**:
    ```bash
    ./gradlew test
    ```
