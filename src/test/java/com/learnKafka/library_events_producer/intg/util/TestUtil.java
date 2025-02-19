package com.learnKafka.library_events_producer.intg.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnKafka.library_events_producer.domain.Book;
import com.learnKafka.library_events_producer.domain.LibraryEvent;
import com.learnKafka.library_events_producer.domain.LibraryEventType;

public class TestUtil {

    public static Book bookRecord(){

        return new Book(123, "Dilip","Kafka Using Spring Boot" );
    }

    public static Book bookRecordWithInvalidValues(){

        return new Book(null, "","Kafka Using Spring Boot" );
    }

    public static LibraryEvent libraryEventRecord(){

        return
                new LibraryEvent(null,
                        LibraryEventType.NEW,
                        bookRecord());
    }


    public static LibraryEvent libraryEventRecord_update(){

        return
                new LibraryEvent(1234,
                        LibraryEventType.UPDATE,
                        bookRecord());
    }

    public static LibraryEvent invalid_Id_LibraryEventRecord_update(){

        return
                new LibraryEvent(null,
                        LibraryEventType.UPDATE,
                        bookRecord());
    }


    public static LibraryEvent invalid_EventType_libraryEventRecord_update_(){

        return
                new LibraryEvent(1234,
                        LibraryEventType.NEW,
                        bookRecord());
    }


    public static LibraryEvent newLibraryEventRecordWithLibraryEventId(){

        return
                new LibraryEvent(123,
                        LibraryEventType.NEW,
                        bookRecord());
    }

    public static LibraryEvent libraryEventRecordUpdate(){

        return
                new LibraryEvent(123,
                        LibraryEventType.UPDATE,
                        bookRecord());
    }

    public static LibraryEvent libraryEventRecordUpdateWithNullLibraryEventId(){

        return
                new LibraryEvent(null,
                        LibraryEventType.UPDATE,
                        bookRecord());
    }

    public static LibraryEvent libraryEventRecordWithInvalidBook(){

        return
                new LibraryEvent(null,
                        LibraryEventType.NEW,
                        bookRecordWithInvalidValues());
    }

    public static LibraryEvent parseLibraryEventRecord(ObjectMapper objectMapper , String json){

        try {
            return  objectMapper.readValue(json, LibraryEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
