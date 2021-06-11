import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalTime
import java.time.temporal.ChronoUnit


class OutputGenerator(private var subjects: MutableList<Subject>, private var timeList: MutableList<MutableList<SubjectTime>>?) {
    private val folderPath = "src/main/resources"
    private val folderName = "output"
    private val startTime = mutableMapOf<Int, MutableList<LocalTime>>()
    private val endTime = mutableMapOf<Int, MutableList<LocalTime>>()
    private val nOfClass = mutableMapOf<Int, MutableList<Int>>()
    private var mostShortDay = Int.MAX_VALUE

    fun preferSettingUp() {
        for(times in timeList?.withIndex()!!) {
            val timeMap = times.value.groupBy { it.day }
            if(timeMap.size < mostShortDay)
                mostShortDay = timeMap.size

            timeMap.forEach{
                if(startTime[it.key] == null)
                    startTime[it.key] = mutableListOf()
                if(endTime[it.key] == null)
                    endTime[it.key] = mutableListOf()
                if(nOfClass[it.key] == null)
                    nOfClass[it.key] = mutableListOf()

                startTime[it.key]!!.add(it.value[0].startTime)
                endTime[it.key]!!.add(it.value.last().endTime)
                nOfClass[it.key]!!.add(it.value.size)
            }

            if(times.index % 30 == 0) {
                for(st in startTime)
                    startTime[st.key] = st.value.distinct().toMutableList()
                for(st in endTime)
                    endTime[st.key] = st.value.distinct().toMutableList()
                for(st in nOfClass)
                    nOfClass[st.key] = st.value.distinct().toMutableList()
            }
        }
        for(st in startTime)
            startTime[st.key] = st.value.distinct().sorted().toMutableList()
        for(st in endTime)
            endTime[st.key] = st.value.distinct().sortedDescending().toMutableList()
        for(st in nOfClass)
            nOfClass[st.key] = st.value.distinct().toMutableList()
    }

    fun generateMenu() {
        var fileWriter: FileWriter? = null

        try {
            fileWriter = FileWriter("$folderPath/$folderName/menu.csv")

            fileWriter.append("Note: \n")
            fileWriter.append("\tstart time and end time column is according index(dunno can ask it friend)\n")
            fileWriter.append("\tList inside column is sort according day\n")
            fileWriter.append("\tstudy percentage high mean u don't have many rest time when study\n")
            fileWriter.append("\t\te.x: if u study straight 10 hour without rest then study percentage is 100\n")
            fileWriter.append("\t\te.x: if u study straight 8 hour with 2 hour rest then study percentage is 80\n")

            fileWriter.append("start time: \n")
                for(time in startTime) {
                    fileWriter.append("\""+time.key.toString() + "-" + time.value.toString() + "\"")
                    fileWriter.append("\n")
                }
            fileWriter.append("end time: \n")
            for(time in endTime) {
                fileWriter.append("\""+time.key.toString() + "-" + time.value.toString() + "\"")
                fileWriter.append("\n")
            }
            fileWriter.append("n Of Class: \n")
            for(n in nOfClass) {
                fileWriter.append("\""+n.key.toString() + "-" + n.value.toString() + "\"")
                fileWriter.append("\n")
            }
            if(mostShortDay == Int.MAX_VALUE)
                fileWriter.append("mostShortDay: 0")
            else
                fileWriter.append("mostShortDay: $mostShortDay")
            fileWriter.append("\n")
            fileWriter.append("\n")
            fileWriter.append("file Id, days, duration, start times, end times, n of classes, n of prefer class, subjects, study percentage(study time/(end of day-start of day))")
            fileWriter.append("\n")

            for (times in timeList!!.withIndex()) {
                val timeMap = times.value.groupBy { it.day }

                fileWriter.append(times.index.toString())   // file Id
                fileWriter.append(',')
                fileWriter.append("\"" + timeMap.size.toString() + "=" + timeMap.keys.toString() +"\"")  // days
                fileWriter.append(',')
                fileWriter.append("In progress")    // duration
                fileWriter.append(',')
                fileWriter.append("\"" + timeMap.map { startTime[it.key]?.indexOf(it.value[0].startTime) ?: -1 }.toString() +"\"")
                fileWriter.append(',')
                fileWriter.append("\"" + timeMap.map { endTime[it.key]?.indexOf(it.value.last().endTime) ?: -1 }.toString() +"\"")
                fileWriter.append(',')
                fileWriter.append("\"" + timeMap.map { it.value.size }.toString() +"\"")   // n of classes
                fileWriter.append(',')
                var preferClass = 0
                for(subjectMap in times.value.groupBy { it.subjectCode }) {
                    preferClass+=subjectMap.value.map { it.code }.filter {
                        subjects.filter { it1 -> it1.code == subjectMap.key }[0].preferred.contains(it)
                    }.size
                }
                fileWriter.append(preferClass.toString())    // prefer class
                fileWriter.append(',')
                fileWriter.append("\"" + times.value.map { it.subjectCode }.distinct().toString() +"\"")    // subjects
                fileWriter.append(',')
                // those if is for last class that over 12.00am
                fileWriter.append(
                    (timeMap.map {
                        it.value.sumOf { it1 ->
                            if(it1.endTime.isBefore(it1.startTime)) {
                                (24*60) + ChronoUnit.MINUTES.between(it1.startTime , it1.endTime)
                            } else {
                                ChronoUnit.MINUTES.between(it1.startTime , it1.endTime)
                            }
                        } /
                                (
                                        if(it.value.last().endTime.isBefore(it.value.first().startTime))
                                            (24*60) + ChronoUnit.MINUTES.between(it.value.first().startTime, it.value.last().endTime).toFloat()
                                        else
                                            ChronoUnit.MINUTES.between(it.value.first().startTime, it.value.last().endTime).toFloat()
                                        )
                    }.sum() / timeMap.size * 100).toInt().toString()
                )    // study time/(end of day-start of day)
                fileWriter.append("\n")
            }

            println("Write CSV successfully!")
        } catch (e: Exception) {
            println("Writing CSV error!")
            e.printStackTrace()
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
            } catch (e: IOException) {
                println("Flushing/closing error!")
                e.printStackTrace()
            }
        }
    }


    fun generate() {
        val f = File(folderPath, folderName)
        f.mkdir()

        for(times in timeList?.withIndex()!!) {
            val outputFile = File("$folderPath/$folderName/${times.index}.txt")

            outputFile.printWriter().use { out ->
                out.println(times.value.map { it.subjectCode }.distinct().toString() + "\n")
                for(subjects in times.value.groupBy { it.subjectCode }) {
                    out.println(subjects.key + "=" + subjects.value.map { it.code })
                }
                out.println("")

                for(time in times.value.groupBy { it.day }) {
                    out.println(time.key.toString())
                    time.value.forEach {
                        out.println(it.subjectCode + " " + it.code + " " +  it.startTime + "-" +  it.endTime)
                    }
                    out.println("")
                }
            }
        }
    }

}