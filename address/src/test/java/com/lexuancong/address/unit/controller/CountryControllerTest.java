package com.lexuancong.address.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lexuancong.address.controller.CountryController;
import com.lexuancong.address.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CountryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CountryControllerTest {
    @MockBean
    private CountryService countryService;

    private final MockMvc mockMvc;

    public CountryControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
    private ObjectWriter objectWriter;

    @BeforeEach
    public void setUp() {
        this.objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

    }


}
