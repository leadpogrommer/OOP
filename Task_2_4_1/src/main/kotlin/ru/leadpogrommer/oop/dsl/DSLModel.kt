package ru.leadpogrommer.oop.dsl

import java.net.URL
import java.time.LocalDate

interface IConfigContext{
    fun tasks(init: TasksContext.() -> Unit)
    fun group(name:String, init: GroupContext.() -> Unit)
    fun defaultAssignments(init: AssignmentsContext.() -> Unit)
}



class ConfigContext: IConfigContext{
    private val tasksCtx = TasksContext()
    private lateinit var groupCtx: GroupContext
    private lateinit var defaultAssignmentsCtx: AssignmentsContext


    override fun tasks(init: TasksContext.() -> Unit) {
        tasksCtx.init()
    }

    override fun group(name: String, init: GroupContext.() -> Unit) {
        if(this::groupCtx.isInitialized)throw java.lang.IllegalStateException("group initialized twice")
        groupCtx = GroupContext(name)
        groupCtx.init()

    }

    override fun defaultAssignments(init: AssignmentsContext.() -> Unit) {
        if(this::defaultAssignmentsCtx.isInitialized)throw IllegalStateException("assignments initialized twice")
        defaultAssignmentsCtx = AssignmentsContext()
        defaultAssignmentsCtx.init()
    }

    internal fun render(): Config {
        if(!this::groupCtx.isInitialized)throw IllegalStateException()

        val defaultAssignments = defaultAssignmentsCtx.assignments.map { it.taskId to it.date }.toMap()
        val processedStudents = groupCtx.students.map { pair ->
            val student = pair.first
            val cancellations = pair.second
            val assignments = defaultAssignments.toMutableMap()
            val newAssignments = assignments.plus(student.assignments.map { it.taskId to it.date }).minus(cancellations).map { Assignment(it.key, it.value) }
            student.copy(assignments = newAssignments)

        }


        return Config(tasksCtx.tasks, Group(groupCtx.name, processedStudents))
    }

}

class TasksContext(){
    internal val tasks = mutableListOf<Task>()
    fun task(id: String, name: String, value: Double){
        tasks.add(Task(id, name, value))
    }
}

class GroupContext(val name: String){
    // student, unassignments
    internal val students = mutableListOf<Pair<Student, List<String>>>()

    fun student(nickname: String, name: String, repo: String, branch: String, assignmentBuilder: AssignmentsContext.() -> Unit = {}){
        val aCtx = AssignmentsContext(true)
        aCtx.assignmentBuilder()

        students.add(Student(nickname, name, URL(repo), branch, aCtx.assignments) to aCtx.cancellation)
    }

}

class AssignmentsContext(val canCancel: Boolean = false){
    internal val assignments = mutableListOf<Assignment>()
    internal val cancellation = mutableListOf<String>()

    fun assign(task: String, date: String){
        assignments.add(Assignment(task, java.sql.Date.valueOf(LocalDate.parse(date))))
    }

    fun unAssign(task: String){
        if(!canCancel)throw IllegalStateException("You cannot unAssign() here")
        cancellation.add(task)
    }

}