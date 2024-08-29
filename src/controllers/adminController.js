require("dotenv").config();
const { data } = require("autoprefixer");
const path = require("path");
const Product = require("../models/product");
const fs = require("fs");
const jwt = require("jsonwebtoken");

const secretKey = process.env.SECRET_KEY;

class AdminController {
    constructor(conn) {
        this.conn = conn;
        this.productModel = new Product(conn);
    }

    index(req, res) {
        if (!req.params.token) {
            return res.redirect("/auth");
        }

        const decoded = jwt.decode(req.params.token, { complete: true });

        if (!decoded) {
            return res.redirect("/auth");
        }

        // Verificar la expiración del token
        const now = Math.floor(Date.now() / 1000); 
        const expiration = decoded.payload.exp;

        if (now > expiration) {
            return res.redirect("/auth");
        }

        res.sendFile(path.join(__dirname, "../views/admin", "index.html"));
    }

    auth(req, res) {
        res.sendFile(path.join(__dirname, "../views/admin", "auth.html"));
    }

    createProduct(req, res) {
        let data = {
            product_name: req.body.product_name,
            product_description: req.body.product_description,
            product_price: req.body.product_price,
            product_discount: req.body.product_discount,
        };

        this.productModel
            .create(data)
            .then((result) => {
                if (result.success) {
                    if (req.file) {
                        const filename = `${result.product_id}.png`;
                        const filepath = path.join(
                            __dirname,
                            "../../public/images/products",
                            filename
                        );

                        fs.writeFile(filepath, req.file.buffer, (err) => {
                            if (err) {
                                console.error(
                                    "Error al guardar el archivo:",
                                    err
                                );
                                return res
                                    .status(500)
                                    .send("Error al guardar la imagen.");
                            }

                            data.product_image_url = `/images/products/${filename}`;
                            this.productModel.updateById(
                                result.product_id,
                                data,
                                res
                            );
                        });
                    } else {
                        res.send(result);
                    }
                }
            })
            .catch((error) => {
                res.status(500).json(error);
            });
    }

    updateProduct(req, res) {
        let data = {
            product_name: req.body.product_name,
            product_description: req.body.product_description,
            product_price: req.body.product_price,
            product_discount: req.body.product_discount,
        };

        // Guarda la imagen si se proporciona
        if (req.file) {
            const filename = `${req.params.id}.png`;
            const filepath = path.join(
                __dirname,
                "../../public/images/products",
                filename
            );

            fs.writeFile(filepath, req.file.buffer, (err) => {
                if (err) {
                    console.error("Error al guardar el archivo:", err);
                    return res.status(500).send("Error al guardar la imagen.");
                }

                data.product_image_url = `/images/products/${filename}`;
                this.productModel.updateById(req.params.id, data, res);
            });
        } else {
            this.productModel.updateById(req.params.id, data, res);
        }
    }

    deleteProduct(req, res) {
        this.productModel.deleteById(req.params.id, res);
    }
}

module.exports = AdminController;
