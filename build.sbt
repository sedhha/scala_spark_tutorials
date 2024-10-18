ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "getting_started_with_spark",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.4.2",
      "org.apache.spark" %% "spark-sql" % "3.4.2"
    ),

    dependencyOverrides += "org.scala-lang" % "scala-library" % scalaVersion.value,

    fork := true,

    javaOptions ++= Seq(
      "-Dlog4j.configuration=file:./src/main/scala/resources/log4j.properties",
      "--add-opens=java.base/java.nio=ALL-UNNAMED",                              // Allow access to java.nio
      "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",                            // Allow access to sun.nio.ch
      "--add-opens=java.base/java.util=ALL-UNNAMED",                             // Allow access to java.util
      "--add-opens=java.base/java.lang=ALL-UNNAMED",                             // Allow access to java.lang
      "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED",                  // Allow access to java.util.concurrent
      "--add-opens=java.base/java.net=ALL-UNNAMED",                              // Allow access to java.net (for URI.scheme)
      "--illegal-access=permit"
    )
  )
