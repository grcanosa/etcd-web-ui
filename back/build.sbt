
organization in ThisBuild := "com.grcanosa"

scalaVersion in ThisBuild := "2.12.10"

resolvers += "confluent.io" at "http://packages.confluent.io/maven/"

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
updateOptions := updateOptions.value.withGigahorse(false)
// Setting javac options in common allows IntelliJ IDEA to import them automatically
javacOptions in compile ++= Seq(
      "-encoding", "UTF-8",
      "-source", "1.8",
      "-target", "1.8",
      "-parameters",
      "-Xlint:unchecked",
      "-Xlint:deprecation"
)

name in (ThisBuild, Compile, assembly) := "etcd-web-ui"

assemblyJarName in assembly := s"${name.value}_${scalaVersion.value}-${version.value}-assembly.jar"

// Skip the tests (comment out to run the tests).
test in assembly := {}

// Publish Fat JARs. Libraries should not publish fat jars.
artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.withClassifier(Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)


pomExtra := {
  <scm>
    <url>https://github.com/grcanosa/etcd-web-ui</url>
    <connection>https://github.com/grcanosa/etcd-web-ui.git</connection>
  </scm>
    <developers>
      <developer>
        <id>grcanosa@gmail.com</id>
        <name>Gonzalo Rodriguez</name>
        <url>https://github.com/grcanosa</url>
      </developer>
    </developers>

}

libraryDependencies ++= Seq(
  "com.github.mingchuno" %% "etcd4s-core" % "0.2.0",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.0",
  "com.typesafe.akka" %% "akka-http"   % "10.1.10",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"


)

assemblyMergeStrategy in assembly := {
  // Needed only to sbt assembly non provided spark-streaming-kafka-0-10
  case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class") => MergeStrategy.first
  // Needed only to sbt assembly etcd coming with libraries
  case PathList("META-INF", "io.netty.versions.properties", xs @ _*) => MergeStrategy.last
  // Needed only to sbt assembly mastria-etcd4s coming with libraries
  case PathList("scala","collection","mutable", xs @ _*) => MergeStrategy.first
  case PathList("scala","collection","mutable", xs @ _*) => MergeStrategy.first
  case PathList("scala","util", xs @ _*) => MergeStrategy.first
  case PathList("library.properties", xs @ _*) => MergeStrategy.first
  case PathList("logback.xml",xs @ _ *) => MergeStrategy.last
  case x => (assemblyMergeStrategy in assembly).value(x)
}

logLevel in assembly := Level.Info


// The default SBT testing java options are too small to support running many of the tests due to the need to launch Spark in local mode.
parallelExecution in Test := false
fork in Test := true
javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")

updateOptions := updateOptions.value.withLatestSnapshots(false)
