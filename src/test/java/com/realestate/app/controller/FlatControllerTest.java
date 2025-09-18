package com.realestate.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.app.dto.FlatDTO;
import com.realestate.app.dto.FlatPageDTO;
import com.realestate.app.model.ComplexEntity;
import com.realestate.app.model.FlatEntity;
import com.realestate.app.repository.ComplexRepository;
import com.realestate.app.repository.FlatRepository;
import com.realestate.app.sort.FlatSortBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class FlatControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3"))
            .withDatabaseName("testDB")
            .withUsername("test-user")
            .withPassword("test-password");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private ComplexRepository complexRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private long complexId;

    @BeforeEach
    void setUp() {
        ComplexEntity complex = createComplex();
        complexId = complex.getId();
    }

    @AfterEach
    void afterEach() {
        flatRepository.deleteAll();
        complexRepository.deleteAll();
    }

    @Test
    void testShowFlat() throws Exception {
        FlatEntity flat = createFlat();
        long flatId = flat.getId();

        MvcResult result = mockMvc.perform(get("/api/flats/" + flatId))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        FlatDTO actualResponse = objectMapper.readValue(content, FlatDTO.class);

        FlatEntity expectedEntity = flatRepository.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Нет такого объекта"));

        assertThat(actualResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntity);
    }

    @Test
    void testShowNonExistingFlat() throws Exception {
        long nonExistingId = 9999;
        mockMvc.perform(get("/api/flats/" + nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testIndex() throws Exception {
        createFlat();
        createFlat();
        MvcResult result = mockMvc.perform(get("/api/flats")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        FlatPageDTO actualResponse = objectMapper.readValue(content, FlatPageDTO.class);

        assertThat(actualResponse.content().size()).isEqualTo(2);
    }

    @Test
    void testIndexWithFilter() throws Exception {
        FlatEntity flat = createFlat();
        createFlat();

        MvcResult result = mockMvc.perform(get("/api/flats")
                        .param("building", flat.getBuilding())
                        .param("flatNumber", flat.getNumber())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        FlatPageDTO actualResponse = objectMapper.readValue(content, FlatPageDTO.class);

        assertThat(actualResponse.content().size()).isEqualTo(1);

        FlatEntity expectedEntity = flatRepository.findById(flat.getId())
                .orElseThrow(() -> new RuntimeException("Нет такого объекта"));
        FlatDTO actualFlatDTO = actualResponse.content().getFirst();

        assertThat(actualFlatDTO).usingRecursiveComparison().isEqualTo(expectedEntity);
    }

    @Test
    void testIndexWithSortByTotalPriceDesc() throws Exception {
        FlatEntity flat1 = createFlat(15000000);
        FlatEntity flat2 = createFlat(20000000);
        FlatEntity flat3 = createFlat(12000000);

        List<FlatEntity> flatEntities = Arrays.asList(flat1, flat2, flat3);
        flatEntities.sort(Comparator.comparingInt(FlatEntity::getActualPriceTotal).reversed());

        MvcResult result = mockMvc.perform(get("/api/flats")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortProperty", String.valueOf(FlatSortBy.ACTUAL_PRICE_TOTAL))
                        .param("sortDirection", String.valueOf(Sort.Direction.DESC)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        FlatPageDTO actualResponse = objectMapper.readValue(content, FlatPageDTO.class);

        assertThat(actualResponse.content().size()).isEqualTo(3);
        List<FlatDTO> actualFlatDTOs = actualResponse.content();

        assertThat(actualFlatDTOs.get(0)).usingRecursiveComparison().isEqualTo(flatEntities.get(0));
        assertThat(actualFlatDTOs.get(1)).usingRecursiveComparison().isEqualTo(flatEntities.get(1));
        assertThat(actualFlatDTOs.get(2)).usingRecursiveComparison().isEqualTo(flatEntities.get(2));
    }

    private ComplexEntity createComplex() {
        ComplexEntity complex = new ComplexEntity();
        complex.setName("Комплекс " + UUID.randomUUID());
        complex.setCity("Москва");
        complex.setDeveloper("Строительная компания " + UUID.randomUUID());
        complexRepository.save(complex);

        return complex;
    }

    private FlatEntity createFlat() {
        return createFlat(ThreadLocalRandom.current().nextInt(6000000, 30000000));
    }

    private FlatEntity createFlat(int actualPriceTotal) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int randomRooms = rand.nextInt(1, 5);
        int totalArea = IntStream.generate(() -> rand.nextInt(10, 30))
                .limit(randomRooms).sum();
        FlatEntity flat = new FlatEntity();
        flat.setComplexId(complexId);
        flat.setBuilding(String.valueOf(rand.nextInt(1, 10000)));
        flat.setNumber(String.valueOf(rand.nextInt(1, 10000)));
        flat.setFloor(rand.nextInt(1, 21));
        flat.setRooms(randomRooms);
        flat.setAreaTotal(totalArea);
        flat.setActualPricePerM2(actualPriceTotal / totalArea);
        flat.setActualPriceTotal(actualPriceTotal);
        flatRepository.save(flat);

        return flat;
    }
}