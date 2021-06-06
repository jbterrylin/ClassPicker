import java.io.File
import java.io.InputStream
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SubjectsReader {
    var subjects = mutableListOf<Subject>()
    var preferTime = mutableListOf<String>()

    init {
        readFromTxt()
    }

    private fun readFromTxt() {
        val inputStream: InputStream = File("C:\\Users\\lee\\IdeaProjects\\ClassPicker\\src\\main\\resources\\subject_data.txt").inputStream()
        var status = ""
        var subject = Subject()
        inputStream.bufferedReader().forEachLine {
            when {
                it.take(11) == "PreferTime:" -> preferTime = it.drop(11).split(",").map { it1-> it1.trim() }.toMutableList()
                it.take(8) == "Subject:" -> subject.name = it.drop(8).trim()
                it.take(12) == "SubjectCode:" -> subject.code = it.drop(12).trim()
                it.take(12) == "SubjectTime:" -> status = "SubjectTime"
                it.take(8) == "Chained:" -> status = "Chained"
                it.take(10) == "Preferred:" -> status = "Preferred"
                it.take(4) == "    " ->
                    when (status) {
                        "SubjectTime" -> {
                            val subjectTimeString = it.split("    ").map { it1 -> it1.trim()}.toMutableList()
                            subjectTimeString.remove("")
                            val subjectTime = SubjectTime()
                            subjectTime.code = subjectTimeString[0]
                            subjectTime.day = subjectTimeString[1].toInt()
                            subjectTime.startTime = LocalTime.parse(subjectTimeString[2], DateTimeFormatter.ofPattern("HH:mm"))
                            subjectTime.endTime = LocalTime.parse(subjectTimeString[3], DateTimeFormatter.ofPattern("HH:mm"))
                            subjectTime.location = if(subjectTimeString.size == 5) subjectTimeString[4] else ""
                            subject.subjectTimes.add(subjectTime)
                        }
                        "Chained"-> {
                            val subjectTimeString = it.split("    ").map { it1 -> it1.trim()}.toMutableList()
                            subjectTimeString.remove("")
                            for(e in subjectTimeString)
                                subject.chained.add(mutableListOf(e.split("-")[0],e.split("-")[1]))
                        }
                        "Preferred"-> {
                            subject.preferred = it.split("    ").map { it1 -> it1.trim()}.toMutableList()
                            subject.preferred.remove("")
                        }
                    }
                it.take(3) == "end" -> {
                    subjects.add(subject)
                    subject = Subject()
                }
            }
        }
    }
}

