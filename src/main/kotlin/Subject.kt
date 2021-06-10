class Subject {
    var name: String =  ""  
    var code: String =  ""
    var required = mutableMapOf<String,Int>()
    var subjectTimes = mutableListOf<SubjectTime>()
    var chained = mutableMapOf<String, MutableList<MutableList<String>>>()
    var preferred = mutableListOf<String>()
}