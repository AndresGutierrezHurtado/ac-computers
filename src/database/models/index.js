const { DataTypes, Sequelize } = require("sequelize");
const { [process.env.NODE_ENV]: config } = require("../config.cjs");

const sequelize = new Sequelize(config.database, config.username, config.password, {
    host: config.host,
    dialect: config.dialect,
    dialectModule: require("pg"),
    port: config.port,
    logging: false,
});

(async () => {
    try {
        await sequelize.authenticate();
        console.log("Connection has been established successfully.");
    } catch (error) {
        console.error("Unable to connect to the database:", error);
    }
})();

const UserModel = require("./user");
const CategoryModel = require("./category");
const ProductModel = require("./product");
const SpecModel = require("./spec");
const MultimediaModel = require("./multimedia");
const RoleModel = require("./role");
const RecoveryModel = require("./recovery");

const User = UserModel(sequelize, DataTypes);
const Category = CategoryModel(sequelize, DataTypes);
const Product = ProductModel(sequelize, DataTypes);
const Spec = SpecModel(sequelize, DataTypes);
const Multimedia = MultimediaModel(sequelize, DataTypes);
const Role = RoleModel(sequelize, DataTypes);
const Recovery = RecoveryModel(sequelize, DataTypes);

// User relations
User.belongsTo(Role, { foreignKey: "role_id", as: "role" });
Role.hasMany(User, { foreignKey: "role_id", as: "users" });

User.hasMany(Recovery, { foreignKey: "user_id", as: "recoveries" });
Recovery.belongsTo(User, { foreignKey: "user_id", as: "user" });

// Product relations
Product.belongsTo(Category, { foreignKey: "category_id", as: "category" });
Category.hasMany(Product, { foreignKey: "category_id", as: "products" });

Product.hasMany(Multimedia, { foreignKey: "product_id", as: "multimedias" });
Multimedia.belongsTo(Product, { foreignKey: "product_id", as: "product" });

Product.hasMany(Spec, { foreignKey: "product_id", as: "specs" });
Spec.belongsTo(Product, { foreignKey: "product_id", as: "product" });

module.exports = {
    User,
    Category,
    Product,
    Spec,
    Multimedia,
    Role,
    Recovery,
    connection: sequelize,
};
