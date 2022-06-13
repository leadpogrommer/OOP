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

    val resultsFile = File("results.html")
    resultsFile.writeText(generateHTML(config, results))
//    print(resultHtml)
}

