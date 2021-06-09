fun main() {
    val subjectsReader = SubjectsReader()
    val result = Calculator(subjectsReader.subjects, subjectsReader.mustTakeSubjectCodes, subjectsReader.optionalNeeded).generateSchedule()
    OutputGenerator(result, subjectsReader.preferTime).generate()
}