package com.learnKafka.library_events_producer.producer;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnKafka.library_events_producer.domain.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

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



    //Async Approach (preferable)
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

    //sync Approach
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


    //Async Approach without sending key, value directly.
    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent_WithObject(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value =objectMapper.writeValueAsString(libraryEvent);

        var producerRecord = buildProducerRecordWithHeader(key,value);
        // 1. blocking call - get metadata about the kafka cluster (very first time) => max.block.ms (By default 60 seconds)
        /* if all broker are down then we have (max.block.ms:60000 [60Second] ) propertie whcih determine after how much time
        *    blocking call  will release */
        // 2. send message happens - returns a CompletableFuture.

        var completableFuture = kafkaTemplate.send(producerRecord);
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

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value) {

        return new ProducerRecord<>(topic,key,value);
    }

    //with header
    private ProducerRecord<Integer, String> buildProducerRecordWithHeader(Integer key, String value) {
        List<Header> recordHeader = List.of(new RecordHeader("event-source","scanner".getBytes()));
        return new ProducerRecord<>(topic,null,key,value,recordHeader);
    }


    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Message Sent Successfully for the key : {} and the value : {} , partition is {}",
                key,value,sendResult.getRecordMetadata().partition());
    }

    private void handleFaliure(Integer key, String value, Throwable ex) {
        log.error("Error sending the message and the exception is {}", ex.getMessage(), ex);
    }
}
