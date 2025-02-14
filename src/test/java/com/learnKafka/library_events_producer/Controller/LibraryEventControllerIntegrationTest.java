package com.learnKafka.library_events_producer.Controller;

import com.learnKafka.library_events_producer.domain.LibraryEvent;
import com.learnKafka.library_events_producer.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryEventControllerIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;
    @Test
    void postLibraryEvent() {
        //given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON.toString());
        var httpEntity = new HttpEntity<>(TestUtil.libraryEventRecord(), httpHeaders);

        //when
        var responseEntity = restTemplate
                .exchange("/v1/libraryevent", HttpMethod.POST,
                        httpEntity, LibraryEvent.class);


        //then
        assertEquals( HttpStatus.CREATED, responseEntity.getStatusCode());
        //assertEquals(2,2);

    }
}