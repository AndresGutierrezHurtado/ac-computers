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
                        product = data[0];
                        document.getElementById("product-image");
                        document.getElementById("product-title").innerHTML = product.product_name;
                        document.getElementById("product-description").innerHTML = product.product_description;
                        document.getElementById("product-price").innerHTML = Number(product.product_price - (product.product_price * (product.product_discount / 100)) ).toLocaleString("es-CO") ;
                        document.getElementById("product-discount").innerHTML = (product.product_discount > 0) ? product.product_discount + "%" : "Sin descuento";
                        document.getElementById("product-total-price").innerHTML = (product.product_discount > 0) ? Number(product.product_price).toLocaleString("es-CO") : " " ;
                    })
                </script>

            `);

            res.send(updatedHtml);
        });

    }
}

module.exports = new PageController();