# 🖥️ AC Computers - Product Catalog System

[Version en Español](./README.es.md)

Welcome to the **AC Computers** repository, a platform developed to optimize the management of technological products, including computers and components, and to enhance customer interaction through modern and accessible tools.

This system is designed to:

-   Display an interactive and well-organized product catalog.
-   Allow customers to download a catalog in PDF format.
-   Offer a contact form for direct email communication.
-   Display important locations through interactive maps.

It is focused on improving the end-user experience and facilitating product administration by the **AC Computers** team.

![Product Catalog Image](./docs/screenshots/ac-computers.png)

---

## 📚 Table of Contents

-   [Main Features](#-main-features)
-   [Technologies Used](#️-technologies-used)
-   [Project Architecture](#-project-architecture)
-   [User Flows](#️-user-flows)
-   [Folder Structure](#-folder-structure)
-   [Installation](#-installation)
-   [Contact](#-contact)

---

## 🚀 Main Features

1. **Product Catalog and Custom PDF**

-   Product display with real-time details such as name, price, discount, and availability.
-   Direct access to individual product pages for more detailed descriptions.
-   Download a PDF catalog with an attractive design adapted to the brand identity.

![Product catalog view](./docs/screenshots/products.png)
![PDF catalog view](./docs/screenshots/pdf.png)

2. **Inventory Management**

-   Full CRUD for products: Create, Read, Update, and Delete.
-   Protected admin panel for efficient inventory management.
-   Optimized image storage through integration with **Cloudinary**.

![Inventory management panel](./docs/screenshots/admin.png)

3. **User Management**

-   Secure authentication system for user registration and login.
-   Fully editable user profiles.
-   Control panel to manage users, assign roles, and administer permissions.

![User management in admin panel](./docs/screenshots/auth.png)

4. **Customer Contact and Support**

-   Contact form connected via email for quick inquiries.
-   Automatic notifications to the admin upon receiving a new message.
-   Interactive map using **Leaflet** to display the company's location or sales points.

![Contact form and location map](./docs/screenshots/contact.png)

5. **Modern Design and Interactive Experience**

-   Responsive interface built with **Tailwind CSS** and **Next.js** for fast and professional navigation.
-   **Three.js** integration to incorporate 3D graphics and animations that enhance user experience.
-   Adaptive design providing an excellent experience on both mobile devices and desktops.

![Example of responsive design with 3D graphics](./docs/screenshots/model.png)

---

## 🛠️ Technologies Used

**Frontend**

-   React.js
-   Tailwind CSS
-   Three.js
-   DaisyUI
-   Leaflet.js

**Backend**

-   Node.js
-   Express.js
-   Sequelize ORM
-   PostgreSQL
-   Puppeteer / Chromium

**Deployment**

-   Vercel
-   Docker

---

## 🧱 Project Architecture

The project follows a structure based on the **Client-Server** pattern, clearly separating business logic, presentation, and route handling.

![Project Architecture](https://www.seobility.net/es/wiki/images/b/b3/API-Rest.png)

---

## 🔄️ User Flows

### User

-   **Public Navigation:** Access without the need to register or log in.
-   **Catalog Viewing:** Browse the product catalog organized by categories, with filters by name, price, or availability.
-   **Product Details:** Access an individual page for each product with detailed descriptions, high-quality images, and discounted prices.
-   **PDF Catalog Download:** Generate and download an updated PDF catalog containing all available product information.
-   **Contact Form:** Send inquiries, quotes, or information requests directly to the support team, receiving automatic confirmations.
-   **Interactive Map:** View the exact location of the store or distribution points through Leaflet, allowing for directions or references.

### Administrator

-   **Secure Login:** Access the admin panel through protected authentication.
-   **Full Product Management:**
    -   Create new products with images stored in **Cloudinary**.
    -   Edit existing product information such as name, price, stock, and technical specifications.
    -   Safely delete outdated products from the catalog.
    -   View a general and detailed listing of all registered products.
-   **Image Management:**
    -   Upload optimized images and store them externally on **Cloudinary** for optimal performance.
-   **User Management:**
    -   View the list of registered users.
    -   Edit user profile information.
    -   Assign or change user roles (user or admin).
-   **Receiving Contact Messages:**
    -   View contact messages received through the form directly in the admin panel.
    -   Manage customer requests quickly and efficiently.
-   **General Supervision:**
    -   Monitor the overall platform status.
    -   Ensure proper functionality of the catalog, forms, and data storage.

---

## 📂 Folder Structure

```text
src/
├── app/                        # Main directory containing the router and API
|   ├── api/                    # Here are the API routes
├── database/                   # Contains everything related to the database, such as models, seeders, migrations, and configuration
|   ├── models/                 # Database models defining the structure of the tables
|   ├── seeders/                # Seeders to populate the database with test data
|   ├── migrations/             # Migrations to update the database structure
|   └── config.cjs              # Database configuration file
├── components/                 # Reusable UI components
├── hooks/                      # Custom hooks to handle business logic
|   └── useGetClientData.js     # Hook to retrieve data from components for the client
├── layouts/                    # Layout structures of the application, such as pages or templates
```

---

## 💾 Installation

### Prerequisites

-   Node.js >= 18
-   PostgreSQL
-   Git

### Steps

1. Clone the repository:

```bash
git clone https://github.com/AndresGutierrezHurtado/ac-computers.git
```

2. Install dependencies:

```bash
npm install
```

3. Set up environment variables:
   Create a `.env` file based on `.env.example`.

4. Run migrations and seeders:

```bash
npm run db:migrate
npm run db:seed
```

5. Start the server:

```bash
npm run dev
```

---

## 📬 Contact

For questions, support, or collaboration, please contact:

-   Andrés Gutiérrez Hurtado
-   Email: [andres52885241@gmail.com](mailto:andres52885241@gmail.com)
-   LinkedIn: [Andrés Gutiérrez](https://www.linkedin.com/in/andr%C3%A9s-guti%C3%A9rrez-hurtado-25946728b/)
-   GitHub: [@AndresGutierrezHurtado](https://github.com/AndresGutierrezHurtado)
-   Portfolio: [Portfolio Link](https://andres-portfolio-b4dv.onrender.com)
