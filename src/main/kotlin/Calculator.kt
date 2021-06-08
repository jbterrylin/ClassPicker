import java.time.LocalTime

class Calculator(private var subjects: MutableList<Subject>, private var  preferTime: MutableList<String>,
                 private var mustTakeSubjectCodes: List<String>, private var optionalNeeded: Int) {
    fun generateSchedule() {
        println(preferTime)
//        var finalSubjectTimeList: MutableList<MutableList<SubjectTime>>? = null
        var mustSubjectTimeList: MutableList<MutableList<SubjectTime>>? = null
        subjects.filter { mustTakeSubjectCodes.contains(it.code) }.forEachIndexed { index, subject ->
            val subjectTimes = combineTime(subject)
            mustSubjectTimeList = mustSubjectTimeList?.let { combineSubjectTimeList(it, subjectTimes, index+1) } ?: subjectTimes
        }
        if(optionalNeeded != 0){
            println("continue")
        }
    }

    private fun combinationUtil (
        arr: Array<SubjectTime>, data: Array<SubjectTime>, start: Int,
        end: Int, index: Int, r: Int, result: MutableList<MutableList<SubjectTime>>
    ):MutableList<MutableList<SubjectTime>> {

        if (index == r) {
            result.add(data.toMutableList())
            return result
        }

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

        if (index == r) {
            result.add(data.toMutableList())
            return result
        }

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

    private fun combineTime(subject: Subject): MutableList<MutableList<SubjectTime>> {
        val subjectTimeList = mutableListOf<MutableList<SubjectTime>>()
        val timeList = mutableListOf<MutableList<MutableList<SubjectTime>>>()

        //group for same group (if group b need 2, get all combination for)
        for (mutableEntry in subject.required) {
            val arr = subject.subjectTimes.filter { it.group == mutableEntry.key }.toTypedArray()
            val r = mutableEntry.value
            val n = arr.size
            val times = combination(arr, n, r)
            times.removeIf { isClashTime(it) }
            timeList.add(times)
        }

        val arr = timeList.flatten().toTypedArray()
        val r = subject.required.size
        val n = arr.size
        val groupedTimeList = combination2dList(arr, n, r)

        for(groupedTimes in groupedTimeList) {
            val groups = groupedTimes.map { it[0].group }.toMutableList()
            if(groups.distinct().size == groups.size)
                if(!isClashTime(groupedTimes.flatten()))
                    subjectTimeList.add(groupedTimes.flatten().toMutableList())
        }

        // remove have chain but don't have other chain member
        for(subjectTimes in subjectTimeList.toMutableList()) {
            val codes = subjectTimes.map { it.code }
            for (chainedCodes in subject.chained) {
                if(codes.contains(chainedCodes[0]))
                    if(codes.filter {chainedCodes.contains(it) }.size != chainedCodes.size) {
                        subjectTimeList.remove(subjectTimes)
                    }
            }
        }

        return subjectTimeList
    }

    private fun combineSubjectTimeList(
        allSubjectTimeList: MutableList<MutableList<SubjectTime>>,
        subjectTimeList: MutableList<MutableList<SubjectTime>>,
        totalSubject: Int
    ): MutableList<MutableList<SubjectTime>> {
        val arr = (allSubjectTimeList+subjectTimeList).toTypedArray()
        val r = 2
        val n = arr.size
        val groupedTimeList = combination2dList(arr, n, r)

        subjectTimeList.clear()
        for(groupedTimes in groupedTimeList) {
            val groups = groupedTimes.flatten().map { it.subjectCode }.distinct().toMutableList()
            if(groups.distinct().size == totalSubject)
                if (!isClashTime(groupedTimes.flatten()))
                    subjectTimeList.add(groupedTimes.flatten().toMutableList())
        }

        return subjectTimeList
    }
}