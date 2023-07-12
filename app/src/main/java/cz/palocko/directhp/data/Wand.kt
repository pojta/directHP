package cz.palocko.directhp.data

data class Wand(
    val wood: String,
    val core: String,
    val length: Number?,
) {
    override fun toString(): String {
        return "Wood: $wood, Core: $core, Length: $length"
    }
}
