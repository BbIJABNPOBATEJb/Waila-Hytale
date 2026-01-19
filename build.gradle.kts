plugins {
    java
}

group = "me.bbijabnpobatejb"
version = "1.0.2"

repositories {
    mavenCentral()
    maven {
        name = "hM"
        url = uri("https://maven.hytale-modding.info/releases")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    // Hytale Server API - compile only since it's provided at runtime
    compileOnly(files("../../HytaleServer.jar"))

    // Testing
    testImplementation(libs.junit)

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

    implementation("com.buuz135:MultipleHUD:1.0.1")
}
tasks.register<Copy>("copyJarToMods") {
    from(tasks.jar)
    into(System.getenv("HYTALE_SERVER_MODS_FOLDER"))
}
tasks.build {
    dependsOn("copyJarToMods")
}
tasks.jar {
    // Set the archive name
    archiveBaseName.set("Waila")
}
