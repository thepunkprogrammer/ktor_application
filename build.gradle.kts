import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.dsl.Coroutines

val kotlinVersion = "1.1.61"
val ktorVersion = "0.9.0"

plugins {
    kotlin("jvm")
    application
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

repositories {
    jcenter()
    mavenCentral()
    val bintray = "http://dl.bintray.com/kotlin"
    maven { url = uri("$bintray/ktor") }
    maven { url = uri("$bintray/kotlinx") }
}

dependencies {

    fun ktor(s: String = "", v: String) = "io.ktor:ktor$s:$v"

    compile(kotlin("stdlib-jre8", kotlinVersion))
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.4")
    compile(ktor(v = ktorVersion))
    compile(ktor("-gson", ktorVersion))
    compile(ktor("-html-builder", ktorVersion))
    compile(ktor("-server-netty", ktorVersion))
    compile("ch.qos.logback:logback-classic:1.2.1")
    compile("org.slf4j:slf4j-api:1.7.25")

    testCompile(ktor("-server-test-host", ktorVersion))
    testCompile("junit:junit:4.12")
}

application {
    applicationName = "ktorapp"
    group = "de.swirtz.kotlin.webdev"
    mainClassName = "de.swirtz.kotlin.webdev.ktor.KtorServerKt"
}


tasks {
    withType<Jar> {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClassName))
        }
        val version = "1.0-SNAPSHOT"

        archiveName = "${application.applicationName}-$version.jar"
        from(configurations.compile.map { if (it.isDirectory) it else zipTree(it) })
    }

}
