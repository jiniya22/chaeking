package com.chaeking.api.config;

import com.chaeking.api.domain.enumerate.Sex;
import com.chaeking.api.domain.value.Ttt;
import com.chaeking.api.domain.value.UserValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeTest() {
//        UserValue.Req.Creation user1 = new UserValue.Req.Creation("jini@jiniworld.me", "123", "지니", LocalDate.of(1992, 11, 11), Sex.F);
        UserValue.Req.Creation user1 = new UserValue.Req.Creation("jini@jiniworld.me", "123", "지니", "1992-11-11", "F");
        try {
            System.out.println(objectMapper.writeValueAsString(user1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deserializeTest() throws JsonProcessingException {
        String jsonString = """
        {
            "email": "sohee@jiniworld.me",
            "password": "222",
            "name": "김소희",
            "birth_date": "1984-02-12",
            "sex": "F"
        }
        """;
//        UserValue.Req.Creation user = jackson2ObjectMapper.readerFor(UserValue.Req.Creation.class).readValue(jsonString);
        Ttt user = objectMapper.readerFor(Ttt.class).readValue(jsonString);
        System.out.println(user);
    }

}