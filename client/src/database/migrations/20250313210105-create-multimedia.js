"use strict";
/** @type {import('sequelize-cli').Migration} */

module.exports = {
    async up(queryInterface, Sequelize) {
        await queryInterface.createTable("Multimedia", {
            media_id: {
                type: Sequelize.UUID,
                defaultValue: Sequelize.UUIDV4,
                primaryKey: true,
            },
            media_url: {
                type: Sequelize.STRING,
            },
            product_id: {
                type: Sequelize.UUID,
            }
        });
    },
    async down(queryInterface, Sequelize) {
        await queryInterface.dropTable("Multimedia");
    },
};
