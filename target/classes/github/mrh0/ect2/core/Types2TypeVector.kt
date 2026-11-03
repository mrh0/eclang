package github.mrh0.ect2.core

open class Types2TypeVector(vector: Types2Type) : Types2Type("core", getVectorId(vector), Types2Any) {
    companion object {
        val ALL_VECTORS: HashMap<String, Types2TypeVector> = hashMapOf()
        fun getVectorId(vector: Types2Type) = "Vector<$vector>"
        fun of(vector: Types2Type): Types2TypeVector = ALL_VECTORS.getOrPut(getVectorId(vector)) { Types2TypeVector(vector) }
    }
}