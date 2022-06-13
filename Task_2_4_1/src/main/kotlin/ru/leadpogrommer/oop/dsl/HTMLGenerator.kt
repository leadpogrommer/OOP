package ru.leadpogrommer.oop.dsl

import kotlinx.html.*
import kotlinx.html.stream.createHTML

fun generateHTML(config: Config, results: Map<String, StudentResults>): String{
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
                            +task.name
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
                            td{plusMinus(assigned)}
                            if (assigned) {
                                val assignment = student.assignments.find { it.taskId == task.id }!!
                                val result = results[student.nickname]!!
                                val taskResult = result.taskResults[task.id]!!

                                td { plusMinus(taskResult.present) }
                                td { plusMinus(taskResult.builds) }
                                td { plusMinus(taskResult.testsPass) }
                                val latestCommit = result.taskCommits[task.id]
                                val wasInTime = (latestCommit != null) && taskResult.testsPass && (assignment.date.isAfter(latestCommit))
                                if(!taskResult.testsPass){
                                    td { +"N/A" }
                                }else if(latestCommit == null){
                                    td {+"UNKNOWN"}
                                }else{
                                    td{
                                        +"Deadline: ${assignment.date}"
                                        br {  }

                                        +"Done: $latestCommit"
                                        br { }
                                        plusMinus(wasInTime)
                                    }
                                }
                                var scorePerTask = if (taskResult.testsPass) {
                                    task.value
                                } else {
                                    0.0
                                }
                                if(!wasInTime)scorePerTask *= 0.5
                                td { +"$scorePerTask" }
                                score += scorePerTask

                            } else {
                                for (i in 0 until 5) td { }
                            }
                        }
                        td { +"$score" }
                    }
                }
            }
        }

    }
    return resultHtml
}

private fun FlowContent.plusMinus(v: Boolean) {
    if (v) {
        div(){
            attributes["style"] ="background-color:green"
            +"+"
        }
    } else {
        div(){
            attributes["style"] ="background-color:red"
            +"-"
        }
    }
}