package com.happio.application.post;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldThrowClientError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/post")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldCreatePost() throws Exception {
        String postBody = "{\"id\": \"4\", \"channelId\": \"10\", \"creatorId\": \"10\", \"content\": \"heey!\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/post")
                        .content(postBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();
        Assertions.assertEquals(result.getResponse().getContentAsString(), "Created");
    }


}