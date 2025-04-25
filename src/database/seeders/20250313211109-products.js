"use strict";
/** @type {import('sequelize-cli').Migration} */

const { categories, products, specs, multimedias } = require("./data.json");

module.exports = {
    async up(queryInterface, Sequelize) {
        /**
         * Add seed commands here.
         *
         * Example:
         * await queryInterface.bulkInsert('People', [{
         *   name: 'John Doe',
         *   isBetaMember: false
         * }], {});
         */

        if (categories.length > 0) await queryInterface.bulkInsert("Categories", categories, {});
        if (products.length > 0) await queryInterface.bulkInsert("Products", products, {});
        if (specs.length > 0) await queryInterface.bulkInsert("Specs", specs, {});
        if (multimedias.length > 0) await queryInterface.bulkInsert("Multimedia", multimedias, {});
    },

    async down(queryInterface, Sequelize) {
        /**
         * Add commands to revert seed here.
         *
         * Example:
         * await queryInterface.bulkDelete('People', null, {});
         *
         */

        if (products.length > 0) await queryInterface.bulkDelete("Products", null, {});
        if (specs.length > 0) await queryInterface.bulkDelete("Specs", null, {});
        if (categories.length > 0) await queryInterface.bulkDelete("Categories", null, {});
        if (multimedias.length > 0) await queryInterface.bulkDelete("Multimedia", null, {});
    },
};
