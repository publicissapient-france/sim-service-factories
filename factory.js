var vertx = require('vertx');
var container = require('vertx/container');
var console = require('vertx/console');
var utils = require('./utils.js');

var id = 'factory-' + utils.uuid();

// Send every 1 second a hello to the city
// https://github.com/xebia-france/sim-service-factories/tree/javascript#dites-hello

vertx.setPeriodic(1000, function (timerId) {
    container.logger.info("Say hello");
    vertx.eventBus.publish("/city", {
        action: "hello",
        from: id,
        team: <- YOUR TEAM NAME HERE
        type: "factory",
        version: "1.0"
    });
});

// Listen message on private address
vertx.eventBus.registerHandler('/city/factory/' + id, function (message, replier) {

    var action = message.action;

    container.logger.info(JSON.stringify(message));
    switch (action) {
        case "response":
            container.logger.info("Get a response from farm " + message.from);
            replier({
                action: "acquittement",
                from: id,
                quantity: message.quantity
            });
            break;
        case "purchase":
            // Here manage your stock
            container.logger.info("Bank accepts a transaction");
            break;
    }

});

// Send 1 request to farms
// https://github.com/xebia-france/sim-service-factories/tree/javascript#achetez-du-houblon

vertx.setTimer(2000, function (timerID) {
    vertx.eventBus.publish("/city/farm", {
        action: "request",
        from: id,
        quantity: 10
    });
});
