fun main() {
    val subjectsReader = SubjectsReader()
    Calculator(subjectsReader.subjects, subjectsReader.preferTime, subjectsReader.mustTakeSubjectCodes, subjectsReader.optionalNeeded).generateSchedule()
}