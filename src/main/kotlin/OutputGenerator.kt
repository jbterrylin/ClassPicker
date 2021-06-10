import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalTime

class OutputGenerator(private var timeList: MutableList<MutableList<SubjectTime>>?) {
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
            fileWriter.append("mostShortDay: $mostShortDay")
            fileWriter.append("\n")
            fileWriter.append("\n")
            fileWriter.append("file Id, days, duration, start times, end times, n of classes, subjects")
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
                fileWriter.append("\"" + times.value.map { it.subjectCode }.distinct().toString() +"\"")   // n of classes

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
            val timeMap = times.value.groupBy { it.day }
            outputFile.printWriter().use { out ->
                out.println(times.value.map { it.subjectCode }.distinct().toString() + "\n")
                for(time in timeMap) {
                    out.println(time.key.toString())
                    out.println(time.value.map { it.subjectCode + " " + it.code + " " +  it.day + " " +  it.startTime + " " +  it.endTime + "\n"}.toString().filter { it != '[' || it != ']' }.toString())
                    out.println("\n")
                }
            }
        }
    }

}