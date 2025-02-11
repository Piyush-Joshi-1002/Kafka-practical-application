package com.learnKafka.library_events_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnKafka.library_events_producer.domain.LibraryEvent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class LibraryEventsProducer {

    @Value("${spring.kafka.topic}")
    private  String topic;

    private final KafkaTemplate<Integer,String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public LibraryEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }




    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value =objectMapper.writeValueAsString(libraryEvent);


        // 1. blocking call - get metadata about the kafka cluster (very first time)
        // 2. send message happens - returns a CompletableFuture.
        var completableFuture = kafkaTemplate.send(topic,key,value);
        return completableFuture
                .whenComplete(((sendResult, throwable) -> {
                    if(throwable != null){
                        handleFaliure(key,value,throwable);
                    }
                    else{
                        handleSuccess(key,value,sendResult);
                    }
                }));
    }
    public SendResult<Integer, String> sendLibraryEvent_SyncApproach(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        var key = libraryEvent.libraryEventId();
        var value =objectMapper.writeValueAsString(libraryEvent);


        // 1. blocking call - get metadata about the kafka cluster (very first time)
        // 2. by using .get() method it block and wait until the message is sent to the kafka
        var sendResult = kafkaTemplate.send(topic,key,value)
                //.get(); either use this one or we can use with timeout like below
                        .get(3, TimeUnit.SECONDS);
        handleSuccess(key,value,sendResult);
        return sendResult;
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Message Sent Successfully for the key : {} and the value : {} , partition is {}",
                key,value,sendResult.getRecordMetadata().partition());
    }

    private void handleFaliure(Integer key, String value, Throwable ex) {
        log.error("Error sending the message and the exception is {}", ex.getMessage(), ex);
    }
}
