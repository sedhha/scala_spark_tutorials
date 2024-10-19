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
      "-Dlog4j.configuration=file:./src/main/scala/resources/log4j.properties"
    )
  )
