"use strict";
/** @type {import('sequelize-cli').Migration} */

module.exports = {
    async up(queryInterface, Sequelize) {
        await queryInterface.createTable("Products", {
            product_id: {
                type: Sequelize.UUID,
                defaultValue: Sequelize.UUIDV4,
                primaryKey: true,
            },
            product_name: {
                type: Sequelize.STRING,
            },
            product_description: {
                type: Sequelize.TEXT,
            },
            product_image_url: {
                type: Sequelize.STRING,
            },
            product_price: {
                type: Sequelize.DECIMAL,
            },
            product_discount: {
                type: Sequelize.INTEGER,
            },
            product_date: {
                type: Sequelize.DATE,
            },
            category_id: {
                type: Sequelize.INTEGER,
            }
        });
    },
    async down(queryInterface, Sequelize) {
        await queryInterface.dropTable("Products");
    },
};
