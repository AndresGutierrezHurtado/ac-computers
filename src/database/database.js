const mysql = require('mysql2');
require('dotenv').config();

class Database {
    constructor() {
        this.conn = mysql.createConnection({
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            database: process.env.DB_NAME,
            user: process.env.DB_USER,
            password: process.env.DB_PASSWORD
        });

        this.conn.connect(err => { if (err) console.error('Error al conectar a la base de datos: ' + err.stack) });
    }

    getConnection() {
        return this.conn;
    }
}

module.exports = new Database();