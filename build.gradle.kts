plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.2.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.github.niestrat99"
version = project.property("plugin_version")!!

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.bsdevelopment.org/releases")
    maven("https://repo.essentialsx.net/releases/")
    maven("https://repo.extendedclip.com/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("net.essentialsx:EssentialsXDiscord:2.20.1")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation("com.github.thatsmusic99:ConfigurationMaster-API:v2.0.0-rc.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        val baseRelocation = "io.github.niestrat99.chatsplus.libs"
        relocate("io.github.thatsmusic99.configurationmaster", "$baseRelocation.configurationmaster")
    }

    processResources {
        inputs.property("plugin_version", project.version)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(Pair("plugin_version", project.version))
        }
    }

    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.20.4")
    }
}