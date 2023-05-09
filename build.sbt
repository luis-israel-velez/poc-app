ThisBuild / scalaVersion := "2.12.14"
ThisBuild / organization := "com.luisv"
resolvers += "Spark Packages" at "https://repos.spark-packages.org/"


lazy val root = (project in file("."))

  .settings(

    name := "HelloWorld",

    //mainClass := Some("com.example.HelloWorld"),
    //      "com.google.cloud" % "google-cloud-storage" % "2.12.0",
    //      "com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.2.12"
    //    exclude("com.google.protobuf", "protobuf-java")
    //    exclude("io.grpc", "grpc-protobuf-lite"),
    //  "com.google.guava" % "guava" % "23.0",
    //  "com.google.protobuf" % "protobuf-java" % "3.5.1",
    libraryDependencies ++= Seq( 
      "org.apache.spark" %% "spark-sql" % "3.3.0",
      "org.apache.iceberg" %% "iceberg-spark-runtime-3.3" % "1.2.0",
      //"org.apache.hadoop" % "hadoop-aws" % "3.3.0",
      //"com.amazonaws" % "aws-java-sdk-bundle" % "1.11.563",
      //"com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.2.12",
      //"com.google.protobuf" % "protobuf-lite" % "3.0.1",
      //"com.google.protobuf" % "protobuf-java" % "3.5.1",
    )
    //libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.3.0",
    //libraryDependencies += "io.delta" %% "delta-core" % "2.1.0",
    //      "io.delta" % "delta-core_2.12" % "1.0.1",
    //  "org.apache.hadoop" % "hadoop-client" % "3.1.0"
    //excludeDependencies += ExclusionRule(organization = "org.apache.spark")
  )

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}