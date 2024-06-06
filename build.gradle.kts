plugins {
    id("java")
}

group = "dev.montanarbriggs"
version = "1.0-DEVELOPMENT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/net.dv8tion/JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.24")

    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20240303")

    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.5.6")
}