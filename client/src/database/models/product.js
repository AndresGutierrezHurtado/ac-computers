"use strict";
const { Model } = require("sequelize");

module.exports = (sequelize, DataTypes) => {
    class Product extends Model {}

    Product.init(
        {
            product_id: {
                type: DataTypes.UUID,
                defaultValue: DataTypes.UUIDV4,
                primaryKey: true,
            },
            product_name: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            product_description: {
                type: DataTypes.TEXT,
                allowNull: false,
            },
            product_image_url: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            product_price: {
                type: DataTypes.DECIMAL,
                allowNull: false,
            },
            product_discount: {
                type: DataTypes.INTEGER,
                allowNull: false,
                defaultValue: 0,
            },
            product_date: {
                type: DataTypes.DATE,
                allowNull: false,
                defaultValue: DataTypes.NOW,
            },
            category_id: {
                type: DataTypes.INTEGER,
                allowNull: false,
            },
        },
        {
            sequelize,
            modelName: "Product",
            timestamps: false,
        }
    );

    return Product;
};
