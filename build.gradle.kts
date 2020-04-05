import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val hikariCpVersion: String by project
val postgreSqlJdbcDriverVersion: String by project
val apacheCommonsTextVersion: String by project
val antlrVersion: String by project
val mysqlJdbcDriverVersion: String by project
val mockkVersion: String by project
val junitVersion: String by project
val hamcrestVersion: String by project

plugins {
    // Kotlin JVM
    application
    kotlin("jvm") version "1.3.70"

    // ANTLR
    antlr

    // Docker
    id("com.palantir.docker") version "0.22.1"

    // Detekt
    id("io.gitlab.arturbosch.detekt") version "1.1.1"

}

group = "br.com.dillmann.restdb"
version = "1.0.0".withBuildVersionSuffix()

application {
    mainClassName = "br.com.dillmann.restdb.BootstrapKt"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    // Ktor
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")

    // Logback
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // JDBC
    implementation("com.zaxxer:HikariCP:$hikariCpVersion")
    implementation("org.postgresql:postgresql:$postgreSqlJdbcDriverVersion")

    // Apache Commons
    implementation("org.apache.commons:commons-text:$apacheCommonsTextVersion")

    // ANTLR
    antlr("org.antlr:antlr4:$antlrVersion")
            
    // Tests
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("junit:junit:$junitVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")

}

tasks {
    compileKotlin {
        dependsOn += generateGrammarSource
    }

    docker {
        files(distTar.get().outputs.files)

        val version = project.version
        buildArgs(mapOf(
            "DIST_FILE" to "restdb-$version.tar"
        ))

        name = "lucasdillmann1/restdb:$version"
    }

    check {
        dependsOn.remove(detekt.get())
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
            jvmTarget = "11"
        }
    }

    test {
        useJUnit()
    }
}

fun String.withBuildVersionSuffix(): String {
    val branch = System.getenv("TRAVIS_BRANCH")
    val suffix = if (branch == null || branch.isBlank() || branch == "development") "-SNAPSHOT"
    else if (branch != "master") "-$branch-SNAPSHOT"
    else ""

    return plus(suffix)
}
