const PDFDocument = require('pdfkit');

class Product {
    constructor(conn){
        this.conn = conn;
    }

    getAll(req, res) {
        this.conn.query(`SELECT * FROM products ORDER BY ${req.query.sort || 'product_id'} ${req.query.order || 'asc'} `, (err, result) => {
            if (err) {
                console.error('Error en la consulta:', err);
                return res.status(500).send('Error en el servidor');
            }
            if (!result || result.length === 0) {
                console.warn('No se encontraron resultados');
                return res.status(404).send('No se encontraron resultados');
            }
            return res.json(result);
        });
    }
    
    getById(req, res) {
        this.conn.query('SELECT * FROM products WHERE product_id = ?', [req.params.id], (err, result) => {
            if(err) res.status(500).send('Error en el servidor');
            res.json(result); 
        });
    }

    generatePDF(req, res) {
        let products = [];

        this.conn.query('SELECT * FROM products', (err, result) => {
            if (err) {
                console.error('Error en la consulta:', err);
                return res.status(500).send('Error en el servidor');
            }

            result.forEach(product => {
                products.push(product);
            });

            const doc = new PDFDocument();
            const pdfFileName = 'lista_productos.pdf';

            // Nombre de la empresa
            doc.font('Helvetica-Bold')
                .fontSize(30)
                .fillColor('#7270ec')
                .text('AC COMPUTERS', { align: 'center' })
                .moveDown(0.2);

            // Título
            doc.fontSize(20)
                .fillColor('#000')
                .text('Lista de productos', { align: 'center'})
                .moveDown();

            // Tabla de productos
            const table = {
                headers: ['Nombre', 'Precio', 'Descuento'],
                rows: products.map(product => [product.product_name, 'COP ' + Number(product.product_price).toLocaleString("es-CO") , product.product_discount ? `${product.product_discount}%` : 'Sin descuento']),
            };

            function drawTable() {
                doc.moveDown();

                let startX = 50;
                let startY = doc.y;
                const padding = 10;
                const columnWidths = [200, 150, 150];  // Actualiza los anchos de columna según el número de columnas

                // Header tabla productos
                doc.font('Helvetica-Bold').fontSize(14);
                table.headers.forEach((header, columnIndex) => {
                    doc.rect(startX, startY, columnWidths[columnIndex], padding * 2).fill('#f0f0f0');
                    doc.rect(startX, startY, columnWidths[columnIndex], padding * 2).stroke();
                    doc.fillColor('black');
                    doc.text(header, startX + 5, startY + 5, { width: columnWidths[columnIndex] - 10 });
                    startX += columnWidths[columnIndex];
                });

                startX = 50;
                startY += padding;

                // Body tabla productos
                doc.font('Helvetica').fontSize(12);
                table.rows.forEach((row) => {
                    let maxCellHeight = 0;

                    row.forEach((cell, cellIndex) => {
                        const cellHeight = doc.heightOfString(cell, { width: columnWidths[cellIndex] - 10 });
                        maxCellHeight = Math.max(maxCellHeight, cellHeight);
                    });

                    // Verificar si hay suficiente espacio en la página actual
                    if (startY + (maxCellHeight + padding) > doc.page.height - 80) {
                        doc.addPage();
                        startY = 50; 
                    }

                    startY += padding;
                    doc.y = startY;

                    row.forEach((cell, cellIndex) => {
                        doc.rect(startX, startY, columnWidths[cellIndex], maxCellHeight + padding).stroke();
                        doc.text(cell, startX + 5, startY + 5, { width: columnWidths[cellIndex] - 10 });
                        startX += columnWidths[cellIndex];
                    });

                    startX = 50;
                    startY += maxCellHeight;
                });
            }

            // Llamar a la función para dibujar la tabla
            drawTable();

            // Finalizar y enviar el PDF como respuesta
            res.setHeader('Content-Disposition', `attachment; filename="${pdfFileName}"`);
            res.setHeader('Content-Type', 'application/pdf');
            doc.pipe(res);
            doc.end();
        });
    }
}

module.exports = Product;