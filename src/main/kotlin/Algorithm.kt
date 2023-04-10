import org.slf4j.LoggerFactory

fun cfl_reachability(
    grammatic: List<NormalGrammatic>,
    graph: Graph
) : Graph {
    grammatic.filter { //adds empty rules
        it.rule == Pair(null, null)
    }.forEach {
        for (id in 0 until graph.size) {
            val edge = Edge(id, id, it.label)
            graph.add(edge)
        }
    }
    val logger = LoggerFactory.getLogger(Edge::class.java)

    val working_edges = Edges(graph.edges.edges.toMutableList()) //fully copy

    while (working_edges.edges.isNotEmpty()) {
        val edge = working_edges.get_and_remove() //(from, to) with B rule
        logger.debug("${edge.from}, ${edge.to}, ${edge.label} <-- B rule")
        logger.debug("Current working size: ${working_edges.edges.size}")

        grammatic.filter { // A := B rule
            it.rule == Pair(edge.label, null)
        }.forEach {
            val new_edge = Edge(edge.from, edge.to, it.label)
            if (!graph.contains(new_edge)) { // adding A rule
                logger.debug("adding ${new_edge.from}, ${new_edge.to}, ${new_edge.label} with A := B rule")
                graph.add(new_edge)
                working_edges.add(new_edge)
            }
        }

        grammatic.filter {
            it.rule.first == edge.label && it.rule.second != null // A := B C
        }.forEach { cur_grammatic -> //choose C rule
            graph.graph[edge.to].edges.filter { it ->
                it.label == cur_grammatic.rule.second // only edges with C rule
            }.forEach {
                val new_edge = Edge(edge.from, it.to, cur_grammatic.label)
                if (!graph.contains(new_edge)) { // adding A rule
                    logger.debug("adding edge (${new_edge.from}, ${new_edge.to}, ${new_edge.label}) with A := B C rule" +
                            " | edge with C rule: ${it.from}, ${it.to}, ${it.label}")
                    graph.add(new_edge)
                    working_edges.add(new_edge)
                }
            }
        }

        grammatic.filter {
            it.rule.second == edge.label // A := C B
        }.forEach { cur_grammatic -> //choose C rule
            graph.rev_graph[edge.from].edges.filter { it ->
                it.label == cur_grammatic.rule.first // only edges with C rule
            }.forEach {
                val new_edge = Edge(it.from, edge.to, cur_grammatic.label)
                if (!graph.contains(new_edge)) { // adding A rule
                    logger.debug("adding edge (${new_edge.from}, ${new_edge.to}, ${new_edge.label}) with A := C B rule" +
                            " | edge with C rule: ${it.from}, ${it.to}, ${it.label}")
                    graph.add(new_edge)
                    working_edges.add(new_edge)
                }
            }
        }
    }
    return graph
}