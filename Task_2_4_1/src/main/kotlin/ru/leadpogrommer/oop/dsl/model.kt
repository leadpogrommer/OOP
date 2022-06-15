package ru.leadpogrommer.oop.dsl

import java.net.URL
import java.time.LocalDate

data class Config(val tasks: List<Task>, val group: Group, val timeout: Long, val javaHome: String?)
data class Task(val id: String, val name: String, val value: Double)
data class Group(val name: String, val students: List<Student>)
data class Student(
    val nickname: String,
    val name: String,
    val repo: URL,
    val branch: String,
    val assignments: List<Assignment>,
    val taskBranches: Map<String, String>,
    val useRootProject: Boolean
)

data class Assignment(val taskId: String, val date: LocalDate)
