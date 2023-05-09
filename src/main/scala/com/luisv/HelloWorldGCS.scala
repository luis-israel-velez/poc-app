package com.luisv


import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object HelloWorldGCS {
    
   def main(args: Array[String]): Unit = {
        val spark = SparkSession
        .builder        
        .appName("Hello World App")
        .getOrCreate()

        println("Hello, world!")

        val dfTest = spark.read.textFile("gs://gcp-prototype-gcs/test.txt").show(false)
        import spark.implicits._
        val origDF  = Seq(("Hello", "123"), ("World", "456")).toDF("x", "y")
        origDF.write.format("parquet").mode("overwrite").save("gs://gcp-prototype-gcs/hello-world")
        spark.stop()
    }
}
