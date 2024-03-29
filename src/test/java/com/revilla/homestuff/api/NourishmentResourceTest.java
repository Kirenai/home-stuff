package com.revilla.homestuff.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.jwt.JwtTokenProvider;
import com.revilla.homestuff.service.NourishmentService;
import com.revilla.homestuff.util.enums.RoleName;
import com.revilla.homestuff.utils.AmountNourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.NourishmentServiceDataTestUtils;
import com.revilla.homestuff.utils.RoleServiceDataTestUtils;
import com.revilla.homestuff.utils.UserServiceDataTestUtils;
import com.revilla.homestuff.utils.dto.response.ApiResponseDataTestUtils;
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

@WebMvcTest(value = NourishmentResource.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtTokenProvider.class)
})
@WithMockUser
class NourishmentResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NourishmentService nourishmentService;

    @MockBean
    private UserRepository userRepository;

    private final StringBuilder URL = new StringBuilder("/api/nourishments");

    private NourishmentDto nourishmentDtoOneMock;

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

        this.nourishmentDtoOneMock = NourishmentServiceDataTestUtils.getNourishmentDtoMock(
                1L,
                "orange",
                "orange.png",
                "orange",
                AmountNourishmentServiceDataTestUtils
                        .getAmountNourishmentDtoMock((byte) 20));

        Mockito.when(this.userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(userMockOne));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should return nourishment list")
    void getNourishmentList() throws Exception {
        List<NourishmentDto> nourishmentDtoList = NourishmentServiceDataTestUtils.getNourishmentDtoList();
        Mockito.when(this.nourishmentService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(nourishmentDtoList);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.toString());

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nourishmentId").value(nourishmentDtoList.get(0).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(nourishmentDtoList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(nourishmentDtoList.get(0).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].imageUrl").value(nourishmentDtoList.get(0).getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isAvailable").value(nourishmentDtoList.get(0).getIsAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nourishmentId").value(nourishmentDtoList.get(1).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(nourishmentDtoList.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(nourishmentDtoList.get(1).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].imageUrl").value(nourishmentDtoList.get(1).getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isAvailable").value(nourishmentDtoList.get(1).getIsAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].nourishmentId").value(nourishmentDtoList.get(2).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(nourishmentDtoList.get(2).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value(nourishmentDtoList.get(2).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].imageUrl").value(nourishmentDtoList.get(2).getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].isAvailable").value(nourishmentDtoList.get(2).getIsAvailable()));
    }

    @Test
    @DisplayName("Should return nourishment by id")
    void getNourishmentById() throws Exception {
        Long nourishmentId = 1L;
        Mockito.when(this.nourishmentService.findOne(Mockito.anyLong(), Mockito.any()))
                .thenReturn(this.nourishmentDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/").append(nourishmentId).toString())
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nourishmentId").value(this.nourishmentDtoOneMock.getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.nourishmentDtoOneMock.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(this.nourishmentDtoOneMock.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl").value(this.nourishmentDtoOneMock.getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAvailable").value(this.nourishmentDtoOneMock.getIsAvailable()));
    }

    @Test
    @DisplayName("Should return error when nourishment not found")
    void getNourishmentByIdNotFound() throws Exception {
        Long nourishmentId = 1L;
        Mockito.when(this.nourishmentService.findOne(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new EntityNoSuchElementException("Nourishment not found with id: " + nourishmentId));

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/").append(nourishmentId).toString());

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should return list of available nourishments")
    void getAvailableNourishmentList() throws Exception {
        List<NourishmentDto> nourishmentDtoList = NourishmentServiceDataTestUtils.getNourishmentDtoList();
        Mockito.when(this.nourishmentService.findAllNourishmentByStatus(Mockito.anyBoolean()))
                .thenReturn(nourishmentDtoList);

        RequestBuilder request = MockMvcRequestBuilders
                .get(this.URL.append("/stock/").append(true).toString());

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nourishmentId").value(nourishmentDtoList.get(0).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(nourishmentDtoList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(nourishmentDtoList.get(0).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].imageUrl").value(nourishmentDtoList.get(0).getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isAvailable").value(nourishmentDtoList.get(0).getIsAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nourishmentId").value(nourishmentDtoList.get(1).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(nourishmentDtoList.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(nourishmentDtoList.get(1).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].imageUrl").value(nourishmentDtoList.get(1).getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isAvailable").value(nourishmentDtoList.get(1).getIsAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].nourishmentId").value(nourishmentDtoList.get(2).getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(nourishmentDtoList.get(2).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value(nourishmentDtoList.get(2).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].imageUrl").value(nourishmentDtoList.get(2).getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].isAvailable").value(nourishmentDtoList.get(2).getIsAvailable()));
    }

    @Test
    @DisplayName("Should create nourishment")
    void createNourishment() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Mockito.when(this.nourishmentService.create(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(NourishmentDto.class), Mockito.any()))
                .thenReturn(this.nourishmentDtoOneMock);

        RequestBuilder request = MockMvcRequestBuilders
                .post(this.URL.append("/user/").append(userId).append("/category/").append(categoryId).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.nourishmentDtoOneMock));

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nourishmentId").value(this.nourishmentDtoOneMock.getNourishmentId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.nourishmentDtoOneMock.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(this.nourishmentDtoOneMock.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl").value(this.nourishmentDtoOneMock.getImageUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAvailable").value(this.nourishmentDtoOneMock.getIsAvailable()));
    }

    @Test
    @DisplayName("Should update nourishment")
    void updateNourishment() throws Exception {
        Long nourishmentId = 1L;
        ApiResponseDto apiResponseDto = ApiResponseDataTestUtils
                .getApiResponseMock("updated successfully", Nourishment.class);
        Mockito.when(this.nourishmentService.update(Mockito.anyLong(), Mockito.any(NourishmentDto.class), Mockito.any()))
                .thenReturn(apiResponseDto);

        RequestBuilder request = MockMvcRequestBuilders
                .put(this.URL.append("/").append(nourishmentId).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.nourishmentDtoOneMock));

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(apiResponseDto.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(apiResponseDto.getMessage()));
    }

    @Test
    @DisplayName("Should delete nourishment")
    void deleteNourishment() throws Exception {
        Long nourishmentId = 1L;
        ApiResponseDto apiResponseDto = ApiResponseDataTestUtils
                .getApiResponseMock("deleted successfully", Nourishment.class);
        Mockito.when(this.nourishmentService.delete(Mockito.anyLong(), Mockito.any()))
                .thenReturn(apiResponseDto);

        RequestBuilder request = MockMvcRequestBuilders
                .delete(this.URL.append("/").append(nourishmentId).toString());

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(apiResponseDto.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(apiResponseDto.getMessage()));
    }

}