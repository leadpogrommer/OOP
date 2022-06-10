package ru.leadpogrommer.oop.dsl

import java.net.URL
import java.util.*

data class Config(val tasks: List<Task>, val group: Group)
data class Task(val id: String, val name: String, val value: Double)
data class Group(val name: String, val students: List<Student>)
data class Student(val nickname: String, val name: String, val repo: URL, val branch: String, val assignments: List<Assignment>)
data class Assignment(val taskId: String, val date: Date)
