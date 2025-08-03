package io.github.Piotr7421.giftapi.strategy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import io.github.Piotr7421.giftapi.repository.KidRepository;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class KidStrategyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KidRepository kidRepository;

    @BeforeEach
    void setUp() {
        kidRepository.deleteAll();
    }

    @Test
    void createKidViaStrategy_ShouldReturn201AndPersistKid() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of(
                        "type", "BOY",
                        "params", Map.of(
                                "firstName", "Tim",
                                "lastName", "Lee",
                                "birthDate", "2014-05-01",
                                "pantsLength", "23"
                        )
                ));

        mockMvc.perform(post("/api/strategy/kids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}