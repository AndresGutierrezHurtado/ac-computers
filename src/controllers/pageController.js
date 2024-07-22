const path = require("path");


class PageController {
    home(req, res) {
        res.sendFile(path.join(__dirname, '../views', 'index.html'));
    }
}

module.exports = new PageController();