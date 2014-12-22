import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.UUID;

/**
 * Created by Xebia on 12/10/2014.
 */
public class Factory extends Verticle {

    public void start() {
        EventBus eventBus = vertx.eventBus();

        String id = "factory-" + UUID.randomUUID().toString();

        // Say hello every 1 second
        // https://github.com/xebia-france/sim-service-factories/tree/master#dites-hello
        sayHello(eventBus, id);
        vertx.setPeriodic(1000, timerId -> sayHello(eventBus, id));

        // Buy hop to sale beer
        // https://github.com/xebia-france/sim-service-factories/tree/master#achetez-du-houblon
        buyHop(eventBus, id);
    }

    private void buyHop(EventBus eventBus, String id) {
        // Listen response and bank
        eventBus.registerHandler("/city/factory/" + id, message -> {
            container.logger().info("Get a message " + message.body());
            if (message.body() != null && message.body() instanceof JsonObject) {
                JsonObject offer = (JsonObject) message.body();
                String action = offer.getString("action");
                String from = offer.getString("from");
                Integer quantity = offer.getInteger("quantity", 0);
                Integer cost = offer.getInteger("cost", 0);
                // Accept offer
                switch (action) {
                    case "response":
                        container.logger().info("Get a response from farm " + from);
                        JsonObject acquittement = new JsonObject();
                        acquittement.putString("action", "acquittement");
                        acquittement.putString("from", id);
                        acquittement.putNumber("quantity", quantity);
                        message.reply(acquittement);
                        break;
                    case "purchase":
                        container.logger().info("Bank accepts a transaction");
                        // Here, you manage your stock
                        break;
                }
            }
        });

        // Publish a request
        JsonObject orderObject = new JsonObject()
                .putString("action", "request")
                .putString("from", id)
                .putNumber("quantity", 10);
        eventBus.publish("/city/farm", orderObject);
    }

    private void sayHello(EventBus eventBus, String id) {
        container.logger().info("Say hello");
        JsonObject hello = new JsonObject();
        hello.putString("action", "hello");
        hello.putString("from", id);
        hello.putString("team", "your-team-name");
        hello.putString("type", "factory");
        hello.putString("version", "1.0");
        eventBus.publish("/city", hello);
    }

}
