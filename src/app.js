const express = require('express');
const path = require('path');
const multer = require('multer');
const upload = multer();
const ProductController = require('./controllers/productController');
const productController = new ProductController(require('./database/database').getConnection());
const AdminController = require('./controllers/adminController');
const adminController = new AdminController(require('./database/database').getConnection());
const PageController = require('./controllers/pageController');
const app = express();

// Static files
app.use(express.static(path.join(__dirname, '../public')));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Routes
app.get('/', (req, res) => PageController.home(req, res));
app.get('/product/:id', (req, res) => PageController.product(req, res));
app.get('/products', (req, res) => PageController.products(req, res));
app.get('/contact', (req, res) => PageController.contact(req, res));
app.get('/list', (req, res) => productController.list(req, res));

app.get('/admin', (req, res) => adminController.index(req, res));
app.get('/auth', (req, res) => adminController.auth(req, res));

// API Routes
app.get('/api/products', (req, res) => productController.getProducts(req, res));
app.get('/api/products/:id', (req, res) => productController.getProduct(req, res));
app.put('/api/products/:id', upload.single('product_image'), (req, res) => adminController.updateProduct(req, res));
app.delete('/api/products/:id', (req, res) => adminController.deleteProduct(req, res));

// Server
const port = 3000;

app.listen(port, () => {
    console.log(`Servidor funcionando en el puerto http://localhost:${port}`);
});