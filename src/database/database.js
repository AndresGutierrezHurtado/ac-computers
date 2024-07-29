const mysql = require('mysql');

class Database {
    constructor() {
        this.conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'ac-computers'
        });

        this.conn.connect(err => { if (err) console.error('Error al conectar a la base de datos: ' + err.stack) });
    }

    getConnection() {
        return this.conn;
    }
}

module.exports = new Database();