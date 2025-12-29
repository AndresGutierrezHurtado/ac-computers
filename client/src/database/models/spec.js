"use strict";
const { Model } = require("sequelize");

module.exports = (sequelize, DataTypes) => {
    class Spec extends Model {}

    Spec.init(
        {
            spec_id: {
                type: DataTypes.UUID,
                defaultValue: DataTypes.UUIDV4,
                primaryKey: true,
            },
            spec_key: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            spec_value: {
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
            modelName: "Spec",
            timestamps: false,
        }
    );

    return Spec;
};
