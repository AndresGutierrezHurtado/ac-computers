const fs = require("fs");

module.exports = {
    development: {
        username: "admin",
        password: "admin",
        database: "ac-computers",
        host: "localhost",
        dialect: "postgres",
        port: 5432,
        dialectOptions: {
            bigNumberStrings: true,
        },
    },
    test: {
        username: "admin",
        password: "admin",
        database: "ac-computers",
        host: "localhost",
        dialect: "postgres",
        port: 5432,
    },
    production: {
        username: process.env.DB_USER,
        password: process.env.DB_PASSWORD,
        database: process.env.DB_NAME,
        host: process.env.DB_HOST,
        port: process.env.DB_PORT,
        dialect: "postgres",
        dialectOptions: {
            bigNumberStrings: true,
        },
    },
};
