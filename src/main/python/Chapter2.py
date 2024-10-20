from pyspark.sql import SparkSession
from pyspark.sql.functions import col,explode,split,lower

if __name__ == "__main__":
    # Initialize SparkSession
    spark = SparkSession.builder\
        .appName("Chapter2")\
            .master("local[*]")\
                .getOrCreate()
                
    pathToTextFile = "./src/assets/sample.txt"
    
    # Read text file through RDDs
    rddText = spark.sparkContext.textFile(pathToTextFile)
    print("\n\n\n\n")
    # print(f"Number of lines in the text file: {rddText.count()}")
    
    # Read text file through DataFrame
    dfText = spark.read.text(pathToTextFile)
    dfText.show()
    
    # Conversions
    
    # Convert RDD to DataFrame
    dfConverted = rddText\
        .map(lambda x: (x,))\
            .toDF(["something_else"])
    dfConverted.show()
    
    # Convert DataFrame to RDD
    rddConverted = dfText.rdd
    # for line in rddConverted.collect():
    #     print(line[0])
    
    wordCount = rddText\
        .flatMap(lambda x: x.split(" "))\
            .map(lambda word: (word.lower(), 1))\
                .reduceByKey(lambda a, b: a+b)
    # print(wordCount.take(10))
    
    dfWordCounts = dfText.select(
        explode(
            split(
                lower(
                    col("value")
                ),
                " "
            )
        )\
            .alias("word")
    )
    
    dfAggregation = dfWordCounts\
        .groupBy("word")\
            .count()
    dfAggregation.show(
        dfAggregation.count(), 
        truncate = False
        )
    
        