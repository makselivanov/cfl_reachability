class NormalGrammatic(
    val label: String,
    val rule: Pair<String?, String?>
)

val c_normal_rules: List<NormalGrammatic> = listOf(
    NormalGrammatic("M", Pair("nd", "buf_M")), // M -> nd V d
    NormalGrammatic("buf_M", Pair("V", "d")),
    NormalGrammatic("V", Pair("nF", "buf_V")), // V -> nF M? F
    NormalGrammatic("buf_V", Pair("M", "F")),
    NormalGrammatic("buf_V", Pair("F", null)),
    NormalGrammatic("nF", Pair("buf_nF", "nF")), // nF -> (M? na)*
    NormalGrammatic("nF", Pair(null, null)),
    NormalGrammatic("buf_nF", Pair("M", "na")),
    NormalGrammatic("buf_nF", Pair("na", null)),
    NormalGrammatic("F", Pair("buf_F", "F")), // F -> (a M?)*
    NormalGrammatic("F", Pair(null, null)),
    NormalGrammatic("buf_F", Pair("a", "M")),
    NormalGrammatic("buf_F", Pair("a", null)),
)

val java_normal_rules: MutableList<NormalGrammatic> = mutableListOf(
    NormalGrammatic("alias", Pair("flowsTo", "nflowsTo")), // alias -> flowsTo nflowsTo
    NormalGrammatic("flowsTo", Pair("alloc", "buf_flows")), // flowsTo -> alloc (assign | store_id alias load_id)*
    NormalGrammatic("buf_flows", Pair(null, null)),
    NormalGrammatic("buf_flows", Pair("buf_flows", "buf_bracket_flowsTo")),
    NormalGrammatic("buf_bracket_flowsTo", Pair("assign", null)), //should add rules for every id
    NormalGrammatic("nflowsTo", Pair("buf_nflows", "nalloc")), // nflowsTo -> (nassign | nload_id alias nstore_id)* nalloc
    NormalGrammatic("buf_nflows", Pair(null, null)),
    NormalGrammatic("buf_nflows", Pair("buf_nflows", "buf_bracket_nflowsTo")),
    NormalGrammatic("buf_bracket_nflowsTo", Pair("nassign", null)), //should add rules for every id
)