fun main() {
    val subjectsReader = SubjectsReader()
    val result = Calculator(subjectsReader.subjects, subjectsReader.preferTime, subjectsReader.optionalNeeded).generateSchedule()
    OutputGenerator(result).generate()
}