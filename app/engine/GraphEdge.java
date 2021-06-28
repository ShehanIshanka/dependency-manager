package engine;

import org.jgrapht.graph.DefaultEdge;

/**
 * Custom implementation for edges
 */
public class GraphEdge
        extends
        DefaultEdge {
    private static final long serialVersionUID = 3258408452177932855L;

    /**
     * Retrieves the source of this edge. This is protected, for use by subclasses only (e.g. for
     * implementing toString).
     *
     * @return source of this edge
     */
    protected Object getEdgeSource() {
        return getSource();
    }

    /**
     * Retrieves the target of this edge. This is protected, for use by subclasses only (e.g. for
     * implementing toString).
     *
     * @return target of this edge
     */
    protected Object getEdgeTarget() {
        return getTarget();
    }

    @Override
    public String toString() {
        return String.format("{\"from\":\"%s\",\"to\":\"%s\"}", getSource(), getTarget());
    }
}