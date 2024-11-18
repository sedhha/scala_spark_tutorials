import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD

object Chapter6 extends App {
  private val spark = SparkSession.builder()
    .appName("Chapter6")
    .master("local[*]")
    .getOrCreate()
    val nums = spark.sparkContext.parallelize(1 to 1000)
    var counter = 0
    nums.foreach(x => counter += x)
    println(s"Counter = $counter")
    counter = 0
    println(s"Counter = $counter")
    nums.foreach(x => counter += 1)
    println(s"Counter = $counter")

    val accum = spark.sparkContext.longAccumulator("MyAccumulator")
    nums.foreach(x => accum.add(x))
    println(s"Accum = ${accum.value}")

}
