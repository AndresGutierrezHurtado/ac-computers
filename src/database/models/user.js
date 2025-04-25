"use strict";
const { Model } = require("sequelize");

module.exports = (sequelize, DataTypes) => {
    class User extends Model {}

    User.init(
        {
            user_id: {
                type: DataTypes.UUID,
                defaultValue: DataTypes.UUIDV4,
                primaryKey: true,
            },
            user_name: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            user_lastname: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            user_email: {
                type: DataTypes.STRING,
                allowNull: false,
                unique: true,
            },
            user_phone: {
                type: DataTypes.DECIMAL(10, 0),
                allowNull: false,
            },
            role_id: {
                type: DataTypes.INTEGER,
                allowNull: false,
                defaultValue: 1,
            },
            user_password: {
                type: DataTypes.TEXT,
                allowNull: false,
            },
        },
        {
            sequelize,
            modelName: "User",
            timestamps: false,
        }
    );

    User.beforeCreate((user) => {
        user.user_password = require("bcrypt").hashSync(user.user_password, 10);
    });

    User.beforeUpdate((user) => {
        if (user.changed("user_password")) {
            user.user_password = require("bcrypt").hashSync(user.user_password, 10);
        }
    });

    return User;
};
