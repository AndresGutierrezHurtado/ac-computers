"use strict";
const { Model } = require("sequelize");

module.exports = (sequelize, DataTypes) => {
    class Recovery extends Model {}

    Recovery.init(
        {
            recovery_id: {
                type: DataTypes.UUID,
                defaultValue: DataTypes.UUIDV4,
                primaryKey: true,
            },
            user_id: {
                type: DataTypes.UUID,
                allowNull: false,
            },
            recovery_expiration: {
                type: DataTypes.DATE,
                allowNull: false,
            },
            recovery_state: {
                type: DataTypes.ENUM("active", "used"),
                defaultValue: "active",
            },
        },
        {
            sequelize,
            modelName: "Recovery",
            timestamps: false,
        }
    );

    return Recovery;
};
