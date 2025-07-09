package com.lexuancong.address.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lexuancong.address.controller.CountryController;
import com.lexuancong.address.service.CountryService;
import com.lexuancong.address.viewmodel.country.CountryGetVm;
import com.lexuancong.address.viewmodel.country.CountryPagingVm;
import com.lexuancong.address.viewmodel.country.CountryPostVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CountryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CountryControllerTest {
    @MockBean
    private CountryService countryService;

    @Autowired
    private MockMvc mockMvc;


    private ObjectWriter objectWriter;

    @BeforeEach
    public void setUp() {
        this.objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();


    }

    // pass
    @Test
    void testGetCountriesPaging_whenValidRequest_thenReturnOk() throws Exception {
        // Mock response
        CountryPagingVm mockPagingVm = new CountryPagingVm(List.of(), 1, 10, 0, 10, true);
        given(countryService.getCountriesPaging(1, 10)).willReturn(mockPagingVm);

        mockMvc.perform(get("/address/management/countries/paging")
                        .param("pageIndex", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    // pass
    @Test
    void testGetCountries_whenValidRequest_thenReturnOk() throws Exception {
        List<CountryGetVm> mockResult = List.of(new CountryGetVm(1L, "VietNam"));
        given(countryService.getCountries()).willReturn(mockResult);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/address/management/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("VietNam"))
                .andExpect(jsonPath("$[0].id").value(1L));

    }

    // pass
    @Test
    void testCreateCountry_whenValidRequest_thenReturnOk() throws Exception {
        CountryGetVm mockResult = new CountryGetVm(1L, "VietNam");
        CountryPostVm countryPostVm = new CountryPostVm("VietNam");
        String bodyRequest = this.objectWriter.writeValueAsString(countryPostVm);
        given(countryService.createCountry(countryPostVm)).willReturn(mockResult);
        this.mockMvc.perform(post("/address/management/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest)
        )
                .andExpect(status().isCreated());


    }


    // pass
    @Test
    void testCreateCountry_WhenNameIsBlank_thenReturnBadRequest() throws Exception {
        CountryGetVm mockResult = new CountryGetVm(1L, "VietNam");
        CountryPostVm countryPostVm = new CountryPostVm("");
        String bodyRequest = this.objectWriter.writeValueAsString(countryPostVm);
        this.mockMvc.perform(post("/address/management/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isBadRequest());

    }



    // pass
    @Test
    void testUpdateCountry_WhenValidRequest_thenReturnOk() throws Exception {
        CountryPostVm countryPostVm = new CountryPostVm("VietNam");
        String bodyRequest = this.objectWriter.writeValueAsString(countryPostVm);
        this.mockMvc.perform(put("/address/management/countries/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest)
        )
                .andExpect(status().isNoContent());
    }

    // pass
    @Test
    void testUpdateCountry_WhenNameIsBlank_thenReturnBadRequest() throws Exception {
        CountryPostVm countryPostVm = new CountryPostVm("");
        String bodyRequest = this.objectWriter.writeValueAsString(countryPostVm);
        this.mockMvc.perform(put("/address/management/countries/1")
        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testDelete_whenValidRequest_thenReturnOk() throws Exception {
        this.mockMvc.perform(delete("/address/management/countries/1")
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }














}
