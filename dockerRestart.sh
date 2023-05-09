#!/bin/sh

docker build -t prototype-base-app:1.0.0 .

docker tag prototype-base-app:1.0.0 gcr.io/prototype-dataplatform/prototype-base-app
docker push gcr.io/prototype-dataplatform/prototype-base-app
