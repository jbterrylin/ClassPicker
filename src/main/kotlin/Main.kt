fun main() {
    val subjectsReader = SubjectsReader()
    val result = Calculator(subjectsReader.subjects, subjectsReader.mustTakeSubjectCodes, subjectsReader.optionalNeeded).generateSchedule()
    val outputGenerator = OutputGenerator(subjectsReader.subjects, result)
    outputGenerator.preferSettingUp()
    outputGenerator.generate()
    outputGenerator.generateMenu()
}

//class Main {
//    companion object {
//        @JvmStatic fun main(args : Array<String>) {
//            val subjectsReader = SubjectsReader()
//            val result = Calculator(subjectsReader.subjects, subjectsReader.mustTakeSubjectCodes, subjectsReader.optionalNeeded).generateSchedule()
//            val outputGenerator = OutputGenerator(subjectsReader.subjects, result)
//            outputGenerator.preferSettingUp()
//            outputGenerator.generate()
//            outputGenerator.generateMenu()
//        }
//    }
//}