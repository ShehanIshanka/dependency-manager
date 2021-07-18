package controllers;

import com.google.inject.Inject;
import engine.JGraphTGraphReader;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Singleton;
import java.nio.file.Paths;

/**
 * Dependency Controller
 */
@Singleton
public class DependencyController extends Controller {

    private JGraphTGraphReader graphReader;
    private final Form<DependencyData> form;
    private MessagesApi messagesApi;
    private String edges = null;
    private String attr = null;

    @Inject
    private static Logger.ALogger logger;

    @Inject
    public DependencyController(FormFactory formFactory, MessagesApi messagesApi, JGraphTGraphReader graphReader) {
        this.graphReader = graphReader;
        this.form = formFactory.form(DependencyData.class);
        this.messagesApi = messagesApi;
        buildGraph();
    }

    public Result index(Http.Request request) {
        return ok(views.html.index.render(edges, attr, form, request, messagesApi.preferred(request)));
    }

    public Result reset() {
        buildGraph();
        return redirect(routes.DependencyController.index());
    }

    public Result buildRecoveryGraph(Http.Request request) {
        final Form<DependencyData> boundForm = form.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return redirect(routes.DependencyController.index()).flashing("error", "Recovery Graph cannot be generated.");
        } else {
            DependencyData data = boundForm.get();
            this.edges = graphReader.getEdges();
            this.attr = graphReader.buildRecoveryGraph(data.getDependencies());
            return redirect(routes.DependencyController.index()).flashing("info", "Recovery Graph is built.");
        }
    }

    public Result upload(Http.Request request) {

        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> dependencyGraphFile = body.getFile("conf/dependency_graph");
        if (dependencyGraphFile != null) {
            Files.TemporaryFile file = dependencyGraphFile.getRef();
            file.copyTo(Paths.get("dependency_graph.json"), true);
            logger.info("File uploading is successful.");
            return redirect(routes.DependencyController.index()).flashing("info", "File is added!");
        } else {
            logger.error("File is missing.");
            return redirect(routes.DependencyController.index()).flashing("error", "File is missing.");
        }
    }

    private void buildGraph() {
        graphReader.readGraphFromFile();
        this.edges = graphReader.getEdges();
        this.attr = graphReader.getAttributes();
    }
}