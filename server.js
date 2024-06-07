const express = require('express');
const path = require('path');
const xlsx = require('xlsx');
const PDFDocument = require('pdfkit');

const app = express();

// Directorio público para servir archivos estáticos
app.use(express.static(path.join(__dirname)));

// Ruta para leer el archivo Excel y enviar los datos al frontend
app.get('/api/precios', (req, res) => {
    const workbook = xlsx.readFile('db/base_de_datos.xlsx');
    const sheetName = workbook.SheetNames[0];
    const data = xlsx.utils.sheet_to_json(workbook.Sheets[sheetName]);
    res.json(data);
});

// Ruta para leer el archivo Excel y enviar los datos de lista al frontend
app.get('/api/lista', (req, res) => {
    const workbook = xlsx.readFile('db/base_de_datos.xlsx');
    const sheetName = workbook.SheetNames[0];
    const data = xlsx.utils.sheet_to_json(workbook.Sheets[sheetName]);
    res.json(data);
});

// Ruta del servidor raíz
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

// Ruta para la página lista
app.get('/lista', (req, res) => {
    res.sendFile(path.join(__dirname, 'lista.html'));
});

// guardar listado de precios en archivo como pdf
app.get('/pdf/lista', (req, res) => {
    const workbook = xlsx.readFile('db/base_de_datos.xlsx');
    const sheetName = workbook.SheetNames[0];
    const data = xlsx.utils.sheet_to_json(workbook.Sheets[sheetName]);

    // Generar el PDF
    const doc = new PDFDocument();
    const pdfFileName = 'lista_productos.pdf';
    
    // Nombre de la empresa
    doc.font('Helvetica-Bold')
    .fontSize(25)
    .fillColor('#7270ec')
    .text('STORE LELOUCH TECHNOLOGY', { align: 'center' })
    .moveDown();
    
    // Título
    doc.fontSize(20)
    .fillColor('#000')
    .text('Lista de productos', { align: 'center' })
    .moveDown();

    // Tabla de productos
    const table = {
        headers: ['Nombre', 'Precio'],
        rows: data.map(producto => [`${producto.Nombre}`, `${producto.Precio.toLocaleString()} COP`])
    };

    function drawTable() {
        doc.moveDown(); 
    
        let startX = 50;
        let startY = doc.y;
        const padding = 10;
        const columnWidths = [350, 150];
    
        // Header tabla productos
        doc.font('Helvetica-Bold').fontSize(14);
        table.headers.forEach((header, columnIndex) => {
            doc.rect(startX, startY, columnWidths[columnIndex], padding * 2).fill('#f0f0f0');
            doc.rect(startX, startY, columnWidths[columnIndex], padding * 2).stroke();
            doc.fillColor('black');
            doc.text(header, startX + 5, startY + 5, { width: columnWidths[columnIndex] - 10});
            startX += columnWidths[columnIndex];
        });
    
        startX = 50;
        startY += padding;
    
        // Body tabla productos
        doc.font('Helvetica').fontSize(12);
        table.rows.forEach((row, rowIndex) => {

            let maxCellHeight = 0;

            row.forEach((cell, cellIndex) => {
                const cellHeight = doc.heightOfString(cell, { width: columnWidths[cellIndex] - 10 });
                maxCellHeight = Math.max(maxCellHeight, cellHeight);
            });
    
            // Verificar si hay suficiente espacio en la página actual
            if (startY + (maxCellHeight + padding ) > doc.page.height - 80) {
                // Cambiar a una nueva página
                doc.addPage();
                startY = 50; // Restablecer la posición inicial en la nueva página
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

// Iniciar el servidor en el puerto 3000
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Servidor iniciado en el puerto ${PORT}`);
});

