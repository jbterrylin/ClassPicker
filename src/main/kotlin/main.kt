fun main() {
    val subjectsReader = SubjectsReader()
    val result = Calculator(subjectsReader.subjects, subjectsReader.mustTakeSubjectCodes, subjectsReader.optionalNeeded).generateSchedule()
    val outputGenerator = OutputGenerator(subjectsReader.subjects, result)
    outputGenerator.preferSettingUp()
    outputGenerator.generate()
    outputGenerator.generateMenu()
}