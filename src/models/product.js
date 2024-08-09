const PDFDocument = require('pdfkit');

class Product {
    constructor(conn){
        this.conn = conn;
    }

    getAll(req, res) {
        // Obtener todos los productos
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

    paginate(req, res) {
        const { page = 1, limit = 5, search = "" } = req.query;
    
        // Obtener la cantidad de registros que coinciden con la búsqueda
        const countQuery = `SELECT COUNT(*) AS count FROM products WHERE product_name LIKE ? or product_description LIKE ? or product_id LIKE ?`;
        this.conn.query(countQuery, [`%${search}%`, `%${search}%`, `%${search}%`], (err, countResult) => {

            if (err) {
                console.error('Error en la consulta:', err);
                return res.status(500).send('Error en el servidor 1');
            }
    
            const totalCount = countResult[0].count;
            const totalPages = Math.ceil(totalCount / limit);
            
            // Obtener los registros paginados
            const query = `SELECT * FROM products WHERE product_name LIKE '%${search}%' or product_description LIKE '%${search}%' or product_id LIKE '%${search}%' ORDER BY ${req.query.sort || 'product_id'} ${req.query.order || 'asc'} LIMIT ${(page - 1) * limit}, ${limit}`;
            this.conn.query(query, (err, result) => {
                if (err) {
                    console.error('Error en la consulta:', err);
                    return res.status(500).send('Error en el servidor 2');
                }
                if (!result || result.length === 0) {
                    console.warn('No se encontraron resultados');
                    return res.status(404).send('No se encontraron resultados');
                }
                
                // Enviar los resultados paginados junto con la información de paginación
                res.json({
                    products: result,
                    page,
                    limit,
                    totalPages,
                    totalCount
                });
            });
        });
    }   
    
    getById(req, res) {
        // Obtener un solo producto
        this.conn.query('SELECT * FROM products WHERE product_id = ?', [req.params.id], (err, result) => {
            if(err) res.status(500).send('Error en el servidor');
            res.json(result); 
        });
    }

    create(data) {
        return new Promise((resolve, reject) => {
            let query = 'INSERT INTO products (';
    
            for (const key in data) {
                query += `${key}, `;
            }
    
            query = query.slice(0, -2);
            query += ') VALUES (';
    
            for (const key in data) {
                query += `'${data[key]}', `;
            }
    
            query = query.slice(0, -2);
            query += ');';
    
            this.conn.query(query, (err, result) => {
                if (err) {
                    console.error('Error en la consulta:', err);
                    return reject({ success: false, message: 'Error en el servidor' });
                }
                resolve({ success: true, message: 'Producto creado exitosamente', product_id: result.insertId });
            });
        });
    }

    updateById(id, data, res) {
        // Actualizar un solo producto
        let query = 'UPDATE products SET ';
        for (const key in data) {
            query += `${key} = '${data[key]}', `;
        }
        query = query.slice(0, -2);
        query += ' WHERE product_id = ' + id;

        this.conn.query(query, [], (err, result) => {
            if (err) {
                console.error('Error en la consulta:', err);
                return res.status(500).json({ success: false, message: 'Error en el servidor' });
            }
            if (result.affectedRows === 0) {
                console.warn('No se encontró el producto para actualizar');
                return res.status(404).json({ success: false, message: 'No se encontró el producto' });
            }
            return res.json({ success: true, message: 'Producto actualizado exitosamente' });
        });
    }

    deleteById(id, res) {
        // Eliminar un solo producto
        this.conn.query('DELETE FROM products WHERE product_id = ?', [id], (err, result) => {
            if (err) {
                console.error('Error en la consulta:', err);
                return res.status(500).json({ success: false, message: 'Error en el servidor' });
            }
            if (result.affectedRows === 0) {
                console.warn('No se encontró el producto para eliminar');
                return res.status(404).json({ success: false, message: 'No se encontró el producto ' + id });
            }
            return res.json({ success: true, message: 'Producto eliminado exitosamente' });
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