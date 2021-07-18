package engine;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
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
    private Map<String, String> vertexLableMap = new HashMap<>();

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
                String id = p.getFirst();
                String label = a.getValue();
                vertexAttributes.add("{\"id\":\"" + id + "\",\"label\":\"" + label + "\"}");
                vertexLableMap.put(label, id);
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

    public String buildRecoveryGraph(String dependencies) {
        logger.info("Dependencies are : {}", dependencies);

        List<String> startVertices = Arrays.stream(dependencies.split(","))
                .map(label -> vertexLableMap.get(label))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Iterator<String> iterator = new DepthFirstIterator(graph, startVertices);

        List<String> attrList = new ArrayList<>();
        while (iterator.hasNext()) {
            String node = iterator.next();
            attrList.add(vertexAttributes.stream().filter(vertex -> vertex.contains(node)).findAny().get());
        }

        String attr = "[" + StringUtils.join(attrList, ",") + "]";

        logger.info("Recovery graph building is successful.");
        return attr;
    }

}