import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.platform.Verticle;

import java.util.UUID;

/**
 * Created by Xebia on 12/10/2014.
 */
public class Factory extends Verticle {

    public void start() {
        EventBus eventBus = vertx.eventBus();

        String id = "factory-" + UUID.randomUUID().toString();
    }
}
