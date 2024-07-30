class Product {
    constructor(conn){
        this.conn = conn;
    }

    getAll(req, res) {
        this.conn.query('SELECT * FROM products;', (err, result) => {
            if (err) {
                console.error('Error en la consulta:', err);
                return res.status(500).send('Error en el servidor');
            }
            if (!result || result.length === 0) {
                console.warn('No se encontraron resultados');
                return res.status(404).send('No se encontraron resultados');
            }
            console.log('Resultados de la consulta:', result);
            return res.json(result);
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