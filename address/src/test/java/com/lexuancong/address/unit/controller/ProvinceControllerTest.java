package com.lexuancong.address.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lexuancong.address.controller.ProvinceController;
import com.lexuancong.address.service.ProvinceService;
import com.lexuancong.address.dto.province.ProvinceGetResponse;
import com.lexuancong.address.dto.province.ProvincePagingGetResponse;
import com.lexuancong.address.dto.province.ProvinceCreateRequest;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

    @Test
    public void testGetProvincesPaging_whenValidRequest_thenReturnOk() throws Exception {
        ProvincePagingGetResponse provincePagingGetResponse = new ProvincePagingGetResponse(List.of(), 1, 10, 0, 10, true);
        given(this.provinceService.getProvincesPaging(1,10,null)).willReturn(provincePagingGetResponse);
        mockMvc.perform(get("/address/management/provinces/paging")
                        .param("pageIndex", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());

    }

    @Test
    public void TestGetProvincesByCountryId_whenValidRequest_thenReturnOk() throws Exception {
        List<ProvinceGetResponse> provinceGetResponses = List.of(new ProvinceGetResponse(5L,"VietNam",5L,"country"));
        given(this.provinceService.getProvincesByCountryId(5L)).willReturn(provinceGetResponses);
        mockMvc.perform(get("/address/management/provinces/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void TestCreateProvince_whenValidRequest_thenReturnOk() throws Exception {
        ProvinceCreateRequest provinceCreateRequest = ProvinceCreateRequest.builder()
                .type("country")
                .countryId(1L)
                .name("Quang Tri")
                .build();
        String requestBody = this.objectWriter.writeValueAsString(provinceCreateRequest);
        ProvinceGetResponse provinceGetResponse = ProvinceGetResponse.builder()
                .countryId(1L)
                .build();
        given(this.provinceService.createProvince(provinceCreateRequest)).willReturn(provinceGetResponse);
        this.mockMvc.perform(post("/address/management/provinces")
        .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

    }


}
