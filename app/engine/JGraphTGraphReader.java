package engine;

import com.google.inject.Singleton;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.json.JSONImporter;
import org.jgrapht.util.SupplierUtil;
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

    //    @Inject
//    JSONImporter<String, DefaultEdge> importer;
    Graph<String, GraphEdge> graph = null;
    List<String> vertexAttributes = null;

    public JGraphTGraphReader() throws FileAccessException {
        String input = FileUtilities.readFileString("dependency_graph.json");

        graph = GraphTypeBuilder
                .directed().allowingMultipleEdges(true).allowingSelfLoops(true)
                .vertexSupplier(SupplierUtil.createStringSupplier(1))
                .edgeSupplier(SupplierUtil.createSupplier(GraphEdge.class)).buildGraph();

        JSONImporter<String, GraphEdge> importer = new JSONImporter<>();

        vertexAttributes = new ArrayList<String>();
        importer.addVertexAttributeConsumer((p, a) -> {
            vertexAttributes.add("{\"id\":\"" + p.getFirst() + "\",\"label\":\"" + a.getValue() + "\"}");
        });

        importer.importGraph(graph, new StringReader(input));
    }

    public String getEdges() {
        String edges = "[" + graph.edgeSet().stream().map(GraphEdge::toString).collect(Collectors.joining(", ")) + "]";
        return edges;
    }

    public String getAttributes() {
        String attr = "[" + vertexAttributes.stream().collect(Collectors.joining(", ")) + "]";
        return attr;
    }


    void readGraph() throws FileAccessException {
        String input = FileUtilities.readFileString("dependency_graph.json");

        Graph<String, DefaultEdge> g = GraphTypeBuilder
                .directed().allowingMultipleEdges(true).allowingSelfLoops(true)
                .vertexSupplier(SupplierUtil.createStringSupplier(1))
                .edgeSupplier(SupplierUtil.DEFAULT_EDGE_SUPPLIER).buildGraph();

        JSONImporter<String, DefaultEdge> importer = new JSONImporter<>();

        Map<String, Map<String, Attribute>> vertexAttributes = new HashMap<>();
        importer.addVertexAttributeConsumer((p, a) -> {
            Map<String, Attribute> attrs = vertexAttributes.get(p.getFirst());
            if (attrs == null) {
                attrs = new HashMap<>();
                vertexAttributes.put(p.getFirst(), attrs);
            }
            attrs.put(p.getSecond(), a);
        });

        importer.importGraph(g, new StringReader(input));

        System.out.println(g.vertexSet());

    }

//    public static void main(String[] args) throws FileAccessException {
//        String input = FileUtilities.readFileString("dependency_graph.json");
//
//        Graph<String, GraphEdge> g = GraphTypeBuilder
//                .directed().allowingMultipleEdges(true).allowingSelfLoops(true)
//                .vertexSupplier(SupplierUtil.createStringSupplier(1))
//                .edgeSupplier(SupplierUtil.createSupplier(GraphEdge.class)).buildGraph();
//
//        JSONImporter<String, GraphEdge> importer = new JSONImporter<>();
//
//        Map<String, String> vertexAttributes = new HashMap<>();
//        importer.addVertexAttributeConsumer((p, a) -> {
//            vertexAttributes.put(p.getFirst(), a.getValue());
//        });
//
//        importer.importGraph(g, new StringReader(input));
//
//        System.out.println(g.edgeSet());
//        System.out.println(vertexAttributes.toString());
//
//        Map<String, String> edges = new HashMap<>();
//        g.edgeSet().stream().forEach(a->edges.put(a.getEdgeSource().toString(),a.getEdgeTarget().toString()));
//        System.out.println(edges.toString());
////        for (String edge : g. ()){
////            String v1 = g.getEdgeSource(edge);
////            String v2 = g.getEdgeTarget(edge);
////            if (edge.getLabel().equals("enemy")) {
////                System.out.printf(v1 + " is an enemy of " + v2 + "\n");
////            } else if (edge.getLabel().equals("friend")) {
////                System.out.printf(v1 + " is a friend of " + v2 + "\n");
////            }
////        }
//
//        System.out.println(g.toString());
//    }

}