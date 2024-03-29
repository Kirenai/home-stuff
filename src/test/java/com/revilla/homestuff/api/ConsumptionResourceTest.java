package com.revilla.homestuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revilla.homestuff.dto.ConsumptionDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.jwt.JwtTokenProvider;
import com.revilla.homestuff.service.ConsumptionService;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.ConsumptionServiceDataTestUtils;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

@WebMvcTest(value = ConsumptionResource.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenProvider.class)
})
@WithMockUser
class ConsumptionResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConsumptionService consumptionService;

    @MockBean
    private UserRepository userRepository;

    private final StringBuilder URL = new StringBuilder("/api/consumptions");

    private ConsumptionDto consumptionDtoOneMock;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        Long userIdOne = 1L;

        String username = "kirenai";
        String password = "kirenai";
        String firstName = "kirenai";
        String lastName = "kirenai";
        Byte age = 22;

        User userMockOne = UserServiceDataTestUtils.getUserMock(userIdOne, username,
                password, firstName, lastName, age);
        userMockOne.setRoles(List.of(RoleServiceDataTestUtils.getRoleMock(1L, RoleName.ROLE_ADMIN)));

        this.consumptionDtoOneMock = ConsumptionServiceDataTestUtils.getConsumptionDtoMock(1L, (byte) 5);

        Mockito.when(this.userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(userMockOne));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should return all consumptions")
    void getAllConsumptions() throws Exception {
        List<ConsumptionDto> consumptionDtoList = ConsumptionServiceDataTestUtils.getConsumptionDtoListMock();
        Mockito.when(this.consumptionService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(consumptionDtoList);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.toString())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value(consumptionDtoList.get(0).getUnit().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].unit").value(consumptionDtoList.get(1).getUnit().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].unit").value(consumptionDtoList.get(2).getUnit().intValue()));
    }

    @Test
    @DisplayName("Should return consumption by id")
    void getConsumptionById() throws Exception {
        Long consumptionId = 1L;
        Mockito.when(this.consumptionService.findOne(Mockito.anyLong(), Mockito.any()))
                .thenReturn(this.consumptionDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/").append(consumptionId).toString())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.unit").value(this.consumptionDtoOneMock.getUnit().intValue()));
    }

    @Test
    @DisplayName("Should create consumption")
    void createConsumption() throws Exception {
        Long nourishmentId = 1L;
        Long userId = 1L;
        Mockito.when(this.consumptionService.create(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ConsumptionDto.class), Mockito.any()))
                .thenReturn(this.consumptionDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .post(this.URL.append("/nourishment/").append(nourishmentId).append("/user/").append(userId).toString())
                .content(this.objectMapper.writeValueAsString(this.consumptionDtoOneMock))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.consumptionId").value(this.consumptionDtoOneMock.getConsumptionId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unit").value(this.consumptionDtoOneMock.getUnit().intValue()));
    }

}