package controllers;

import com.google.inject.Inject;
import engine.GraphReader;
import engine.JGraphTGraphReader;
import models.Widget;
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
import java.util.List;

import static play.libs.Scala.asScala;

//import javax.inject.Inject;

/**
 * An example of form processing.
 * <p>
 * https://playframework.com/documentation/latest/JavaForms
 */
@Singleton
public class WidgetController extends Controller {

    //    @Inject
    private JGraphTGraphReader graphReader;
//    private GraphReader graphReader = new JGraphTGraphReader("dependency_graph.json");

    private final Form<WidgetData> form;
    private MessagesApi messagesApi;
    private final List<Widget> widgets;

    @Inject
    private static Logger.ALogger logger;

    //    @javax.inject.Inject
    @Inject
    public WidgetController(FormFactory formFactory, MessagesApi messagesApi, JGraphTGraphReader graphReader) {
        this.graphReader = graphReader;
        this.form = formFactory.form(WidgetData.class);
        this.messagesApi = messagesApi;
        this.widgets = com.google.common.collect.Lists.newArrayList(
                new Widget("Data 1"),
                new Widget("Data 2"),
                new Widget("Data 3")
        );

    }

    public Result index(Http.Request request) {
        graphReader.readGraphFromFile();
        String edges = graphReader.getEdges();
        String attr = graphReader.getAttributes();
        return ok(views.html.index.render(edges, attr, asScala(widgets), form, request, messagesApi.preferred(request)));
    }

    public Result listWidgets(Http.Request request) {
        return ok(views.html.listWidgets.render(asScala(widgets), form, request, messagesApi.preferred(request)));
    }

    public Result createWidget(Http.Request request) {
        final Form<WidgetData> boundForm = form.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return badRequest(views.html.listWidgets.render(asScala(widgets), boundForm, request, messagesApi.preferred(request)));
        } else {
            WidgetData data = boundForm.get();
            widgets.add(new Widget(data.getName()));
            return redirect(routes.WidgetController.listWidgets())
                    .flashing("info", "Widget added!");
        }
    }

    public Result buildDependencyGraph(Http.Request request) {
        final Form<WidgetData> boundForm = form.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return redirect(routes.WidgetController.index()).flashing("error", "File is missing.");
        } else {
            WidgetData data = boundForm.get();
            widgets.add(new Widget(data.getName()));

            String edges = graphReader.getEdges();
//            String attr = graphReader.getAttributes();
            String attr = graphReader.buildGraphFromDependencies(data.getName());
            logger.info(edges);
            return ok(views.html.index.render(edges, attr, asScala(widgets), form, request, messagesApi.preferred(request)));
        }
    }

    public Result upload(Http.Request request) {

        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> dependencyGraphFile = body.getFile("dependency_graph");
        if (dependencyGraphFile != null) {
            Files.TemporaryFile file = dependencyGraphFile.getRef();
            file.copyTo(Paths.get("dependency_graph.json"), true);
            logger.info("File uploading is successful.");
            return redirect(routes.WidgetController.index())
                    .flashing("info", "File is added!");
        } else {
            logger.error("File is missing.");
            return redirect(routes.WidgetController.index()).flashing("error", "File is missing.");
        }
    }
}