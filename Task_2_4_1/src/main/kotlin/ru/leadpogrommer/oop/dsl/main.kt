package ru.leadpogrommer.oop.dsl

//import org.gr

//import org.eclipse.jgit


import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.io.File


suspend fun main() {
    val config = readFile(File("Task_2_4_1/test.oop.kts"))
    val runner = TaskRunner(config)
    val results = runner.run()
    println("Got results")
    val resultHtml = createHTML().html {
        body {
            style {
                +"table, th, td {\n  border: 1px solid black;\n border-collapse: collapse;\n}"
            }
            h1 {
                +"Group ${config.group.name}"
                br { }
                +"Grades"
            }
            table {
                // first header row
                tr {
                    td()
                    config.tasks.forEach { task ->
                        td {
                            colSpan = "6"
                            +"${task.name}"
                        }
                    }
                    td { +"Total" }
                }
                // second header row
                tr {
                    td { +"Student" }
                    config.tasks.forEach {
                        td { +"Assigned" }
                        td { +"Present" }
                        td { +"Builds" }
                        td { +"Tests" }
                        td { +"In time" }
                        td { +"Score" }
                    }
                    td { }
                }
                config.group.students.forEach { student ->
                    var score = 0.0
                    tr {
                        td { +student.name }
                        config.tasks.forEach { task ->
                            val assigned = student.assignments.filter { it.taskId == task.id }.isNotEmpty()
                            if (assigned) {
                                td { +"+" }
                                val result = results[student.nickname]!!
                                val taskResult = result.taskResults[task.id]!!
                                td { +plusMinus(taskResult.present) }
                                td { +plusMinus(taskResult.builds) }
                                td { +plusMinus(taskResult.testsPass) }
                                td { +"Todo" }
                                val scorePerTask = if (taskResult.testsPass) {
                                    task.value
                                } else {
                                    0.0
                                }
                                td { +"$scorePerTask" }
                                score += scorePerTask

                            } else {
                                td { +"-" }
                                for (i in 0 until 5) td { }
                            }
                        }
                        td { +"$score" }
                    }
                }
            }
        }

    }
    val resultsFile = File("results.html")
    resultsFile.writeText(resultHtml)
//    print(resultHtml)
}

fun plusMinus(v: Boolean) = if (v) {
    "+"
} else {
    "-"
}