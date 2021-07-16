package engine;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.json.JSONImporter;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.util.SupplierUtil;
import play.Logger;
import utils.file.FileAccessException;
import utils.file.FileUtilities;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JGraphT Implementation of Graph reader
 */
@Singleton
public class JGraphTGraphReader implements GraphReader {

    @Inject
    private static Logger.ALogger logger;

    private Graph<String, GraphEdge> graph = null;
    private List<String> vertexAttributes = new ArrayList<>();

    private final String dependencyGraphFile;

    @Inject
    public JGraphTGraphReader(@Named("dependencyGraphFile") final String dependencyGraphFile) {
        this.dependencyGraphFile = dependencyGraphFile;
    }

    /**
     * Generate graph from file
     */
    @Override
    public void readGraphFromFile() {
        String input = null;
        try {
            input = FileUtilities.readFileString(dependencyGraphFile);

            graph = GraphTypeBuilder
                    .directed().allowingMultipleEdges(true).allowingSelfLoops(true)
                    .vertexSupplier(SupplierUtil.createStringSupplier(1))
                    .edgeSupplier(SupplierUtil.createSupplier(GraphEdge.class)).buildGraph();
            JSONImporter<String, GraphEdge> importer = new JSONImporter<>();
            logger.info("JGraph Initialization is successful.");

            vertexAttributes.clear();
            importer.addVertexAttributeConsumer((p, a) -> {
                vertexAttributes.add("{\"id\":\"" + p.getFirst() + "\",\"label\":\"" + a.getValue() + "\"}");
            });
            importer.importGraph(graph, new StringReader(input));

        } catch (FileAccessException e) {
            logger.error("An exception occurred when reading file", e);
        }

    }

    /**
     * Get Edges of the graph
     *
     * @return Edge string
     */
    @Override
    public String getEdges() {
        String edges = "[" + graph.edgeSet().stream().map(GraphEdge::toString).collect(Collectors.joining(", ")) + "]";
        logger.info("Edge returning is successful.");
        return edges;
    }

    /**
     * Get graph attributes
     *
     * @return Attribute string
     */
    @Override
    public String getAttributes() {
        String attr = "[" + vertexAttributes.stream().collect(Collectors.joining(", ")) + "]";
        logger.info("Attribute returning is successful.");
        return attr;
    }

    public String buildGraphFromDependencies(String startNodes) {
        logger.info(StringUtils.join(graph.vertexSet()));

        startNodes = "2,6,4";
        List<String> startNodeList = Arrays.asList(startNodes.split(","));

//        vertexAttributes.stream().filter(uri -> startNodeList.contains(node))

        Iterator<String> iterator = new DepthFirstIterator(graph, startNodeList);

        List<String> attr = new ArrayList<>();
        while (iterator.hasNext()) {
            String node = iterator.next();
            attr.add(vertexAttributes.stream().filter(uri -> uri.contains(node)).findAny()
                    .get());
        }
        logger.info("[" + StringUtils.join(attr, ",") + "]");
        return "[" + StringUtils.join(attr, ",") + "]";
    }

}