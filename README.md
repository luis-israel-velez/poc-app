https://tunguyen9889.medium.com/how-to-run-spark-job-on-eks-cluster-54f73f90d0bc


# Example of spark executions on top of k8s
sh /home/lara/programs/spark/bin/spark-submit \
    --master k8s://https://<k8s-apiserver-host>:<k8s-apiserver-port> \
    --deploy-mode cluster \
    --name spark-pi \
    --class org.apache.spark.examples.SparkPi \
    --conf spark.executor.instances=5 \
    --conf spark.kubernetes.container.image=<spark-image> \
    local:///path/to/examples.jar

                                                             

# Docker:

docker build -t prototype-base-app:1.0.0 .

docker tag prototype-base-app:1.0.0 gcr.io/prototype-dataplatform/prototype-base-app
docker push gcr.io/prototype-dataplatform/prototype-base-app


# Cloud CLI:

gcloud storage cp file  gs://gcp-prototype-gcs/landing/file
aws s3 cp file s3://file


Iceberg:



kubec
Spark:


export GOOGLE_APPLICATION_CREDENTIALS=<path>/key.json




---Metastore


thrift://10.0.192.28:9083

./spark-shell --jars ../jars/iceberg-spark-runtime-3.3_2.12-1.2.0.jar \
    --conf spark.sql.warehouse.dir=gs://gcp-prototype-gcs/warehouse


# Spark SQL 

./spark-sql --packages org.apache.iceberg:iceberg-spark-runtime-3.2_2.12:1.2.0 \
    --conf spark.sql.catalog.spark_catalog=org.apache.iceberg.spark.SparkCatalog \
    --conf spark.sql.catalog.spark_catalog.type=hadoop \
    --conf spark.sql.catalog.spark_catalog.warehouse=gs://gcp-prototype-gcs/raw 


spark.sparkContext.hadoopConfiguration.set("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog")
spark.sparkContext.hadoopConfiguration.set("spark.sql.catalog.spark_catalog.type", "hadoop")
spark.sparkContext.hadoopConfiguration.set("spark.sql.catalog.spark_catalog.warehouse", "gs://gcp-prototype-gcs/raw ")


./spark-shell --jars ../jars/iceberg-spark-runtime-3.3_2.12-1.2.0.jar \
    --conf spark.sql.catalog.spark_catalog=org.apache.iceberg.spark.SparkCatalog \
    --conf spark.sql.catalog.spark_catalog.type=hadoop \
    --conf spark.sql.catalog.spark_catalog.warehouse=gs://gcp-prototype-gcs/raw 


./spark-shell --jars ../jars/iceberg-spark-runtime-3.3_2.12-1.2.0.jar \
    --conf spark.sql.catalog.spark_catalog=org.apache.iceberg.spark.SparkCatalog \
    --conf spark.sql.catalog.spark_catalog.type=hadoop \
    --conf spark.sql.catalog.spark_catalog.warehouse=s3a://aws-prototype-s3/raw

spark.sqlContext.sql(""" CREATE OR REPLACE TABLE default.test_iceberg2 (id string, data string) using iceberg; """)
spark.sqlContext.sql(""" insert into default.test_iceberg2 select "Hello", "World"; """)
spark.sqlContext.sql(""" select * from default.test_iceberg2; """).show()



