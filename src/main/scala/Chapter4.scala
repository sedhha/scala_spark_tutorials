import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD

object Chapter5 extends App {
  private val spark = SparkSession.builder()
    .appName("Chapter5")
    .master("local[*]")
    .getOrCreate()

  // val nums = spark.sparkContext.parallelize(1 to 1_000_000_000)
  // val sum = nums.reduce(_ + _)
  // val nums = spark.sparkContext.parallelize(1 to 10)
  // val sum = nums.reduce(_ + _)
  // val numSum = nums.take(5).reduce(_ + _)
  // println(s"Num sum until 5 elements is ${numSum}")
  // println(s"Num sum for all elements is ${sum}")
  /*
    nums <-> (1 to 10) [Lineage Graph]
    nums.take(5) (5 to 10)
    Persistence and Caching 
    DISK_ONLY MEMORY_ONLY DISK_AND_MEMORY
    -----------------------------------
    Persistence
    
                            -----------
                              Caching
                            -----------
    -----------------------------------
  */
  // val x = nums.count()
  // nums.persist()
  // val y = nums.count() 
  /*
   nums = (1,2,3,4,5) -> 5
   Transformation -> Lazy Evaluation
   Only when action takes place is when transformation
   goes into active state
   count, take, collect -> nums (1,2,3,4,5) -> count
   .persist()
   Memory -> (1,2,3,4,5) -> faster to access .count,.collect
    (1,2,3,4,5) -> 5 : Count Result
     // memory
     recompute everything but this time it will
     take RDD from memory
     [(key, value), (key, value)]
     [("sam","Delhi"), ("sam", "Hyderabad")]
     -> reduceByKey(_ + "-" + _)
     [("sam", "Delhi-Hyderabad")]
  */
  val lines = spark.sparkContext.textFile("./src/assets/sample.txt")
  /*
    val lineLengths = lines.map(_.length)
    val lineLengths = lines.map(line => line.length)
  */
  val lineLengths = lines.map(_.length)
  val totalLines = lineLengths.count()
  lineLengths.persist()
  val totalLength = lineLengths.reduce(_+_)
  println(s"Total Lines = $totalLines & Total Lengths = $totalLength")

}
