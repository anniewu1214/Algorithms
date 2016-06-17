package graph.minimun_spanning_tree;

/**
 * The <tt>Edge</tt> class represents a weighted edge in an
 * {@link EdgeWeightedGraph}. Each edge consists of two integers
 * (naming the two vertices) and a real-value weight. The data type
 * provides methods for accessing the two endpoints of the edge and
 * the weight. The natural order for this data type is by ascending
 * order of weight.
 */
public class Edge implements Comparable<Edge> {
    private int v;
    private int w;
    private double weight;

    // Initializes an edge between vertices v and w of the given weight
    public Edge(int v, int w, double weight) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex name must be a non-negative integer");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex name must be a non-negative integer");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    // Returns the weight of the edge.
    public double weight() {
        return weight;
    }

    // Returns either endpoint of the edge.
    public int either() {
        return v;
    }

    // Returns the endpoint of the edge that is different from the given vertex
    // (unless the edge represents a self-loop in which case it returns the same vertex)
    public int other(int vertex) {
        if (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    // Compares two edges by weight.
    @Override
    public int compareTo(Edge that) {
        if (this.weight() < that.weight()) return -1;
        else if (this.weight() > that.weight()) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.2f", v, w, weight);
    }

    public static void main(String[] args) {
        Edge edge = new Edge(12, 23, 3.14);
        System.out.println(edge);
    }
}