"use strict";
const { Model } = require("sequelize");

module.exports = (sequelize, DataTypes) => {
    class Multimedia extends Model {}

    Multimedia.init(
        {
            media_id: {
                type: DataTypes.UUID,
                defaultValue: DataTypes.UUIDV4,
                primaryKey: true,
            },
            media_url: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            product_id: {
                type: DataTypes.UUID,
                allowNull: false,
            },
        },
        {
            sequelize,
            modelName: "Multimedia",
            timestamps: false,
        }
    );

    return Multimedia;
};
