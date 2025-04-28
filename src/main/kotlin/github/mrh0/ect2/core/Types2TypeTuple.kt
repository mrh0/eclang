package github.mrh0.ect2.core

class Types2TypeTuple private constructor(tuple: Array<Types2Type>) : Types2Type("core", getTupleId(tuple), Types2Any) {
    companion object {
        val ALL_TUPLES: HashMap<String, Types2TypeTuple> = hashMapOf()
        fun getTupleId(union: Array<Types2Type>) = "Tuple<${union.toSortedSet(compareBy { it.uniqueName }).joinToString(",") { it.uniqueName }}>"
        fun of(tuple: Array<Types2Type>): Types2TypeTuple = ALL_TUPLES.getOrPut(getTupleId(tuple)) { Types2TypeTuple(tuple) }
    }
}