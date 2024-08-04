const path = require('path');

class AdminController {

    index(req, res) {
        res.sendFile(path.join(__dirname, '../views/admin', 'index.html'));
    }

    auth(req, res) {
        res.sendFile(path.join(__dirname, '../views/admin', 'auth.html'));
    }

}

module.exports = new AdminController();