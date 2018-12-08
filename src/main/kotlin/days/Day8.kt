package days

class Day8 : Day(8) {

    private val root = parseTree(inputString)

    override fun partOne(): Any {
        return metadataSum(root)
    }

    override fun partTwo(): Any {
        return nodeValue(root)
    }

    data class AocNode(val header: Pair<Int, Int>, val childNodes: List<AocNode>, val metadata: List<Int>)

    companion object {

        fun parseTree(input: String): AocNode {
            return parseTree(input.split(" ").map { it.trim().toInt() }.iterator())
        }

        private fun parseTree(input: Iterator<Int>): AocNode {
            val header = input.next() to input.next()
            val children = (0 until header.first).map { parseTree(input) }
            val metadata = (0 until header.second).map { input.next() }
            return AocNode(header, children, metadata)
        }

        fun metadataSum(node: AocNode): Int {
            return node.metadata.sum() + node.childNodes.map { metadataSum(it) }.sum()
        }

        fun nodeValue(node: AocNode): Int {
            return if (node.childNodes.isEmpty()) {
                node.metadata.sum()
            }
            else {
                node.metadata.filterNot { it == 0 }.map {
                    val childNodeIndex = it - 1
                    if (childNodeIndex <= node.childNodes.lastIndex) nodeValue(node.childNodes[it - 1]) else 0
                }.sum()
            }
        }
    }

}
