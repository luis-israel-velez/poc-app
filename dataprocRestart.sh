#!/bin/sh

sbt clean
sbt assembly 
gcloud storage cp target/scala-2.12/HelloWorld-assembly-0.1.0-SNAPSHOT.jar gs://gcp-prototype-gcs/jars/app.jar
