package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import controllers.WidgetController;
import engine.JGraphTGraphReader;
import play.Logger;
import startup.HttpErrorHandler;

/**
 * Guice module for logger injections
 */
public class LoggerModule extends AbstractModule {

    /**
     * Configures the module
     */
    @Override
    protected void configure() {
        requestStaticInjection(JGraphTGraphReader.class);
        requestStaticInjection(WidgetController.class);
        requestStaticInjection(HttpErrorHandler.class);
    }

    /**
     * Provides the application logger
     *
     * @return the application logger
     */
    @Provides
    Logger.ALogger provideLogger() {
        return Logger.of("application");
    }
}