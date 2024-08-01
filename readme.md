# AC Computers
Este proyecto representa la tienda "AC Computers", que ofrece una amplia gama de productos tecnológicos de alta calidad. La tienda está diseñada para proporcionar una experiencia de compra fácil y agradable a los clientes, con una interfaz intuitiva y funcionalidades que facilitan la exploración y la compra de productos.

## Guía de uso

### Iniciar el proyecto
Para iniciar el proyecto, sigue estos pasos:

1. Descargar el proyecto o clonarlo.

2. Navega hasta el directorio del proyecto:
   ```bash
   cd ac-computers
   ```

3. Instala las dependencias del proyecto utilizando npm:
   ```bash
   npm install 
   ```

4. Subir la base de datos a phpMyAdmin:
   - Accede a phpMyAdmin a través de tu navegador web.
   - Crea una nueva base de datos para el proyecto.
   - Importa el archivo SQL de la base de datos que se encuentra en la carpeta `src/database` del proyecto.

### Ejecutar el proyecto en modo de desarrollo
Para ejecutar el proyecto en modo de desarrollo, utiliza los siguientes comandos:
```bash
npm run serve
```

Estos comandos iniciarán el servidor de desarrollo y abrirán la aplicación en tu navegador predeterminado. La aplicación se recargará automáticamente si realizas cambios en los archivos del código fuente.

### Acceder a la aplicación
Una vez que el servidor esté en funcionamiento, puedes acceder a la aplicación desde tu navegador web visitando la siguiente URL:
[http://localhost:3000](http://localhost:3000)


### Cosas pendientes por hacer:
- arreglar precios, que funcione el descuento
- lista de precios pdf
- agregar más parametros para las consultas SQL
- sección administrador

git config --global user.name "w3schools-test"
git config --global user.email "test@w3schools.com"