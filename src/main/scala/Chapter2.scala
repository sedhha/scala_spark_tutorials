import org.apache.spark.sql.SparkSession
object Chapter2 extends App {
  private val spark = SparkSession.builder()
    .appName("Chapter2")
    .master("local[*]")
    .getOrCreate()
  import spark.implicits._
  // Reading text file with RDD ->
  private val rddText = spark.sparkContext.textFile("./src/assets/sample.txt")
  println(s"This is the RDD version of the text file")
  rddText.collect().foreach(println)
  // Reading text file with DataFrame ->
  println(s"This is the DataFrame version of the text file")
  private val dfText = spark.read.text("./src/assets/sample.txt")
  dfText.show()
  // Converting rdd to dataframe and vice versa
  private val rddToDf = rddText.toDF("line")
  println(s"This is the DataFrame version of the RDD")
  rddToDf.show()
  private val dfTordd = dfText.rdd
  println(s"This is the RDD version of the DataFrame")
  dfTordd.collect().foreach(println)


  spark.stop()
}
