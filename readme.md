# 🖥️ AC Computers - Sistema de Catálogo de Productos

Bienvenido al repositorio del **Sistema de Catálogo de Productos de AC Computers**, una plataforma desarrollada para optimizar la gestión de productos tecnológicos, incluyendo computadores y componentes, y mejorar la interacción con los clientes mediante herramientas modernas y accesibles.

Este sistema está diseñado para:

-   Mostrar un catálogo interactivo de productos de manera clara y ordenada.
-   Permitir a los clientes descargar un catálogo en formato PDF.
-   Ofrecer un formulario de contacto para atención directa por correo electrónico.
-   Visualizar ubicaciones importantes mediante mapas interactivos.

Está orientado a mejorar la experiencia del usuario final y facilitar la administración de productos por parte del equipo de **AC Computers**.

---

## 📚 Tabla de Contenidos

-   [Características Principales](#características-principales)
-   [Tecnologías Utilizadas](#tecnologías-utilizadas)
-   [Arquitectura del Proyecto](#arquitectura-del-proyecto)
-   [Instalación](#instalación)
-   [Contacto](#contacto)

---

## 🚀 Características Principales

1. **Catálogo de Productos**

-   Listado visual con detalles como nombre, precio, descuento y disponibilidad.
-   Enlaces directos a la página individual de cada producto.
-   Descarga del catálogo en PDF con diseño personalizado.

![]()

2. **Gestión de Inventario**

-   CRUD completo de productos (Crear, Leer, Actualizar, Eliminar).
-   Interfaz administrativa protegida.

![]()

3. **Contacto con Clientes**

-   Formulario de contacto vía correo electrónico.
-   Notificaciones automáticas al recibir mensajes.

![]()

4. **Mapas**

-   Integración con **Leaflet** para mostrar la ubicación del punto de venta.

![]()

5. **Diseño Moderno y Responsivo**

-   Interfaz desarrollada con **Tailwind CSS** y **DaisyUI** para una experiencia fluida en todos los dispositivos.
-   Integración de **Three.js** para añadir gráficos 3D interactivos que enriquecen la experiencia visual.

![]()

---

## 🛠️ Tecnologías Utilizadas

**Frontend**

-   React.js
-   Tailwind CSS
-   DaisyUI
-   Leaflet.js

**Backend**

-   Node.js
-   Express.js
-   Sequelize ORM
-   PostgreSQL
-   Puppeteer / Chromium

**Despliegue**

-   Vercel
-   Docker

---

## 🧱 Arquitectura del Proyecto

El proyecto sigue una estructura basada en el patrón **Cliente-Servidor**, separando claramente la lógica del negocio, la presentación y el manejo de rutas.

![Arquitectura del Proyecto](https://res.cloudinary.com/dyuh7jesr/image/upload/v1733257411/ac-computers/arquitectura-proyecto.png)

---

## 🔄️ Flujos de uso

### Usuario

### Administrador

---

## 📂 Estructura de carpetas

```
src/
├── app/                        # Directorio principal que contiene el enrutador y la API
|   ├── api/                    # Aquí se encuentran las rutas de la API
├── database/                   # Contiene todo lo relacionado con la base de datos, como modelos, seeders, migraciones y configuración
|   ├── models/                 # Modelos de la base de datos que definen la estructura de las tablas
|   ├── seeders/                # Seeders para poblar la base de datos con datos de prueba
|   ├── migrations/             # Migraciones para actualizar la estructura de la base de datos
|   └── config.cjs              # Archivo de configuración de la base de datos
├── components/                 # Componentes reutilizables de la interfaz de usuario
├── hooks/                      # Hooks personalizados para manejar lógica de negocio
|   └── useGetClientData.js     # Hook para obtener datos desde componentes para cielnte
├── layouts/                    # Estructuras de disposición de la aplicación, como páginas o plantillas
```

---

## 💾 Instalación

### Requisitos Previos

-   Node.js >= 18
-   PostgreSQL
-   Git

### Pasos

1. Clonar el repositorio:

```bash
git clone https://github.com/AndresGutierrezHurtado/ac-computers.git
```

2. Instalar dependencias:

```bash
npm install
```

3. Configurar variables de entorno:
   Crear un archivo `.env` basado en `.env.example`.

4. Ejecutar migraciones y sembrado de datos:

```bash
npm run db:migrate
npm run db:seed
```

5. Iniciar el servidor:

```bash
npm run dev
```

---

## 📬 Contacto

Para preguntas, soporte o colaboración, por favor contacta:

-   Andrés Gutiérrez Hurtado
-   Correo: [andres52885241@gmail.com](mailto:andres52885241@gmail.com)
-   LinkedIn: [Andrés Gutiérrez](https://www.linkedin.com/in/andr%C3%A9s-guti%C3%A9rrez-hurtado-25946728b/)
-   GitHub: [@AndresGutierrezHurtado](https://github.com/AndresGutierrezHurtado)
-   Portafolio: [Link portafolio](https://andres-portfolio-b4dv.onrender.com)
