package ru.leadpogrommer.oop.dsl

import kotlinx.coroutines.*
import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.gradle.tooling.BuildException
import org.gradle.tooling.GradleConnectionException
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
//            .setBranch(branch)
//            .setBranchesToClone(listOf(branch))
            .setCloneAllBranches(true)
            .setDirectory(repoDirFile)
            .call()

    }

    private suspend fun latestCommitDateForDir(repo: Git, path: Path): LocalDate? = withContext(Dispatchers.IO){
        val relativePath = repo.repository.workTree.toPath().relativize(path).toString()
        val commits = repo.log()
            .addPath(relativePath)
            .all()
            .call()
            .toList()
        println(relativePath)
        println(commits.size)
        val latestCommitTime = commits.maxOfOrNull {
            it.commitTime
        } ?: return@withContext null

        return@withContext LocalDate.ofInstant(Instant.ofEpochSecond(latestCommitTime.toLong()), TimeZone.getDefault().toZoneId())
    }

    private suspend fun getCommitDates(repo: Git): List<LocalDate> = withContext(Dispatchers.IO){
        val commits = repo.log().all().call()
        commits.map { LocalDate.ofInstant(Instant.ofEpochSecond(it.commitTime.toLong()), TimeZone.getDefault().toZoneId()) }
            .toList()
    }

    private fun processTask(taskPath: Path, useRootProject: Boolean): TaskStatus{
        val taskDirFile = taskPath.toFile()
        if(!taskDirFile.exists() || !taskDirFile.isDirectory){
            return TaskStatus(false, false, false);
        }
        val gradle = GradleConnector.newConnector()
        if(useRootProject){
            gradle.forProjectDirectory(taskDirFile.parentFile)
        }else{
            gradle.forProjectDirectory(taskDirFile)
        }
        val taskPrefix = if(useRootProject) taskDirFile.name + ":" else ""
        gradle.useGradleVersion("7.2")
        val connection = gradle.connect()
        val cancelSource = GradleConnector.newCancellationTokenSource()
        val cancelJob = GlobalScope.launch(Dispatchers.Default) {
            delay(config.timeout)
            println("Cancelling")
            cancelSource.cancel()
        }

        try {
            try {
                val buildLauncher = connection.newBuild()
                buildLauncher.forTasks(taskPrefix+"assemble")
                config.javaHome?.let { buildLauncher.setJavaHome(File(it)) }

                buildLauncher.withCancellationToken(cancelSource.token())
                buildLauncher.addProgressListener(ProgressListener { event ->
//                    println("BUILD EVENT: ${event.description}")
                })
                buildLauncher.addArguments("--debug")
//                buildLauncher.setStandardError(System.err)
                buildLauncher.run()

            }catch (e: GradleConnectionException){
                println(e.message)
                return TaskStatus(true, false, false);
            }

            try {
                val testLauncher = connection.newBuild()
                testLauncher.forTasks(taskPrefix + "test")
                config.javaHome?.let { testLauncher.setJavaHome(File(it)) }

                testLauncher.withCancellationToken(cancelSource.token())
//                testLauncher.setStandardError(System.err)
                testLauncher.addProgressListener(ProgressListener {event ->
//                    println("TEST EVENT: ${event.description}")
                })
                testLauncher.run()
            }catch (e: GradleConnectionException) {
                return TaskStatus(true, true, false)
            }
            return TaskStatus(true, true, true)
        }finally {
            cancelJob.cancel()
            connection.close()
            gradle.disconnect()
        }

    }

    suspend fun processStudent(student: Student): StudentResults{
        val tasks = config.tasks
        println("Processing student ${student.name}")
        val repo = getRepo(student)
        val dates = getCommitDates(repo)

        val commitDates = mutableMapOf<String, LocalDate>()
        val results = mutableMapOf<String, TaskStatus>()

        student.assignments.forEach {assignment->
            val task = tasks.find {
                    task->  task.id == assignment.taskId
            }!!
            val taskPath = Paths.get(repo.repository.workTree.absolutePath, task.id)
            val branch = student.taskBranches[task.id]!!
            repo.reset().setMode(ResetCommand.ResetType.HARD).call()
            repo.checkout().setName("origin/$branch").call()
            val result = withContext(gradleContext){
                processTask(taskPath, student.useRootProject)
            }
            val latestCommitDate = latestCommitDateForDir(repo, taskPath)
            println(latestCommitDate)

            println("${student.nickname} - ${task.name} - $result")

            results[assignment.taskId] = result
            latestCommitDate?.let { commitDates[assignment.taskId] = it; println("${assignment.taskId} = $it") }
        }
        // todo: task commits
        return StudentResults(student.nickname, results, commitDates, dates)
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