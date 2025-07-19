plugins {
    id("java-library")
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


dependencies {
    implementation("com.squareup:javapoet:1.13.0")

    //背后的服务，能够监听你是否在编译中
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    compileOnly("com.google.auto:auto-common:0.11")
}

