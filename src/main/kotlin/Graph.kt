data class Edge(
    val from: Int,
    val to: Int,
    val label: String
)

class Edges (
    val edges: MutableList<Edge> = mutableListOf()
) {
    fun add(edge: Edge) {
        edges.add(edge)
    }
    fun get_and_remove(): Edge {
        return edges.removeFirst()
    }
}
class Graph(
    val size: Int
) {
    val graph: Array<Edges> = Array(size) { Edges() }
    val rev_graph: Array<Edges> = Array(size) { Edges() }
    val edges: Edges = Edges()
    fun add(edge: Edge) {
        if (edge.from >= size || edge.from < 0 || edge.to >= size || edge.to < 0) {
            error("edge from or to is not from [0, size) interval")
        }
        graph[edge.from].add(edge)
        val rev_edge = Edge(edge.to, edge.from, "n${edge.label}")
        graph[edge.to].add(rev_edge)
        rev_graph[edge.to].add(edge)
        rev_graph[edge.from].add(rev_edge)
        edges.add(edge)
        edges.add(rev_edge)
    }

    fun contains(edge: Edge): Boolean {
        return graph[edge.from].edges.contains(edge)
    }
}