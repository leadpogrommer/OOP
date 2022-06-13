package ru.leadpogrommer.oop.dsl

import java.io.File
import java.util.*
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.script.experimental.jvmhost.createJvmEvaluationConfigurationFromTemplate

@KotlinScript(
    fileExtension = "oop.kts"
)
abstract class OOPScript(config: IConfigContext) : IConfigContext by config {

}

private fun evalFile(scriptFile: File, config: IConfigContext): ResultWithDiagnostics<EvaluationResult> {
    val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<OOPScript> {
        jvm {
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
        defaultImports(Date::class)
    }

    val evalConfig = createJvmEvaluationConfigurationFromTemplate<OOPScript> {
        jvm {
            constructorArgs(config)

        }
    }

    return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), compilationConfiguration, evalConfig)
}

fun readFile(scriptFile: File): Config {
    val configCtx = ConfigContext()
    val res = evalFile(scriptFile, configCtx)

    res.reports.forEach {
        if (it.severity > ScriptDiagnostic.Severity.DEBUG) {
            println("${it.location} : ${it.message}" + if (it.exception == null) "" else ": ${it.exception}")
        }
    }

    val returnValue = res.valueOrThrow().returnValue
    if (returnValue is ResultValue.Error) {
        throw returnValue.error
    }

    return configCtx.render()
}