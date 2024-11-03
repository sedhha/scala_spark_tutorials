# Chapter 5: RDD Operations

In Spark, **Resilient Distributed Datasets (RDDs)** provide a powerful abstraction for processing and managing data across a cluster in a distributed, fault-tolerant way. They support two primary categories of operations: **transformations** and **actions**.

### Transformations and Actions in RDDs

1. **Transformations**:

   - Transformations create new RDDs from existing ones. However, they don’t compute their results immediately. Instead, they define a sequence of transformations to be applied to an RDD and only execute them when an action requires a result. This is known as **lazy evaluation**.
   - Examples of transformations include:
     - **map**: Applies a function to each element in the RDD and returns a new RDD with the results. For example, `rdd.map(x => x * 2)` doubles each element in the RDD.
     - **filter**: Filters elements based on a provided condition, returning a new RDD with elements that satisfy this condition.
     - **flatMap**: Similar to `map`, but each element can be mapped to multiple elements (flattening nested structures).
     - **reduceByKey**: An optimized transformation used with key-value pairs that applies a reduction function across values with the same key, creating an RDD with combined results but keeping it distributed.

2. **Actions**:

   - Actions trigger the execution of the transformations applied to RDDs, bringing the results back to the driver program. Actions compute the transformations defined on an RDD and return values to the driver.
   - Examples of actions include:

     - **reduce**: Aggregates all the elements in an RDD by applying a specified function and returns a single result. For instance, `rdd.reduce((x, y) => x + y)` will add all elements together.
     - **collect**: Brings all the elements of an RDD to the driver as an array.
     - **count**: Counts the number of elements in an RDD.
     - **take**: Retrieves a specified number of elements from the RDD.

   - **reduceByKey vs. reduce**: While `reduce` applies a function across the entire RDD and returns a single result to the driver, `reduceByKey` is a distributed transformation that aggregates values only within specific keys, returning a distributed dataset.

### Lazy Evaluation in Spark

All transformations in Spark are lazy, meaning they do not execute immediately. Spark maintains a lineage graph of all transformations that apply to an initial dataset (e.g., a file or a base RDD). When an action is called, Spark uses this lineage to optimize the execution plan and run transformations efficiently, minimizing resource usage. For example, if a `map` transformation is followed by a `reduce` action, Spark understands that only the final `reduce` output needs to be returned, not intermediate results.

### RDD Persistence

By default, Spark recomputes the transformations each time an action is called on an RDD. However, for cases where an RDD will be used multiple times, **persistence** or **caching** can improve performance by storing the RDD in memory. This prevents Spark from recalculating the RDD each time an action is performed, speeding up repeated computations significantly. The `persist` method allows flexible storage options:

- **Memory-only storage** (the default with `cache`), where RDD data is stored in memory for quick access.
- **Disk storage** when the RDD is too large for memory, keeping it accessible across cluster nodes.
- **Replicated storage** across multiple nodes, increasing fault tolerance by storing copies of the RDD.

By leveraging these operations and optimization techniques, Spark enables scalable and efficient data processing across distributed systems.

In Spark, `persist` and `cache` are both used to store RDDs in memory, allowing for faster access if the RDD is used multiple times in subsequent operations. While they are similar in function, there are some differences in their behavior and flexibility.

### 1. `cache` Method

The `cache` method is a shorthand for `persist` with a specific storage level. By default, `cache` stores the RDD in **memory only**. If the RDD is too large to fit in memory, Spark may recompute parts of the RDD that do not fit when they are needed.

```scala
val rdd = spark.sparkContext.textFile("sample.txt")
val wordsRDD = rdd.flatMap(line => line.split(" "))

// Cache the RDD in memory only
wordsRDD.cache()
val wordCount1 = wordsRDD.count()    // First action triggers computation and caches result
val wordCount2 = wordsRDD.count()    // Second action reads directly from cache
```

In this example:

- The first action (`count`) triggers the RDD computation and stores `wordsRDD` in memory.
- The second action (`count`) uses the cached data, making it faster.

### 2. `persist` Method

