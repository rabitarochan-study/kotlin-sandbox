package io.github.rabitarochan.sandbox.kotlin.nashorn

import java.io.InputStream
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

fun main(args: Array<String>) {
    var start = System.currentTimeMillis()
    val engine = createScriptEngine()
    val md = engine.get("md")
    println("[CreateEngine time: ${System.currentTimeMillis() - start}ms]")

    start = System.currentTimeMillis()
    val res = (engine as Invocable).invokeMethod(md, "render", "# It works!")
    println(res)
    println("[invokeMethod time: ${System.currentTimeMillis() - start}ms]")
}

fun createScriptEngine(): ScriptEngine {
    val factory = ScriptEngineManager()
    val engine = factory.getEngineByName("JavaScript")
    resourceAsStreamOf("nashorn/markdown-it.js").use { stream ->
        val js = stream.reader().readText()
        engine.eval(js)
    }
    engine.eval("""
        var md = markdownit();
    """)
    return engine
}

fun resourceAsStreamOf(path: String): InputStream {
    val cl = Thread.currentThread().contextClassLoader
    return cl.getResourceAsStream(path)
}
