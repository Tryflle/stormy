plugins {
    java
    id("com.github.weave-mc.weave-gradle") version "fac948db7f"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "me.tryfle"
version = "1.0"

minecraft.version("1.8.9")

repositories {
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven/")
    mavenCentral()
}

dependencies {
    compileOnly("com.github.weave-mc:weave-loader:v0.2.4")
    compileOnly("org.spongepowered:mixin:0.8.5")

}

tasks.compileJava {
    options.release.set(17)
}