class Subject {
    var name: String =  ""  
    var code: String =  ""
    var required = mutableMapOf<String,Int>()
    var subjectTimes = mutableListOf<SubjectTime>()
    var chained = mutableListOf<MutableList<String>>()
    var preferred = mutableListOf<String>()

    /* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Staring and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */
    private fun combinationUtil (
        arr: Array<SubjectTime>, data: Array<SubjectTime>, start: Int,
        end: Int, index: Int, r: Int, result: MutableList<MutableList<SubjectTime>>
    ):MutableList<MutableList<SubjectTime>> {
        // Current combination is ready to be printed, print it
        if (index == r) {
            result.add(data.toMutableList())
            return result
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        var i = start
        while (i <= end && end - i + 1 >= r - index) {
            data[index] = arr[i]
            combinationUtil(arr, data, i + 1, end, index + 1, r, result)
            i++
        }
        return result
    }

    private fun combination(arr: Array<SubjectTime>, n: Int, r: Int): MutableList<MutableList<SubjectTime>> {
        return combinationUtil(arr, Array(r, init= { SubjectTime() }), 0, n - 1, 0, r, mutableListOf())
    }
    private fun combinationUtil2dList (
        arr: Array<MutableList<SubjectTime>>, data: Array<MutableList<SubjectTime>>, start: Int,
        end: Int, index: Int, r: Int, result: MutableList<MutableList<MutableList<SubjectTime>>>
    ):MutableList<MutableList<MutableList<SubjectTime>>> {
        // Current combination is ready to be printed, print it
        if (index == r) {
            result.add(data.toMutableList())
            return result
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        var i = start
        while (i <= end && end - i + 1 >= r - index) {
            data[index]=(arr[i])
            combinationUtil2dList(arr, data, i + 1, end, index + 1, r, result)
            i++
        }
        return result
    }

    private fun combination2dList(arr: Array<MutableList<SubjectTime>>, n: Int, r: Int): MutableList<MutableList<MutableList<SubjectTime>>> {
        return combinationUtil2dList(arr, Array(r, init= { mutableListOf() }), 0, n - 1, 0, r, mutableListOf())
    }

    fun combineTime() {
        val subjectTimeList = mutableListOf<MutableList<SubjectTime>>()
        val timeList = mutableListOf<MutableList<MutableList<SubjectTime>>>()
        for (mutableEntry in required) {
            val arr = subjectTimes.filter { it.group == mutableEntry.key }.toTypedArray()
            val r = mutableEntry.value
            val n = arr.size
            val a = combination(arr, n, r)
            timeList.add(a)
        }

        val arr = timeList.flatten().toTypedArray()
        val r = required.size
        val n = arr.size
        val groupedTimeList = combination2dList(arr, n, r)

        // remove clash time combination

        for(groupedTimes in groupedTimeList) {
            val groups = groupedTimes.map { it[0].group }.toMutableList()
            if(groups.distinct().size == groups.size)
                subjectTimeList.add(groupedTimes.flatten().toMutableList())
        }
        println(subjectTimeList.size)
        for(a in subjectTimeList) {
            println(a.map { it.location })
        }
        println("end")
    }
}