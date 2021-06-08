import java.time.LocalTime

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

    private fun isClashTime(subjectTimes: List<SubjectTime>):Boolean {
        val timesByDay = subjectTimes.groupBy { it.day }
        for(mutableEntry in timesByDay) {
            val values = mutableEntry.value.sortedBy { it.startTime }
            var lastEndTime: LocalTime? = null
            values.forEach {
                lastEndTime = if(lastEndTime == null) {
                    if(values.size > 1 && it.endTime.isBefore(it.startTime)) return true
                    it.endTime
                } else {
                    if (it.endTime.isBefore(it.startTime)) {
                        if (!timesByDay[mutableEntry.key + 1].isNullOrEmpty()) {
                            // last class clash with next day first class
                            val nextDayFirstClass =
                                timesByDay[mutableEntry.key + 1]?.sortedBy { it1 -> it1.startTime }?.get(0)?.startTime
                            if (it.endTime.isAfter(nextDayFirstClass)) return true
                        }
                        // the class that across 2 day is not last class
                        if (it != values.last()) return true
                    }
                    // last class end time is after next class start time
                    if (lastEndTime!!.isAfter(it.startTime)) return true
                    it.endTime
                }
            }
        }
        return false
    }

    fun combineTime(): MutableList<MutableList<SubjectTime>> {
        val subjectTimeList = mutableListOf<MutableList<SubjectTime>>()
        val timeList = mutableListOf<MutableList<MutableList<SubjectTime>>>()

        //group for same group (if group b need 2, get all combination for)
        for (mutableEntry in required) {
            val arr = subjectTimes.filter { it.group == mutableEntry.key }.toTypedArray()
            val r = mutableEntry.value
            val n = arr.size
            val times = combination(arr, n, r)
            times.removeIf { isClashTime(it) }
            timeList.add(times)
        }

        val arr = timeList.flatten().toTypedArray()
        val r = required.size
        val n = arr.size
        val groupedTimeList = combination2dList(arr, n, r)

        for(groupedTimes in groupedTimeList) {
            val groups = groupedTimes.map { it[0].group }.toMutableList()
            if(groups.distinct().size == groups.size)
                if(!isClashTime(groupedTimes.flatten()))
                    subjectTimeList.add(groupedTimes.flatten().toMutableList())
        }

        return subjectTimeList
    }
}