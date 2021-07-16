package startup;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import modules.CommonModule;
import modules.ControllerModule;
import modules.LoggerModule;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;

/**
 * Extended Guice Application Loader for providing custom Guice bindings that are
 * required by Service application
 */
public class ApplicationLoader extends GuiceApplicationLoader {
    //Configuration object
    private final Config config;

    /**
     * Default constructor
     */
    public ApplicationLoader() {
        this.config = ConfigFactory.load();
    }

    /**
     * Overrided builder method
     *
     * @param context application's default context
     * @return redefined initial Guice builder
     */
    @Override
    public GuiceApplicationBuilder builder(Context context) {
        return initialBuilder
                .in(context.environment())
                .loadConfig(config)
                .bindings(new LoggerModule())
                .bindings(new CommonModule(config))
                .bindings(new ControllerModule(config));
    }

}