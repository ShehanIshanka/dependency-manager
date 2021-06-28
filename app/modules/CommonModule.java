package modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.typesafe.config.Config;

/**
 * Guice module for Common injections
 *
 */
public class CommonModule extends AbstractModule {
    //In case you need to use any configuration values use the config object for that purpose
    private final Config config;

    /**
     * Default constructor
     *
     * @param config configuration object
     */
    public CommonModule(Config config) {
        this.config = config;
    }

    /**
     * Configures the module
     *
     */
//    @Override
//    protected void configure() {
//        install(new FactoryModuleBuilder().build(ExecutionStatusTrackerFactory.class));
//    }
}