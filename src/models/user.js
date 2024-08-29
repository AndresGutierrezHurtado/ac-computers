require('dotenv').config();
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");

const secretKey = process.env.SECRET_KEY

class User {
    constructor(conn) {
        this.conn = conn;
    }

    authUser(req, res) {
        
        const email = req.body.user_email;
        const password = req.body.user_password;
        
        const userQuery = "SELECT * FROM users WHERE user_email = ?";

        this.conn.query(userQuery, [email], async (err, result) => {
            try {
                if (err) {
                    console.error("Error al consultar la base de datos:", err);
                    res.status(500).json({
                        success: false,
                        message: "Error al iniciar sesión"
                    });
                }

                if (result.length > 0) {
                    const user = result[0];
                    const match = await bcrypt.compare(
                        password,
                        user.user_password
                    );

                    if (match) {
                        const tokenData = {
                            id: user.user_id,
                            firstname: user.user_firstname,
                            lastname: user.user_lastname,
                            email: user.user_email,
                        };

                        const token = jwt.sign(tokenData, secretKey, {
                            expiresIn: "5h",
                        });
                        res.json({
                            success: true,
                            message: "Te has autenticado correctamente.",
                            token: token,
                        });
                    } else {
                        res.status(401).json({
                            success: false,
                            message: "La contraseña no coincide."
                        });
                    }
                } else {
                    res.status(404).json({
                        success: false,
                        message: "Correo electrónico no encontrado"
                    });
                }
            } catch (error) {
                console.error(error.message);
                res.status(500).json({
                    success: false,
                    message: "Error al iniciar sesión."
                });
            }
        });

    }

    updateById(id, data, res) {
        let query = "UPDATE users SET ";

        const params = [];

        for (const key in data) {
            query += `${key} = ?, `;
            params.push(data[key]);
        }

        query = query.slice(0, -2);
        query += " WHERE user_id = ?";

        params.push(id);

        this.conn.query(query, params, (err, result) => {
            if (err) {
                console.error("Error en la consulta:", err);
                return res
                    .status(500)
                    .json({ success: false, message: "Error en el servidor" });
            }
            if (result.affectedRows === 0) {
                console.warn("No se encontró el producto para actualizar");
                return res.status(404).json({
                    success: false,
                    message: "No se encontró el producto",
                });
            }
            return res.json({
                success: true,
                message: "Producto actualizado exitosamente",
            });
        });
    }
}

module.exports = User;
