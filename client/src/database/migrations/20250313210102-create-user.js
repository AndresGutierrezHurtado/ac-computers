"use strict";
/** @type {import('sequelize-cli').Migration} */

module.exports = {
    async up(queryInterface, Sequelize) {
        await queryInterface.createTable("Users", {
            user_id: {
                type: Sequelize.UUID,
                defaultValue: Sequelize.UUIDV4,
                primaryKey: true,
            },
            user_name: {
                type: Sequelize.STRING,
            },
            user_lastname: {
                type: Sequelize.STRING,
            },
            user_email: {
                type: Sequelize.STRING,
                unique: true,
            },
            user_phone: {
                type: Sequelize.DECIMAL,
            },
            role_id: {
                type: Sequelize.INTEGER,
                defaultValue: 1,
            },
            user_password: {
                type: Sequelize.TEXT,
            }
        });
    },
    async down(queryInterface, Sequelize) {
        await queryInterface.dropTable("Users");
    },
};
