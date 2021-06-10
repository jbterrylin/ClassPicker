import java.io.File
import java.io.InputStream
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SubjectsReader {
    var subjects = mutableListOf<Subject>()
    var mustTakeSubjectCodes = listOf<String>()
    var optionalNeeded = 0

    init {
        val inputStream: InputStream = File("C:\\Users\\lee\\IdeaProjects\\ClassPicker\\src\\main\\resources\\subject_data.txt").inputStream()
        var status = ""
        var subject = Subject()
        inputStream.bufferedReader().forEachLine {
            when {
                it.take(9) == "MustTake:" -> mustTakeSubjectCodes = it.drop(9).split("    ").map { it1 -> it1.trim() }.filter { it1 -> it1.isNotBlank() }
                it.take(15) == "OptionalNeeded:" -> optionalNeeded = it.drop(15).trim().toInt()
                it.take(8) == "Subject:" -> subject.name = it.drop(8).trim()
                it.take(12) == "SubjectCode:" -> subject.code = it.drop(12).trim()
                it.take(9) == "Required:" -> {
                    val subjectRequiredString = it.drop(9).split("    ").map { it1 -> it1.trim()}.toMutableList()
                    subjectRequiredString.remove("")
                    for(e in subjectRequiredString)
                        subject.required[e.split("-")[0]] = e.split("-")[1].toInt()
                }
                it.take(12) == "SubjectTime:" -> status = "SubjectTime"
                it.take(8) == "Chained:" -> status = "Chained"
                it.take(10) == "Preferred:" -> status = "Preferred"
                it.take(4) == "    " ->
                    when (status) {
                        "SubjectTime" -> {
                            val subjectTimeString = it.split("    ").map { it1 -> it1.trim()}.toMutableList()
                            subjectTimeString.remove("")
                            val subjectTime = SubjectTime()
                            subjectTime.subjectCode = subject.code
                            subjectTime.group = subjectTimeString[0]
                            subjectTime.code = subjectTimeString[1]
                            subjectTime.day = subjectTimeString[2].toInt()
                            subjectTime.startTime = LocalTime.parse(subjectTimeString[3], DateTimeFormatter.ofPattern("HH:mm"))
                            subjectTime.endTime = LocalTime.parse(subjectTimeString[4], DateTimeFormatter.ofPattern("HH:mm"))
                            subjectTime.location = if(subjectTimeString.size == 6) subjectTimeString[5] else ""
                            subject.subjectTimes.add(subjectTime)
                        }
                        "Chained"-> {
                            val subjectTimeString = it.split("    ").map { it1 -> it1.trim()}.toMutableList()
                            subjectTimeString.remove("")
                            for(e in subjectTimeString)
                                subject.chained.add(e.split(",").sorted())
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

