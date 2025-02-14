package com.learnKafka.library_events_producer.unit;

import com.learnKafka.library_events_producer.domain.LibraryEvent;
import com.learnKafka.library_events_producer.intg.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryEventControllerTest {

    @Autowired
    TestRestTemplate restTemplate;
    @Test
    void postLibraryEvent() {

        assertEquals(2,2);

    }
}