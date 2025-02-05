plugins {
    java
    application
}

application {
    mainClass.set("io.getunleash.example.ConnectToEdgeSample")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("io.getunleash:unleash-client-java:9.2.6")
    implementation("ch.qos.logback:logback-classic:1.4.6")
}
