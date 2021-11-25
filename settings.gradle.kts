rootProject.name = "kmm_workshop"

val buildAndroid = extra["org.gradle.project.buildAndroid"].toString().toBoolean()
if (buildAndroid) {
    include("app")
}

include("common")
