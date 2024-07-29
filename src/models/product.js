class Product {
    constructor(conn){
        this.conn = conn;
    }

    getAll(req, res) {
        this.conn.query('SELECT * FROM products;', (err, result) => {
            if (err) {res.status(500).send('Error en el servidor');}
            if (result.length == 0 || !result) {res.status(404).send('No se encontraron resultados');}
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