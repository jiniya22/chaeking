package com.chaeking.api.config;

import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.util.cipher.AESCipher;
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
        String secretKey = "A37aXdxH6gwTySajLe8eZWNvyC2yuZVB";
        String password = AESCipher.encrypt("jini", secretKey);
//        UserValue.Req.Creation user1 = new UserValue.Req.Creation("jini@jiniworld.me", "123", "지니", Sex.F);
        UserValue.Req.Creation user1 = new UserValue.Req.Creation("jini@jiniworld.me", password, secretKey,"지니", "F", false, false);
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
            "password": "3wqPrWol1T3h/3U+w2abGw==",
            "secret_key": "A37aXdxH6gwTySajLe8eZWNvyC2yuZVB",
            "name": "김소희",
            "sex": "F"
        }
        """;
        UserValue.Req.Creation user = objectMapper.readerFor(UserValue.Req.Creation.class).readValue(jsonString);
        System.out.println(user);
    }

}