class Calculator(var subjects: MutableList<Subject>,var  preferTime: MutableList<String>) {
    fun generateSchedule() {
//        subjects.forEach { subject ->
//            subject.combineTime()
//        }
        subjects[0].combineTime()
    }
}