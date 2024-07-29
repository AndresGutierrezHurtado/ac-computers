const express = require('express');
const path = require('path');
const ProductController = require('./controllers/productController');
const productController = new ProductController(require('./database/database').getConnection());
const PageController = require('./controllers/pageController');
const app = express();

// Static files
app.use(express.static(path.join(__dirname, '../public')));

// Routes
app.get('/', (req, res) => PageController.home(req, res));
app.get('/product/:id', (req, res) => PageController.product(req, res));

// API Routes
app.get('/api/products', (req, res) => productController.getProducts(req, res));
app.get('/api/products/:id', (req, res) => productController.getProduct(req, res));

// Server
const port = process.env.PORT || 3000;

app.listen(port, () => {
    console.log(`Servidor funcionando en el puerto http://localhost:${port}`);
});