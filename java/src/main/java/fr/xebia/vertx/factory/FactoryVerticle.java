package fr.xebia.vertx.factory;

import java.util.UUID;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * 
 * Stub for a factory service using vert.x application plateform.
 *
 * @author xebia
 */
public class FactoryVerticle extends Verticle {

    /*
     * the inner event bus provided by vert.x
     */
    private EventBus eventBus;
    /*
     * your configuration loaded by vert.x
     */
    private JsonObject config;
    /*
     * your team name.
     */
    private String teamName;
    /*
     * the id of this Verticle instance
     */
    private String idFactory;
    /*
     * the quantity of stock you got.
     */
    private long currentStock;


    @Override
    public void start() {
        /*
         * Everything here is executed by the event loop. Any blocking call must be avoided if your Verticle is not
         * a worker one (and it has not to be for the exercice !)
         */
        eventBus = vertx.eventBus();
        config = container.config();
        teamName = "vicTeam";// todo => change this name right now... This one is not exactly glorious ^-^
        idFactory = "store-" + UUID.randomUUID().toString();

        startPeriodicHello();
        startListeningStoreOrders();
        startListeningOnPrivateFactoryChannel();

    }

    private void startPeriodicHello() {
        vertx.setPeriodic(5000, longId -> {
            //todo => send a hello message to everybody
        });
    }

    private void startListeningStoreOrders() {
        // todo => listening for orders coming from store.
        // Remember : You must have enough stock to reply to an order
        // if you try to cheat, the bank will penalize you.
    }

    private void startListeningOnPrivateFactoryChannel() {
        // todo => listening for messages sended one /city/factory/${idFactory}
        // Remember : Such message may come from the bank or a farm
    }

}
