"use strict";
/** @type {import('sequelize-cli').Migration} */

const { users, roles, recoveries } = require("./data.json");

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

        if (users.length > 0) await queryInterface.bulkInsert("Users", users, {});
        if (roles.length > 0) await queryInterface.bulkInsert("Roles", roles, {});
        if (recoveries.length > 0) await queryInterface.bulkInsert("Recoveries", recoveries, {});
    },

    async down(queryInterface, Sequelize) {
        /**
         * Add commands to revert seed here.
         *
         * Example:
         * await queryInterface.bulkDelete('People', null, {});
         */

        if (users.length > 0) await queryInterface.bulkDelete("Users", null, {});
        if (roles.length > 0) await queryInterface.bulkDelete("Roles", null, {});
        if (recoveries.length > 0) await queryInterface.bulkDelete("Recoveries", null, {});
    },
};