The `persist` method provides more **flexibility** compared to `cache`, as it allows you to specify different **storage levels** for the RDD. For instance, you can store the RDD in memory only, on disk, or as a combination of both. Some common storage levels include:

- **MEMORY_ONLY**: Stores RDD in memory only (same as `cache`).
- **MEMORY_AND_DISK**: Stores RDD in memory, and spills to disk if memory is insufficient.
- **DISK_ONLY**: Stores RDD only on disk.
- **MEMORY_ONLY_SER**: Stores RDD in serialized format in memory, reducing memory usage.

#### Example of `persist` with Different Storage Levels

```scala
val rdd = spark.sparkContext.textFile("sample.txt")
val wordsRDD = rdd.flatMap(line => line.split(" "))

// Persist with MEMORY_AND_DISK storage level
wordsRDD.persist(org.apache.spark.storage.StorageLevel.MEMORY_AND_DISK)
val wordCount1 = wordsRDD.count()    // First action triggers computation and caches result
val wordCount2 = wordsRDD.count()    // Second action reads from memory or disk if needed
```

In this example:

- `persist` with `MEMORY_AND_DISK` storage level stores the RDD in memory if there’s enough space; otherwise, it spills the data to disk.
- If the RDD is too large to fit entirely in memory, Spark avoids recomputation by reading parts of the RDD from disk.

### Key Differences Between `persist` and `cache`

| Feature          | `cache`                               | `persist`                                                    |
| ---------------- | ------------------------------------- | ------------------------------------------------------------ |
| Default Behavior | Stores the RDD in **memory only**     | Requires a specified storage level                           |
| Flexibility      | Less flexible (only stores in memory) | More flexible (can store in memory, disk, or both)           |
| Usage            | Shorthand for `persist(MEMORY_ONLY)`  | Use for custom storage levels                                |
| Example Use Case | Suitable for RDDs that fit in memory  | Useful when memory constraints require other storage options |

### When to Use Each

- Use `cache` when you’re sure the RDD can fit into memory and no specific storage customization is needed.
- Use `persist` if you want more control over storage levels or if the RDD might be too large for memory and needs disk-based persistence.

In summary, `cache` is a convenient shortcut for in-memory storage, while `persist` offers a broader range of options for handling storage constraints.

1. **Lazy Evaluation of Transformations**:

   - When you define `lineLengths` with `lines.map(s => s.length)`, Spark doesn’t compute it immediately. Instead, it waits until an action (like `reduce`) triggers the computation. This lazy evaluation enables Spark to optimize execution and avoid unnecessary computations.

2. **Triggering Computation with `reduce`**:

   - When `reduce((a, b) => a + b)` is called, Spark performs the following:
     - It reads the data from `data.txt` into the `lines` RDD.
     - It applies the `map` transformation to calculate the length of each line, resulting in `lineLengths`.
     - Finally, it reduces the values in `lineLengths` by summing them up to get `totalLength`.
   - Since this is the first time `lineLengths` is computed, if `lineLengths.persist()` was called before `reduce`, the computed `lineLengths` RDD would be stored in memory after the `reduce` action finishes.

3. **Effect of `persist()`**:

   - When `persist()` is applied to `lineLengths` before the `reduce` action, Spark stores the computed `lineLengths` in memory after the first computation. This stored RDD allows future actions or transformations on `lineLengths` to read directly from memory, bypassing the need to recompute `lines.map(s => s.length)` from scratch.
   - **Important**: `persist()` only affects subsequent actions. The initial `reduce` action still performs the full computation the first time because `lineLengths` has not yet been computed.

4. **Reusing `lineLengths`**:
   - If another action (e.g., `lineLengths.count()`) is called after the `reduce`, Spark will use the in-memory version of `lineLengths`, avoiding the need to re-read `data.txt` and recompute line lengths.

In summary, `persist()` saves `lineLengths` in memory **after the first computation** triggered by `reduce`. If another action uses `lineLengths` later, Spark will use the in-memory RDD directly, making future operations faster by avoiding recomputation. The `reduce` itself still performs the computation in full the first time, but subsequent actions on `lineLengths` are faster due to caching.
