package com.senpare.authservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.senpare.authservice.BaseTest;


public abstract class BaseIntTest<T> extends BaseTest<T> {

    protected static final String BASE_URL = "/api/v1";
    protected static final JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule()) // we need this to support serialization of Java 8 date/time classes
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
    protected final T model;
    @Autowired
    protected MockMvc mockMvc;
    protected String expectedModel;


    public BaseIntTest(T model) {
        this.model = model;
    }

    @BeforeEach
    void setUp() throws Throwable {
        expectedModel = jsonMapper.writeValueAsString(model);
    }


}
