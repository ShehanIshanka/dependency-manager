package startup;

import com.google.inject.Inject;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.notFound;

/**
 * Custom HTTP error handler for Service
 */
public class HttpErrorHandler implements play.http.HttpErrorHandler {

    @Inject
    private static Logger.ALogger logger;

    /**
     * Overrided client error handler
     *
     * @param request    headers that came in the request
     * @param statusCode HTTP status code for the request
     * @param message    error message
     * @return client error promise
     */
    @Override
    public CompletionStage<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        String corrId = request.header("X-CORRELATION-ID").orElse("");
        if (statusCode == Http.Status.NOT_FOUND) {
            logger.error("[CORRID={}] Client requested for an unavailable PATH={}", corrId, request.path());
            return CompletableFuture.completedFuture(
                    notFound("")
            );
        } else {
            logger.error("[CORRID={}] Unknown client error with error message: {}", corrId, message);
            return CompletableFuture.completedFuture(
                    Results.status(statusCode, "")
            );
        }
    }

    /**
     * Overrided server error handler
     *
     * @param request   headers that came in the request
     * @param exception server side exception
     * @return server error promise
     */
    @Override
    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
        String corrId = request.header("X-CORRELATION-ID").orElse("");
        logger.error("[CORRID={}] Unhandled server error occurred", corrId, exception);
        return CompletableFuture.completedFuture(
                Results.internalServerError("")
        );
    }
}
