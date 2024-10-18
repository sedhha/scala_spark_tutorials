# Chapter 2 Getting Started

Here we will run some basic commands to get started with Spark.

## Step 1: Open [GettingStarted.scala](../main/scala/Chapter2.scala)

### Reading a text file

In this example we will learn about:
- RDD and DataFrames in Spark
RDD (Resilient Distributed Dataset) and DataFrames are two important abstractions:
RDD is the low-level, immutable distributed collection of objects. It offers more control over data processing and can handle unstructured data, but it doesn’t have optimizations like Catalyst (query optimizer) or Tungsten (execution engine).
DataFrames are high-level APIs built on top of RDDs that offer optimizations, a SQL-like interface, and support for structured data (i.e., columns with types). They are easier to use and more performant due to the optimizations Spark applies under the hood.
- When to use RDD and when to use DataFrames
RDDs are useful when:
 - You need fine-grained control over low-level transformations. 
 - You are working with unstructured or semi-structured data. 
 - You prefer strong type safety and are comfortable with Scala's functional programming constructs.

DataFrames are better when:
 - You are working with structured data, like rows and columns. 
 - You want to use SQL queries or benefit from performance optimizations such as Catalyst. 
 - You want to use built-in Spark functions for easy data manipulation.


- How to read a text file in Spark
- How to convert RDD to DataFrame and vice versa
- Line Count of given text file
- Word Count of given text file


### Understanding Spark Session

```scala
private val spark = SparkSession.builder()
    .appName("Chapter2")
    .master("local[*]")
    .getOrCreate()
```

`SparkSession.builder()`

SparkSession: This is the main entry point for creating a Spark session. Since Spark 2.0, SparkSession is a unified API for working with Spark functionalities like RDDs, DataFrames, and Datasets.
It encapsulates both the SQLContext and HiveContext APIs used in earlier versions of Spark.

`.builder()`: This initializes a builder object that will be used to configure and create a SparkSession. The builder() method provides a fluent API to configure various settings (like app name, master URL, etc.) before creating a Spark session instance.

`.appName("Chapter2")`: .appName(): Useful for logging and tracking purposes in the Spark UI and logs. In this case, name is "Chapter2", but it can be anything meaningful based on the context of your Spark job. This is especially useful when monitoring jobs on a Spark cluster or YARN cluster to identify your application.

`master("local[*]")`: `.master()`: This sets the master URL for the cluster manager. The master defines where the Spark driver should connect to launch the application. Spark supports several cluster managers, and the master URL determines where and how Spark will run the job.

`"local[*]"`: local means that Spark will run locally on the machine where the code is executed, not in a distributed cluster. `[*]` specifies the number of CPU cores Spark should use. In this case, the * means that Spark will use all available cores on the host machine. For example, if your machine has 4 cores, Spark will use all 4 cores for parallelism in your job. This is useful for local development and testing.
Other examples of `.master()` values:

- `"local[1]"`: Use only one thread (or core) to run the job.
- `"local[4]"`: Use exactly 4 threads (or cores) to run the job.
- `"yarn"`: The master URL would be set to yarn when running in yarn cluster. YARN is the resource management layer of the Hadoop ecosystem. It allows various data-processing engines, such as Spark, to use the same underlying cluster infrastructure for resource allocation and job execution. Essentially, YARN handles the allocation of system resources (CPU, memory) and scheduling jobs on the available cluster nodes.


`.getOrCreate()`: If a SparkSession has already been created in the current application context, calling .getOrCreate() will return that existing session rather than creating a new one. If there isn’t a SparkSession already running, this method will create a new one based on the configurations (such as appName and master) provided earlier.