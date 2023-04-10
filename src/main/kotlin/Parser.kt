import com.xenomachina.argparser.*

class Parser(parser: ArgParser) {
    val input by parser.storing(
        "-i", "--input",
        help = "input file, if not given then read from standard input").default<String>("")

    val output by parser.storing(
        "-o", "--output",
        help = "output file, if not given then write to standard output").default<String>("")

}