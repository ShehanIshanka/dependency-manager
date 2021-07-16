package modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import engine.GraphReader;
import engine.JGraphTGraphReader;

/**
 * Guice module for Common injections
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
     */
    @Override
    protected void configure() {
        // Named constants
        bindConstant().annotatedWith(Names.named("dependencyGraphFile")).to(config.getString("dependency.graph.file"));

        bind(GraphReader.class).to(JGraphTGraphReader.class);

//        bind(new TypeLiteral<BaseConnectionManager<Connection>>() {
//        }).to(HBaseConnectionManager.class);
//        bind(new TypeLiteral<BaseConnection<Result>>() {
//        }).to(HBaseConnection.class);
    }
}