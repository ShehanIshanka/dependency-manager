package modules;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import startup.BootStrapper;

/**
 * Guice module for Controller injections
 */
public class ControllerModule extends AbstractModule {
    //In case you need to use any configuration values use the config object for that purpose
    private final Config config;

    /**
     * Default constructor
     *
     * @param config configuration object
     */
    public ControllerModule(Config config) {
        this.config = config;
    }

    /**
     * Configures the module
     */
    @Override
    protected void configure() {

        // This binding is to initialize app startup singleton
        bind(BootStrapper.class).asEagerSingleton();
    }
}