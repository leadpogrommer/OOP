package ru.leadpogrommer.oop.dsl

import java.io.File

suspend fun main() {
    val config = readFile(File("Task_2_4_1/test.oop.kts"))
    val runner = TaskRunner(config)
    val results = runner.run()
    println("Got results")

    val resultsFile = File("results.html")
    resultsFile.writeText(generateHTML(config, results))
}

