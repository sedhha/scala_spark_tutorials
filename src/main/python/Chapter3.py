from pyspark.sql import SparkSession
import time

# Initialize SparkSession
spark = SparkSession.builder \
    .appName("Chapter3") \
    .master("local[*]") \
    .getOrCreate()

# Function to measure time taken for operations
def time_it(largeNumbers, identifier):
    start_time = time.time()

    # First action: Reduce to compute the sum
    sum_value = largeNumbers.reduce(lambda a, b: a + b)
    print(f"Sum: {sum_value}")

    # Second action: Count the number of elements
    count_value = largeNumbers.count()
    print(f"Count: {count_value}")

    # Calculate the average
    average = sum_value / count_value
    print(f"Average: {average}")

    end_time = time.time()
    print(f"[{identifier}] Time taken: {end_time - start_time} seconds")

# Create the base RDD once
nums = spark.sparkContext.parallelize(range(1, 100000001))

# RDDs for with and without caching
largeNumbersWithCache = nums.filter(lambda x: x > 500000).cache()
largeNumbersWithoutCache = nums.filter(lambda x: x > 500000)

# Trigger an action to cache largeNumbersWithCache
largeNumbersWithCache.count()

print("========================= With Caching ================================")
for _ in range(5):
    time_it(largeNumbersWithCache, "With Caching")
    print("Expensive Operation complete\n================================")

print("====================== Without Caching ================================")
for _ in range(5):
    time_it(largeNumbersWithoutCache, "Without Caching")
    print("Expensive Operation complete\n================================")

# Stop the Spark session
spark.stop()
