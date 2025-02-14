package com.learnKafka.library_events_producer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnKafka.library_events_producer.domain.LibraryEvent;
import com.learnKafka.library_events_producer.producer.LibraryEventsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping
@Slf4j
public class LibraryEventController {


    private final LibraryEventsProducer libraryEventsProducer;

    public LibraryEventController(LibraryEventsProducer libraryEventsProducer) {
        this.libraryEventsProducer = libraryEventsProducer;
    }


    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(
            @RequestBody  LibraryEvent libraryEvent
    ) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        log.info("libraryEvent: {}",libraryEvent);

        //libraryEventsProducer.sendLibraryEvent_SyncApproach(libraryEvent);
        libraryEventsProducer.sendLibraryEvent_WithObject(libraryEvent);

        log.info("After sending libraryEvent : ");

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
