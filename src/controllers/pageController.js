const path = require("path");
const fs = require("fs");


class PageController {
    home(req, res) {
        res.sendFile(path.join(__dirname, '../views', 'index.html'));
    }

    product(req, res) {
        const id = req.params.id;

        fs.readFile(path.join(__dirname, '../views', 'product.html'), 'utf8', (err, data) => {
            if (err) { return res.status(500).send('Error reading the HTML file.'); }

            // Inserta los datos del producto en el archivo HTML
            const updatedHtml = data.replace('<!-- SCRIPT_PLACEHOLDER -->', `
                <script>
                    fetch('/api/products/${id}')
                    .then(response => response.json())
                    .then(data => {
                        product = data;
                        console.log(product);
                    })
                </script>
            `);

            res.send(updatedHtml);
        });

    }
}

module.exports = new PageController();