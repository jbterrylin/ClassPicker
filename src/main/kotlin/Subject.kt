class Subject {
    var name: String =  ""  
    var code: String =  ""
    var required = mutableMapOf<String,Int>()
    var subjectTimes = mutableListOf<SubjectTime>()
    var chained = mutableListOf<MutableList<String>>()
    var preferred = mutableListOf<String>()

    // Extensions
    fun <T, S> Collection<T>.cartesianProduct(other: Iterable<S>): List<Pair<T, S>> {
        return cartesianProduct(other) { first, second -> first to second }
    }

    private fun <T, S, V> Collection<T>.cartesianProduct(other: Iterable<S>, transformer: (first: T, second: S) -> V): List<V> {
        return this.flatMap { first -> other.map { second -> transformer.invoke(first, second) } }
    }

    fun combineTwoList(list1: MutableList<SubjectTime>,list2: MutableList<SubjectTime>): MutableList<MutableList<SubjectTime>> {
//        val list1 = subjectTimes.filter { it.group == mutableEntry.key }.toMutableList()
//        val list2 = subjectTimes.filter { it.group == mutableEntry.key }.toMutableList()
        val tempTimes = mutableListOf<MutableList<SubjectTime>>()
        list1.cartesianProduct(list2) { a, b ->
            if(!(tempTimes.any { it == mutableListOf<SubjectTime>(a, b) } ||
                        tempTimes.any { it == mutableListOf<SubjectTime>(b, a) }))
                tempTimes.add(mutableListOf<SubjectTime>(a, b))
        }
        tempTimes.removeAll { it.distinct().size != it.size }
        for(tempTime in tempTimes)
            println(tempTime[0].location + " " +tempTime[1].location)
        return tempTimes
    }

    fun combineTime() {
        var timeList = mutableListOf<MutableList<MutableList<SubjectTime>>>()
        for (mutableEntry in required) {
            if(mutableEntry.value == 1) {
                timeList.add(subjectTimes.filter { it.group == mutableEntry.key }.map { mutableListOf<SubjectTime>(it) }.toMutableList())
            } else {
                var tempTimeList = mutableListOf<MutableList<MutableList<SubjectTime>>>()
                combineTwoList(
                    subjectTimes.filter { it.group == mutableEntry.key }.toMutableList(),
                    subjectTimes.filter { it.group == mutableEntry.key }.toMutableList())
            }
        }
//        val ints = listOf(0, 1, 2)
//        val strings = listOf("a", "b", "c")
//        strings.cartesianProduct(ints) { string, int ->
//            "$int $string"
//        }
//
//        // Or use more generic one
//        strings.cartesianProduct(ints)
//            .map { (string, int) ->
//                "$int $string"
//            }
//            .forEach(::println)
    }
}