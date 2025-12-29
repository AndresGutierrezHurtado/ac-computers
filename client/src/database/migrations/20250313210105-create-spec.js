"use strict";
/** @type {import('sequelize-cli').Migration} */

module.exports = {
    async up(queryInterface, Sequelize) {
        await queryInterface.createTable("Specs", {
            spec_id: {
                type: Sequelize.UUID,
                defaultValue: Sequelize.UUIDV4,
                primaryKey: true,
            },
            spec_key: {
                type: Sequelize.STRING,
            },
            spec_value: {
                type: Sequelize.STRING,
            },
            product_id: {
                type: Sequelize.UUID,
            }
        });
    },
    async down(queryInterface, Sequelize) {
        await queryInterface.dropTable("Specs");
    },
};
