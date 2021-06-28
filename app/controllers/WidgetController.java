package controllers;

import engine.JGraphTGraphReader;
import models.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Files;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.file.FileAccessException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Paths;
import java.util.List;

import static play.libs.Scala.asScala;

/**
 * An example of form processing.
 * <p>
 * https://playframework.com/documentation/latest/JavaForms
 */
@Singleton
public class WidgetController extends Controller {

    private final Form<WidgetData> form;
    private MessagesApi messagesApi;
    private final List<Widget> widgets;
    private final String edges;
    private final String attr;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public WidgetController(FormFactory formFactory, MessagesApi messagesApi) throws FileAccessException {
        this.form = formFactory.form(WidgetData.class);
        this.messagesApi = messagesApi;
        this.widgets = com.google.common.collect.Lists.newArrayList(
                new Widget("Data 1", 123),
                new Widget("Data 2", 456),
                new Widget("Data 3", 789)
        );
//        this.edges = Json.stringify(Json.toJson(new JGraphTGraphReader().getEdges()));
//        this.attr = Json.stringify(Json.toJson(new JGraphTGraphReader().getAttributes()));
        this.edges = new JGraphTGraphReader().getEdges();
        this.attr = new JGraphTGraphReader().getAttributes();
    }

    public Result index(Http.Request request) {
        System.out.println(edges);
        System.out.println(attr);
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
            widgets.add(new Widget(data.getName(), data.getPrice()));
            return redirect(routes.WidgetController.listWidgets())
                    .flashing("info", "Widget added!");
        }
    }

    public Result upload(Http.Request request) {

        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile("picture");
        if (picture != null) {
            System.out.println("aaaaaaaaa");
            String fileName = picture.getFilename();
            long fileSize = picture.getFileSize();
            String contentType = picture.getContentType();
            Files.TemporaryFile file = picture.getRef();
            file.copyTo(Paths.get("destination.jon"), true);
            return redirect(routes.WidgetController.index())
                    .flashing("info", "File added!");
        } else {
            System.out.println("bbbb");
            return badRequest().flashing("error", "Missing file");
        }
    }
}