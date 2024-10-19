import org.apache.spark.sql.SparkSession

object Chapter3Reduce extends App {
  private val spark = SparkSession.builder()
    .appName("Chapter3")
    .master("local[*]")
    .getOrCreate()

    val numbers = Seq(1,2,3,4,5)
    val rdd = spark.sparkContext.parallelize(numbers)
    val sum = rdd.reduce((a,b) => a + b)
    // (1,2) -> 1 + 2 = 3 E1
    // (3, 3) -> 3 + 3 = 6 E2
    // (6,4) -> 6 + 4 = 10 E3
    // (10, 5) -> 10 + 5 = 15 E4
    /*
        E1: Max(1,2): N1, Max(3,4): N2, Max(5): N3
        E2: (2,4): N1 , 5: N3
        E3: (4,5): N1 -> 5
    */
    println(sum)
    // 1000 documents -> document -> 1M words
    // Splunk
    // logger=ERROR, 
    // .log -> 1M log texts | | | |
    // log=ERROR,INFO,WARN thread=Main,SUbsequent

}
