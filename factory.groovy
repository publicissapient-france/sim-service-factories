import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.core.Vertx

Vertx vx = vertx
def logger = container.logger

def id = "factory-" + UUID.randomUUID().toString()

// Say hello every 1 second to the city
// https://github.com/xebia-france/sim-service-factories/blob/groovy/README.md#dites-hello

vx.setPeriodic(1000) { timerID ->
    logger.info("Say hello");
    vx.eventBus.publish("/city", [
            action: "hello",
            from: id,
            team: "your-team-name",
            type: "factory",
            version: "1.0"
    ]);
}

// Listen message on private address
vx.eventBus.registerHandler("/city/factory/" + id) { message ->
    logger.info("Get a message from " + message.body.from);
    logger.info("${message.body}");
    switch (message.body.action) {
    // Farm proposes an offer
        case "response":
            // Replies to the offer
            message.reply([
                    action: "acquittement",
                    from: id,
                    quantity: message.body.quantity
            ]);
            break;
        case "purchase":
            logger.info("Bank accepts a transaction");
            // Here manage your stock
            break;
    }
}

// Request hop from farms
// https://github.com/xebia-france/sim-service-factories/blob/groovy/README.md#achetez-du-houblon

vx.setTimer(2000) { timerId ->
    vx.eventBus.publish("/city/farm", [
            action: "request",
            from: id,
            quantity: 10
    ]);
}
