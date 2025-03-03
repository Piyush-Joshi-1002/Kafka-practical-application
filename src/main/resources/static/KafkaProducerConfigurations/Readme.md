
# Kafka Producer Configuration Documentation (Version 0.10.0)

This documentation provides detailed configuration settings for Kafka producers in version 0.10.0. It helps users configure Kafka producers to manage various aspects like message delivery, retries, batching, and compression.

## Key Configuration Settings

1. **Message Delivery**
    - Controls how messages are sent to Kafka.
    - Example: The `"acks"` setting defines the level of acknowledgment required from brokers to confirm message delivery.

2. **Retries**
    - Determines how many times a producer will retry sending a message if it fails.
    - Example: The `"retries"` setting specifies the number of retries on failure.

3. **Batching**
    - Allows the producer to batch multiple messages together, optimizing performance by reducing the number of network requests.
    - Example: The `"batch.size"` setting controls the maximum size of a batch.

4. **Compression**
    - Compresses messages to reduce bandwidth usage, improving efficiency.
    - Example: The `"compression.type"` setting specifies the compression algorithm (e.g., "gzip", "snappy").

For more detailed settings and configurations, visit [Kafka Producer Configurations](https://kafka.apache.org/0100/documentation.html#producerconfigs).


# Retain/Recover Failed Records in Kafka

This document explains strategies for handling failed Kafka records in a `library-events` topic due to producer misconfigurations. Two options are presented: using a recovery topic and using a database with a scheduler.

## Option 1: Using a Recovery Topic

### Workflow:
1. **Client Request**: The client sends a request to the API.
2. **Processing**: The API forwards the request to the Kafka Producer.
3. **Failure Handling**:
   - If the Kafka Producer encounters a `RuntimeException`, the record is not published to `library-events`.
   - Instead, the failed record is sent to a `recovery-topic`.
4. **Recovery Process**:
   - A Kafka Consumer reads from the `recovery-topic`.
   - The consumer retries sending the failed records to the Kafka Producer.
   - If successful, the record is finally published to `library-events`.

### Advantages:
- Provides an automatic retry mechanism.
- Retains failed records in Kafka, avoiding data loss.

---

## Option 2: Using a Database and Scheduler

### Workflow:
1. **Client Request**: The client sends a request to the API.
2. **Processing**: The API forwards the request to the Kafka Producer.
3. **Failure Handling**:
   - If the Kafka Producer encounters a `RuntimeException`, the record is not published to `library-events`.
   - Instead, the failed record is stored in a database.
4. **Recovery Process**:
   - A scheduler periodically checks the database for failed records.
   - The scheduler attempts to reprocess and send these records back to the Kafka Producer.
   - If successful, the records are published to `library-events`.

### Advantages:
- Ensures persistent storage of failed records.
- Allows controlled and scheduled reprocessing.
- Avoids overloading Kafka with immediate retries.

---

## Conclusion

Both approaches effectively handle failed records, but they cater to different use cases:
- **Use Option 1 (Recovery Topic)** when you need real-time retry mechanisms within Kafka.
- **Use Option 2 (Database + Scheduler)** when you require persistent storage and scheduled retries for better control.

