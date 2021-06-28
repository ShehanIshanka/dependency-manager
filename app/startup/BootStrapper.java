package startup;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import play.inject.ApplicationLifecycle;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Service bootstrap class which provides the application startup functionality
 */
@Singleton
public class BootStrapper {

    // Configuration object
    private final Config config;

    /**
     * Constructor of the bootstrapper class
     *
     * @param applicationLifecycle application life cycle instance
     */
    @Inject
    public BootStrapper(ApplicationLifecycle applicationLifecycle) throws IOException, InterruptedException {
        /*
            Load the application config manually as in this stage of application cannot reference the application.conf
            via the Play application
         */
        this.config = ConfigFactory.load();

        applicationLifecycle.addStopHook(() -> {

            return CompletableFuture.completedFuture(null);
        });
    }
}