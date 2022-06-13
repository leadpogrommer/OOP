package ru.leadpogrommer.oop.dsl

import java.net.URL
import java.time.LocalDate

interface IConfigContext {
    fun tasks(init: TasksContext.() -> Unit)
    fun group(name: String, init: GroupContext.() -> Unit)
    fun defaultAssignments(init: AssignmentsContext.() -> Unit)
    fun timeout(millis: Long)
    fun javaHome(path: String)
}

class ConfigContext : IConfigContext {
    private val tasksCtx = TasksContext()
    private lateinit var groupCtx: GroupContext
    private lateinit var defaultAssignmentsCtx: AssignmentsContext
    var timeout: Long = 20000
    var javaHomePath: String? = null


    override fun tasks(init: TasksContext.() -> Unit) {
        tasksCtx.init()
    }

    override fun group(name: String, init: GroupContext.() -> Unit) {
        if (this::groupCtx.isInitialized) throw java.lang.IllegalStateException("group initialized twice")
        groupCtx = GroupContext(name)
        groupCtx.init()

    }

    override fun defaultAssignments(init: AssignmentsContext.() -> Unit) {
        if (this::defaultAssignmentsCtx.isInitialized) throw IllegalStateException("assignments initialized twice")
        defaultAssignmentsCtx = AssignmentsContext()
        defaultAssignmentsCtx.init()
    }

    override fun timeout(millis: Long) {
        timeout = millis
    }

    override fun javaHome(path: String) {
        javaHomePath = path
    }

    internal fun render(): Config {
        if (!this::groupCtx.isInitialized) throw IllegalStateException()

        val defaultAssignments = defaultAssignmentsCtx.assignments.map { it.taskId to it.date }.toMap()
        val processedStudents = groupCtx.students.map { pair ->
            val student = pair.first
            val cancellations = pair.second
            val assignments = defaultAssignments.toMutableMap()
            val newAssignments = assignments.plus(student.assignments.map { it.taskId to it.date }).minus(cancellations)
                .map { Assignment(it.key, it.value) }
            student.copy(
                assignments = newAssignments,
                taskBranches = tasksCtx.tasks.map { it.id to student.branch }.toMap().plus(student.taskBranches)
            )
        }
        return Config(tasksCtx.tasks, Group(groupCtx.name, processedStudents), timeout, javaHomePath)
    }
}

class TasksContext() {
    internal val tasks = mutableListOf<Task>()
    fun task(id: String, name: String, value: Double) {
        tasks.add(Task(id, name, value))
    }
}

class GroupContext(val name: String) {
    // student, unassignments
    internal val students = mutableListOf<Pair<Student, List<String>>>()

    fun student(
        nickname: String,
        name: String,
        repo: String,
        branch: String,
        assignmentBuilder: StudentContext.() -> Unit = {}
    ) {
        val aCtx = StudentContext()
        aCtx.assignmentBuilder()

        students.add(
            Student(
                nickname,
                name,
                URL(repo),
                branch,
                aCtx.assignments,
                aCtx.taskBranches,
                aCtx.useRootProject
            ) to aCtx.cancellation
        )
    }
}

class StudentContext : AssignmentsContext(canCancel = true) {
    internal val taskBranches = mutableMapOf<String, String>()
    internal var useRootProject = false

    fun taskBranch(taskID: String, branch: String) {
        taskBranches[taskID] = branch
    }

    fun useRootProject() {
        useRootProject = true;
    }

}

open class AssignmentsContext(val canCancel: Boolean = false) {
    internal val assignments = mutableListOf<Assignment>()
    internal val cancellation = mutableListOf<String>()

    fun assign(task: String, date: String) {
        assignments.add(Assignment(task, LocalDate.parse(date)))
    }

    fun unAssign(task: String) {
        if (!canCancel) throw IllegalStateException("You cannot unAssign() here")
        cancellation.add(task)
    }
}