import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col,explode,split, lower}

object Chapter2 {
  private val spark = SparkSession.builder()
    .appName("Chapter2")
    .master("local[*]")
    .getOrCreate()
  
  /*
    How to read a text file in Spark
    How to convert RDD to DataFrame and vice versa
    Line Count of given text file
    Word Count of given text file
  */
  
  // Through RDDs and DataFrames
  private val pathToTextFile = "./src/assets/sample.txt"

  // Through RDDs
  private val rddText = spark.sparkContext.textFile(pathToTextFile)
  // rddText.collect().foreach(println)
  // println(s"Line Count of the given file - ${rddText.collect().length}")
  
  // Thorugh DF
  private val dfText = spark.read.text(pathToTextFile)
  dfText.show()

  // Conversions

  // Convert to DF from RDD
  // println("Convert to DF from RDD")
  import spark.implicits._
  private val dfFromRDD = rddText.toDF("something_else")
  // dfFromRDD.show()

  // Convert to RDD from DF
  // println("Convert to RDD from DF")
  private val rddFromDF = dfText.rdd
  // rddFromDF.collect().foreach(println)
  
  // Word Count
  // println("Word Count of the given file")
  private val numberOfWords = rddText
  .flatMap(line => line.split(" "))
  .map(word => (word.toLowerCase, 1))
  .reduceByKey((value1,value2) => value1 + value2)
  // numberOfWords.collect().foreach(println)
  
  // Do this with dataframe

  private val dfWordCounts = dfText
                              .select(
                                explode(split(lower(col("value")), " ")).as("word")
                              )

  private val dfAggregation = dfWordCounts.groupBy("word").count()
  println("WOrd count with dataframe")
  dfAggregation.show()


  spark.stop()
}