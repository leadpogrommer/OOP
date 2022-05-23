package ru.leadpogrommer.oop.dsl

import java.io.File
import java.net.URL
import java.util.Date

import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
//import kotlin.script.experimental.jvm.
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.script.experimental.jvmhost.createJvmEvaluationConfigurationFromTemplate


data class Config(val tasks: List<Task>, val group: Group)
data class Task(val id: String, val name: String, val value: Double)
data class Group(val name: String, val students: List<Student>)
data class Student(val nickname: String, val Name: String, val repo: URL, val branch: String)
data class Assignment(val taskId: String, val date: Date)


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
        return Config(tasksCtx.tasks, Group(groupCtx.name, groupCtx.students))
    }

}

class TasksContext(){
    internal val tasks = mutableListOf<Task>()
    fun task(id: String, name: String, value: Double){
        tasks.add(Task(id, name, value))
    }
}

class GroupContext(val name: String){
    internal val students = mutableListOf<Student>()

    fun student(nickname: String, name: String, repo: String, branch: String){
        student(nickname, name, URL(repo), branch)
    }

    fun student(nickname: String, name: String, repo: URL, branch: String){
        students.add(Student(nickname, name, repo, branch))
    }

}

class AssignmentsContext(){

}


@KotlinScript(
    fileExtension = "oop.kts"
)
abstract class OOPScript(config: IConfigContext): IConfigContext by config{

}

fun evalFile(scriptFile: File, config: IConfigContext): ResultWithDiagnostics<EvaluationResult> {
    val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<OOPScript> {
        jvm {
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
    }

    val evalConfig = createJvmEvaluationConfigurationFromTemplate<OOPScript>{
        jvm{
            constructorArgs(config)
        }
    }

//    BasicJvmScriptEvaluator()
//    BasicJvmScriptingHost().eval()
    return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), compilationConfiguration, evalConfig)
}


fun main(){
    val configCtx = ConfigContext()
    val res = evalFile(File("Task_2_4_1/test.oop.kts"), configCtx)
//    val result = res.valueOrNull()?.returnValue


    res.reports.forEach{
        if (it.severity > ScriptDiagnostic.Severity.DEBUG) {
            println(" : ${it.message}" + if (it.exception == null) "" else ": ${it.exception}")
        }
    }

    val config = configCtx.render()
    println(config)
}