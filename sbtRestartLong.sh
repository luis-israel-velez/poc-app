#!/bin/sh

sbt clean
sbt reload
sbt update
sbt assembly
docker build -t prototype-base-app:1.0.0 .
