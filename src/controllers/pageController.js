const path = require("path");
const fs = require("fs");


class PageController {
    home(req, res) {
        res.sendFile(path.join(__dirname, '../views', 'index.html'));
    }

    products(req, res) {
        res.sendFile(path.join(__dirname, '../views', 'products.html'));
    }

    product(req, res) {
        res.sendFile(path.join(__dirname, '../views', 'product.html'));
    }

    contact(req, res) {
        res.sendFile(path.join(__dirname, '../views', 'contact.html'));
    }

}

module.exports = new PageController();