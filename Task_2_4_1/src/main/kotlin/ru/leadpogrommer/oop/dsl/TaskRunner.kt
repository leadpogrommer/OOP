package ru.leadpogrommer.oop.dsl

import kotlinx.coroutines.*
import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.gradle.tooling.BuildException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProgressListener
import org.gradle.tooling.TestExecutionException
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import java.time.LocalDate
import java.util.*
import kotlin.coroutines.CoroutineContext

class TaskRunner(val config:Config) {
//    override val coroutineContext: CoroutineContext
//        get() = Dispatchers.Default

    @OptIn(DelicateCoroutinesApi::class)
    val gradleContext = newFixedThreadPoolContext(2, "Gradle runner context")
    private val repoBasePath = Paths.get(Paths.get(".").toAbsolutePath().toString(),"student_repos").toAbsolutePath().toString()

    private suspend fun getRepo(student: Student): Git = withContext(Dispatchers.IO){
        val repoPath = Paths.get(repoBasePath, student.nickname)
        println(repoPath)
        val repoDirFile = repoPath.toFile()
        if(repoDirFile.exists()){
            println("WARNING: deleting directory ${repoDirFile.absolutePath}")
            FileUtils.deleteDirectory(repoDirFile)
        }
        val branch = "refs/heads/${student.branch}"
        Git.cloneRepository()
            .setURI(student.repo.toString())
            .setBranch(branch)
            .setBranchesToClone(listOf(branch))
            .setDirectory(repoDirFile)
            .call()

    }

    private suspend fun getCommitDates(repo: Git): List<LocalDate> = withContext(Dispatchers.IO){
        val commits = repo.log().all().call()
        commits.map { LocalDate.ofInstant(Instant.ofEpochSecond(it.commitTime.toLong()), TimeZone.getDefault().toZoneId()) }
            .toList()
    }

    private fun processTask(taskPath: Path): TaskStatus{
        val taskDirFile = taskPath.toFile()
//    println("Processing task $taskPath")
        if(!taskDirFile.exists() || !taskDirFile.isDirectory){
//        println("Task not exists")
            return TaskStatus(false, false, false);
        }
        val gradle = GradleConnector.newConnector()
        gradle.forProjectDirectory(taskDirFile)
        gradle.useGradleVersion("6.8")
        val connection = gradle.connect()
        try {

            val buildLauncher = connection.newBuild()
            buildLauncher.forTasks("build")
            buildLauncher.setJavaHome(File("/usr/lib/jvm/java-11-openjdk/"))
            buildLauncher.addProgressListener(ProgressListener { event ->
//            println("BUILD EVENT: ${event.description}")
            })
            buildLauncher.run()
        }catch (e: BuildException){
//        println("Build failed")
            return TaskStatus(true, false, false);
        }

//    println("running tests")
        try {
            val testLauncher = connection.newTestLauncher()
            testLauncher.setJavaHome(File("/usr/lib/jvm/java-11-openjdk/"))
            testLauncher.withJvmTestClasses("*")

            testLauncher.addProgressListener(ProgressListener {event ->
//        println("TEST EVENT: ${event.description}")

            })
            testLauncher.run()
        }catch (e: TestExecutionException){
            return TaskStatus(true, true, false)
        }catch (e: BuildException){
            return TaskStatus(true, true, false)
        }
        connection.close()
//    println("Task ok")
        return TaskStatus(true, true, true)
    }

    suspend fun processStudent(student: Student): StudentResults{
        val tasks = config.tasks
        println("Processing student ${student.name}")
        val repo = getRepo(student)
        val dates = getCommitDates(repo)

        val results = student.assignments.map {assignment->
            val task = tasks.find {
                    task->  task.id == assignment.taskId
            }!!
            val result = withContext(gradleContext){
                processTask(Paths.get(repo.repository.workTree.absolutePath, task.id))
            }
            println("${student.nickname} - ${task.name} - $result")
            assignment.taskId to result
        }.toMap()
        // todo: task commits
        return StudentResults(student.nickname, results, emptyMap(), dates)
    }

    suspend fun run(): Map<String, StudentResults> = coroutineScope{
        config.group.students.map {
            async {
                processStudent(it)
            }
        }.awaitAll().map { it.id to it }.toMap()
    }

}

data class StudentResults(val id: String, val taskResults: Map<String, TaskStatus>, val taskCommits: Map<String, LocalDate>, val commits: List<LocalDate>)