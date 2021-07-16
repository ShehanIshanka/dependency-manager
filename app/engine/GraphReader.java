package engine;

/**
 * Interface for Graph reader
 */
public interface GraphReader {

    /**
     * Generate graph from file
     */
    void readGraphFromFile();

    /**
     * Get Edges of the graph
     *
     * @return Edge string
     */
    String getEdges();

    /**
     * Get graph attributes
     *
     * @return Attribute string
     */
    String getAttributes();
}