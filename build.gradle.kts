plugins {
    id("java")
    //application
}

/*
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "net.cowtopia.dscjava.Main"
    }
}*/




group = "net.cowtopia.dscjava"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.21")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    //implementation("net.dv8tion:JDA:5.0.0-beta.20")
}


tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "net.cowtopia.dscjava.Main"
    }
    include("net.dv8tion:JDA:5.0.0-beta.20")
}
