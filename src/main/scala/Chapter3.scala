import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD

object Chapter3 {
  private val spark = SparkSession.builder()
    .appName("Chapter3")
    .master("local[*]")
    .getOrCreate()

  def time_it(largeNumbers: RDD[Int], identifier: String): Unit = {
    val startTime = System.currentTimeMillis

    // First action: Reduce to compute the sum
    val sum = largeNumbers.reduce(_ + _)
    println(s"Sum: $sum")

    // Second action: Count the number of elements
    val count = largeNumbers.count()
    println(s"Count: $count")

    // Calculate the average
    val average = sum / count.toDouble
    println(s"Average: $average")

    val endTime = System.currentTimeMillis
    println(s"[$identifier] Time taken: ${endTime - startTime} ms")
  }

  // Create the base RDD once
  val nums = spark.sparkContext.parallelize(1 to 1_000_000_00)

  // RDDs for with and without caching
  val largeNumbersWithCache = nums.filter(_ > 500000).cache()
  val largeNumbersWithoutCache = nums.filter(_ > 500000)

  // Trigger an action to cache largeNumbersWithCache
  largeNumbersWithCache.count()

  println("========================= With Caching ================================")
  for (_ <- 0 until 5) {
    time_it(largeNumbersWithCache, "With Caching")
    println("Expensive Operation complete\n================================")
  }

  println("====================== Without Caching ================================")
  for (_ <- 0 until 5) {
    time_it(largeNumbersWithoutCache, "Without Caching")
    println("Expensive Operation complete\n================================")
  }

  spark.stop()
}
