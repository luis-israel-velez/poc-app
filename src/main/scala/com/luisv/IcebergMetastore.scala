package com.luisv


import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.iceberg
import org.apache.spark.sql.types.{StructType, StringType}

object IcebergMetastore {

    
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
        .builder.appName("Hello World App")
        .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkSessionCatalog")
        .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
        .config("spark.sql.catalog.spark_catalog.type", "hive")
        //.config("spark.sql.catalog.catalog-spark_catalog.catalog-impl", "")
        //.config("hive.metastore.uris", "thrift://10.51.192.30:9083")
        //.config("hive.metastore.warehouse.dir", "gs://gcp-prototype-gcs/warehouse")
        .config("engine.hive.enabled", "true")
        .config("spark.sql.catalog.spark_catalog.uris", "thrift://10.51.192.33:9083")
        .config("spark.sql.catalog.spark_catalog.warehouse.dir", "gs://gcp-prototype-gcs/warehouse")
        .getOrCreate()

        //Contract piece, End state it should be a API with a Key identifier for the job 
        val schema = new StructType()
            .add("sourcePath", StringType, true)
            .add("sourceFormat", StringType, true)
            .add("destPath", StringType, true)
            .add("keyField", StringType)
 
        val jsonDF = spark.read.format("json").schema(schema).load("gs://gcp-prototype-gcs/contract/example.json")

        //Reading Module, simple way to use contract to detect the origin and destination data as well as prep configuration for next modules

        import spark.implicits._
        val sourcePath = jsonDF.select($"sourcePath").first().getString(0)
        val sourceFormat = jsonDF.select($"sourceFormat").first().getString(0)
        val destTable = jsonDF.select($"destPath").first().getString(0) 
        val keyValue = jsonDF.select($"keyField").first().getString(0)

        //Each one of these should have a module complex enough to understand different formats, transformations, etc

        val srcDF = spark.read.format(sourceFormat).option("header", "true").load(sourcePath).limit(10000)

        //Schema Inference (Simple)
        val srcDFSchema: String = srcDF.schema.mkString
        //After this we would have a module to map the schema to our open table format type (depending on which one we select)
        //val srcDFType = ModuleForTypeTransformation(srcDF)

        //Dedupe, filtering this cause current file has no dups.
        //val srcDFDedupe = srcDFType.dropDuplicate(keyValue)

        //DQ, module that has rules that we agreed upon to be implemented in all files, for example timezoneformat, upper vs lower in codes, etc. 
        //val srcDFDQ = ObjectThatHandleThis(srcDFDedupe)

        //Writing Module, here we write multiple pieces to different tables, if we have API Layer for Metadata, then here we would call them

        //For testing purposes
        val finalDF = srcDF

        finalDF.createOrReplaceTempView("finalDF")

        //spark.sqlContext.sql(s""" CREATE DATABASE raw2; """.stripMargin)

        spark.sqlContext.sql(s""" CREATE OR REPLACE TABLE raw2.${destTable} USING iceberg AS
                                 SELECT * FROM finalDF ; """.stripMargin)

        //This would work as follow:
            //Upon every execution we report the data to pipline_metadata
            //This can be use to audit or build monitoring on top, we can add other metadata if needed/wanted
        spark.sqlContext.sql(s""" CREATE OR REPLACE TABLE raw2.pipeline_metadata USING iceberg AS
        SELECT uuid() as id, "${destTable}_raw" as name, "Ingestion process for ${destTable}" as description, 
        "raw_batch" as type, "${sourcePath}" as src, "${destTable}" as tgt, "data quality rule/result here" as dq_check, 
        current_timestamp() as run_timestamp, "success" as state ;  """.stripMargin)

        //This would work as follow:
            //If schema does not exist, create
            //If schema exist, check for changes, if no changes, don't do anything
            //If schema exist and it is different, create a new version with the changes
        spark.sqlContext.sql(s""" CREATE OR REPLACE TABLE raw2.schema_registry USING iceberg AS
        SELECT "${sourcePath}" as src, "${srcDFSchema}" as src_schema, "${sourceFormat}" as src_type, 1 as version, 
        current_timestamp() as  create_timestamp, current_timestamp() as update_timestamp; """.stripMargin)
        //spark.sqlContext.sql(""" select * from default.test_iceberg3; """).show()
        spark.stop()
    }
}



//spark.sql.catalog.spark_catalog org.apache.iceberg.spark.SparkCatalog
//spark.sql.catalog.spark_catalog.type hadoop
//spark.sql.catalog.spark_catalog.warehouse /<folder for iceberg data>/