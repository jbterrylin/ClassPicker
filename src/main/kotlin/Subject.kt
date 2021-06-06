class Subject {
    var name: String =  ""  
    var code: String =  ""
    var subjectTimes = mutableListOf<SubjectTime>()
    var chained = mutableListOf<MutableList<String>>()
    var preferred = mutableListOf<String>()
}