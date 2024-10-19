from pyspark.sql import SparkSession
from pyspark.sql.functions import col, explode, split, lower

# Initialize SparkSession
spark = SparkSession.builder.appName("Chapter2").master("local[*]").getOrCreate()

# Path to the text file
path_to_text_file = "./src/assets/sample.txt"

# Through RDDs
rdd_text = spark.sparkContext.textFile(path_to_text_file)
# Print the lines from the text file
# print("\n".join(rdd_text.collect()))

# Line count of the given file
line_count = rdd_text.count()
print(f"Line Count of the given file: {line_count}")

# Through DataFrame
df_text = spark.read.text(path_to_text_file)
df_text.show(truncate=False)

# Conversions

# Convert to DataFrame from RDD
df_from_rdd = rdd_text.toDF(["something_else"])
# df_from_rdd.show(truncate=False)

# Convert to RDD from DataFrame
rdd_from_df = df_text.rdd
# print("\n".join(map(str, rdd_from_df.collect())))

# Word Count using RDDs
rdd_word_count = rdd_text\
    .flatMap(lambda line: line.split(" "))\
        .map(lambda word: (word.lower(), 1))\
            .reduceByKey(lambda a, b: a + b)

# Print the word count
# print("\n".join(map(str, rdd_word_count.collect())))

# Word Count using DataFrame API
df_word_counts = df_text.select(
    explode(split(lower(col("value")), " ")).alias("word")
)

df_aggregation = df_word_counts.groupBy("word").count()
print("Word count with DataFrame API:")
df_aggregation.show(truncate=False)

# Stop the Spark session
spark.stop()
