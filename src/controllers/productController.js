const Product = require('../models/product');

class ProductController {

    constructor(conn) {
        this.conn = conn;
        this.productModel = new Product(conn);
    }

    getProducts(req, res) {
        this.productModel.getAll(req, res);
    }

    getProduct(req, res) {
        this.productModel.getById(req, res);
    }

    list(req, res) {
        this.productModel.generatePDF(req, res);
    }

}

module.exports = ProductController;