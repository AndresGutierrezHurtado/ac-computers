class Product {
    constructor(conn){
        this.conn = conn;
    }

    getAll(req, res) {
        this.conn.query('SELECT * FROM products;', (err, result) => {
            if(err) res.status(500).send('Error en el servidor');
            res.json(result); 
        });
    }

    getById(req, res) {
        this.conn.query('SELECT * FROM products WHERE product_id = ?', [req.params.id], (err, result) => {
            if(err) res.status(500).send('Error en el servidor');
            res.json(result); 
        });
    }
}

module.exports = Product;