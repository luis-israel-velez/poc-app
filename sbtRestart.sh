#!/bin/sh

sbt clean
sbt assembly
docker build -t prototype-base-app:1.0.0 .
