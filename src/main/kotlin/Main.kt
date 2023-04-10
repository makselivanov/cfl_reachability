import com.xenomachina.argparser.ArgParser
import java.io.File
import kotlin.math.max


fun main(args: Array<String>) {
    ArgParser(args).parseInto(::Parser).run {
        assert(input.isNotEmpty())
        assert(output.isNotEmpty())
        var is_c_grammer = false

        val edges = Edges()
        var size = 0

        File(input).forEachLine {
            val (left_name, right_name, label) = it.split(" ")
            val left_id = left_name.toInt()
            val right_id = right_name.toInt()
            if (label == "a" || label == "d") {
                is_c_grammer = true
            }
            val edge = Edge(left_id, right_id, label)
            edges.add(edge)
            size = max(size, left_id + 1)
            size = max(size, right_id + 1)
        }
        val graph = Graph(size)
        val store_load_ids: MutableSet<Int> = mutableSetOf()

        edges.edges.forEach {edge ->
            graph.add(edge) //will add reverse edge automaticly
            if (!is_c_grammer && edge.label.startsWith("store_")) {
                store_load_ids.add(edge.label.substring("store_".length).toInt())
            }
            if (!is_c_grammer && edge.label.startsWith("load_")) {
                store_load_ids.add(edge.label.substring("load_".length).toInt())
            }
        }
        if (!is_c_grammer) {
            store_load_ids.forEach { id ->
                java_normal_rules.addAll( arrayOf(
                    NormalGrammatic("bracket_flowsTo", Pair("store_$id", "buffer_$id")),
                    NormalGrammatic("buffer_$id", Pair("alias", "load_$id")),
                    NormalGrammatic("bracket_nflowsTo", Pair("nload_$id", "nbuffer_$id")),
                    NormalGrammatic("nbuffer_$id", Pair("alias", "nstore_$id"))
                ))
            }
        }
        val cur_grammatic = if (is_c_grammer) c_normal_rules else java_normal_rules
        val writer = File(output).bufferedWriter()
        val new_graph = cfl_reachability(cur_grammatic, graph)

        val main_labels = arrayOf(
            "M", "V", "alias", "flowsTo", "nflowsTo"
        )
        val counter = main_labels.map { it -> Pair(it, 0) }.toMap().toMutableMap()

        new_graph.edges.edges.filter {
            main_labels.contains(it.label) && it.from != it.to
        }.forEach {
            writer.write("${it.from} ${it.to} ${it.label}\n")
            val cur_count = counter.getOrDefault(it.label, 0)
            counter[it.label] = cur_count + 1
        }
        writer.flush()

        counter.filter {
            it.value != 0
        }.forEach {
            println("${it.key} have ${it.value} oriented edges")
        }
    }
}