"use strict";
/** @type {import('sequelize-cli').Migration} */

module.exports = {
    async up(queryInterface, Sequelize) {
        await queryInterface.createTable("Recoveries", {
            recovery_id: {
                type: Sequelize.UUID,
                defaultValue: Sequelize.UUIDV4,
                primaryKey: true,
            },
            user_id: {
                type: Sequelize.UUID,
                allowNull: false,
            },
            recovery_expiration: {
                type: Sequelize.DATE,
                allowNull: false,
            },
            recovery_state: {
                type: Sequelize.ENUM("active", "used"),
                defaultValue: "active",
            },
        });
    },
    async down(queryInterface, Sequelize) {
        await queryInterface.dropTable("Recoveries");
    },
};
