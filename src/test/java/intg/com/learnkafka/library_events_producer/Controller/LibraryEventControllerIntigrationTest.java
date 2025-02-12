package intg.com.learnkafka.library_events_producer.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryEventControllerIntigrationTest {



    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void postLibraryEvent(){

    }
}