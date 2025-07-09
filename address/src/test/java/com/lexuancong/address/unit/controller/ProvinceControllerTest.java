package com.lexuancong.address.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lexuancong.address.controller.ProvinceController;
import com.lexuancong.address.service.ProvinceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProvinceController.class)
@AutoConfigureMockMvc(addFilters = false)

public class ProvinceControllerTest {
    @MockBean
    private ProvinceService provinceService;
    @Autowired
    private MockMvc mockMvc;

    private ObjectWriter objectWriter;
    @BeforeEach
    public void setUp() {
        this.objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    }
}
