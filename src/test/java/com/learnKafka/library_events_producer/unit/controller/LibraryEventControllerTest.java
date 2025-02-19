package com.learnKafka.library_events_producer.unit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnKafka.library_events_producer.controller.LibraryEventController;
import com.learnKafka.library_events_producer.domain.LibraryEvent;
import com.learnKafka.library_events_producer.intg.util.TestUtil;
import com.learnKafka.library_events_producer.producer.LibraryEventsProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(LibraryEventController.class)
class LibraryEventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LibraryEventsProducer libraryEventsProducer;

    @Autowired
    ObjectMapper objectMapper;
    @Test
    void postLibraryEvent() throws Exception {

        //given
        var json = objectMapper.writeValueAsString(TestUtil.libraryEventRecord());


        //when
        when(libraryEventsProducer.sendLibraryEvent_WithObject(isA(LibraryEvent.class)))
                .thenReturn(null);


        //then
        mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void postLibraryEvent_invalidValues_4xx() throws Exception {

        //given
        var json = objectMapper.writeValueAsString(TestUtil.libraryEventRecordWithInvalidBook());


        //when
        when(libraryEventsProducer.sendLibraryEvent_WithObject(isA(LibraryEvent.class)))
                .thenReturn(null);

        var expectedErrorMessage = "book.bookId - must not be null , book.bookName - must not be blank , ";
        //then
        mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));
    }

}