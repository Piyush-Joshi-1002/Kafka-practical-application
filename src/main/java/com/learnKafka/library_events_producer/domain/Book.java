package com.learnKafka.library_events_producer.domain;

public record Book(
        Integer bookId,
        String bookName,
        String bookAuthor
) {

}
