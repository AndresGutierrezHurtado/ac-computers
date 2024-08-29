const User = require("../models/user");

class UserController {
    constructor(conn) {
        this.conn = conn;
        this.userModel = new User(conn);
    }

    login(req, res) {
        this.userModel.authUser(req, res);
    }

}

module.exports = UserController;
