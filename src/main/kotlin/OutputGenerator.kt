class OutputGenerator(private var result: MutableList<MutableList<SubjectTime>>?, private var preferTime: MutableList<String>) {
    fun generate() {
        println(result)
        println(preferTime)
    }
}