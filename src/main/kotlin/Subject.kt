import java.time.LocalTime

class Subject {
    var name: String =  ""  
    var code: String =  ""
    var required = mutableMapOf<String,Int>()
    var subjectTimes = mutableListOf<SubjectTime>()
    var chained = mutableListOf<List<String>>()
    var preferred = mutableListOf<String>()
}