import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AlgorithmKtTest {
    @Test
    fun simple() {
        val graph = Graph(4)
        graph.add(Edge(0, 1, "a")) // 0 = 1
        graph.add(Edge(0, 2, "d")) // *0 = 2
        graph.add(Edge(1, 3, "d")) // *1 = 3
        cfl_reachability(c_normal_rules, graph)
        assert(graph.contains(Edge(2, 3, "M")))
        assert(graph.contains(Edge(3, 2, "M")))
        assert(graph.contains(Edge(3, 2, "V")))
        assert(graph.contains(Edge(2, 3, "V")))
        assert(graph.contains(Edge(0, 1, "V")))
        assert(graph.contains(Edge(1, 0, "V")))
        assertFalse(graph.contains(Edge(0, 1, "M")))
    }
}