import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.flywaydb.flyway") version "7.1.1"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.noarg") version "1.9.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

noArg {
    annotation("com.example.degica_project.annotations.NoArgsConstructor")
}

repositories {
    mavenCentral()
}

sourceSets {
    create("initTest") {
        kotlin {
            compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath.get()
            runtimeClasspath += sourceSets.main.get().output + compileClasspath
        }
    }
}

dependencies {
    // While CSVs are easy to create, since we're dealing with
    // other languages, let's use a third-party library just in case.
    // https://mvnrepository.com/artifact/io.github.openfeign/feign-okhttp
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.4")
    implementation("org.apache.commons:commons-csv:1.10.0")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.2")
    implementation("org.flywaydb:flyway-core:10.0.0")
    implementation("org.flywaydb:flyway-database-postgresql:10.0.0")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.2")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

flyway {
    //    TODO: read from environmental variables
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "mysecretpassword"
    driver = "org.postgresql.Driver"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["initTest"].output.classesDirs
    classpath = sourceSets["initTest"].runtimeClasspath
    shouldRunAfter("test")

    useJUnitPlatform()

    testLogging {
        events("passed")
    }
}

tasks.check { dependsOn(integrationTest) }

tasks.withType<Test> {
    useJUnitPlatform()
}
