package io.github.Piotr7421.giftapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import io.github.Piotr7421.giftapi.model.Gift;
import io.github.Piotr7421.giftapi.model.Kid;
import io.github.Piotr7421.giftapi.model.command.CreateGiftCommand;
import io.github.Piotr7421.giftapi.model.command.CreateKidCommand;
import io.github.Piotr7421.giftapi.model.command.UpdateGiftCommand;
import io.github.Piotr7421.giftapi.model.command.UpdateKidCommand;
import io.github.Piotr7421.giftapi.repository.GiftRepository;
import io.github.Piotr7421.giftapi.repository.KidRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class KidControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KidRepository kidRepository;
    @Autowired
    private GiftRepository giftRepository;

    private Kid kid;
    private Gift gift;

    @BeforeEach
    void setUp() {
        kid = Kid.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .birthDate(LocalDate.of(2012, 4, 21))
                .build();
        kidRepository.save(kid);

        gift = Gift.builder()
                .name("Lego")
                .price(199.99)
                .kid(kid)
                .build();
        giftRepository.save(gift);
    }

    @AfterEach
    void tearDown() {
        giftRepository.deleteAll();
        kidRepository.deleteAll();
    }

    @Test
    void findAll_ShouldReturnPagedKids() throws Exception {
        mockMvc.perform(get("/api/v1/kids"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(kid.getId()))
                .andExpect(jsonPath("$.content[0].firstName").value("Jan"))
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void findById_ShouldReturnKid() throws Exception {
        mockMvc.perform(get("/api/v1/kids/{kidId}", kid.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(kid.getId()))
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"));
    }

    @Test
    void findById_WhenKidNotFound_ShouldReturn404() throws Exception {
        int notExistingId = 999;
        mockMvc.perform(get("/api/v1/kids/{kidId}", notExistingId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Kid with id=" + notExistingId + " not found"));
    }

    @Test
    void create_ShouldCreateKid() throws Exception {
        CreateKidCommand command = new CreateKidCommand()
                .setFirstName("Anna")
                .setLastName("Nowak")
                .setBirthDate(LocalDate.of(2011, 1, 1));

        mockMvc.perform(post("/api/v1/kids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Nowak"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createKid_WhenMissingAllFields_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/kids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation error")));//
    }

    @Test
    void createKid_WhenInvalidFirstNamePattern_ShouldReturnBadRequest() throws Exception {
        Map<String, Object> body = Map.of(
                "firstName", "john",
                "lastName", "Smith",
                "birthDate", "2011-05-05"
        );
        mockMvc.perform(post("/api/v1/kids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation error")));
    }

    @Test
    void update_ShouldUpdateKid() throws Exception {
        UpdateKidCommand command = new UpdateKidCommand()
                .setFirstName("Jacek")
                .setLastName("Placek")
                .setBirthDate(LocalDate.of(2010, 2, 2));

        mockMvc.perform(put("/api/v1/kids/{kidId}", kid.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(kid.getId()))
                .andExpect(jsonPath("$.firstName").value("Jacek"))
                .andExpect(jsonPath("$.lastName").value("Placek"));
    }

    @Test
    void update_WhenKidNotFound_ShouldReturn404() throws Exception {
        int missingId = 888;
        UpdateKidCommand command = new UpdateKidCommand()
                .setFirstName("John")
                .setLastName("Black")
                .setBirthDate(LocalDate.of(2000, 2, 9));

        mockMvc.perform(put("/api/v1/kids/{kidId}", missingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Kid with id=" + missingId + " not found"));
    }

    @Test
    void updateKid_WhenMissingAllFields_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/kids/{kidId}", kid.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation error")));
    }

    @Test
    void delete_ShouldRemoveKid() throws Exception {
        int kidId = kid.getId();
        mockMvc.perform(get("/api/v1/kids/{kidId}", kid.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/kids/{kidId}", kid.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/kids/{kidId}", kidId))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllGifts_ShouldReturnGiftsOfKid() throws Exception {
        mockMvc.perform(get("/api/v1/kids/{kidId}/gifts", kid.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(gift.getId()))
                .andExpect(jsonPath("$.content[0].name").value("Lego"));
    }

    @Test
    void findGiftById_ShouldReturnGift() throws Exception {
        mockMvc.perform(get("/api/v1/kids/{kidId}/gifts/{giftId}", kid.getId(), gift.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(gift.getId()))
                .andExpect(jsonPath("$.name").value("Lego"));
    }

    @Test
    void findGiftById_WhenGiftNotFound_ShouldReturn404() throws Exception {
        int notExistingId = 999;
        mockMvc.perform(get("/api/v1/kids/{kidId}/gifts/{giftId}", kid.getId(), notExistingId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Gift with id=" + notExistingId + " and kidId=" + kid.getId() + " not found"));
    }

    @Test
    void createGift_ShouldCreateGift() throws Exception {
        CreateGiftCommand cmd = new CreateGiftCommand()
                .setName("Bike")
                .setPrice(999.99);

        mockMvc.perform(post("/api/v1/kids/{kidId}/gifts", kid.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bike"))
                .andExpect(jsonPath("$.price").value(999.99));
    }

    @Test
    void createGift_WhenTooManyGifts_ShouldReturnBadRequest() throws Exception {
        IntStream.range(0, 3).forEach(i ->
                giftRepository.save(Gift.builder()
                        .name("Extra" + i)
                        .price(10.0 + i)
                        .kid(kid)
                        .build())
        );

        CreateGiftCommand cmd = new CreateGiftCommand()
                .setName("TooMuch")
                .setPrice(1.0);

        mockMvc.perform(post("/api/v1/kids/{kidId}/gifts", kid.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("To many gifts for kid with id=" + kid.getId()));
    }

    @Test
    void createGift_WhenMissingAllFields_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/kids/{kidId}/gifts", kid.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation error")));
    }

    @Test
    void createGift_WhenNegativePrice_ShouldReturnBadRequest() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "Puzzle",
                "price", -5.0
        );
        mockMvc.perform(post("/api/v1/kids/{kidId}/gifts", kid.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation error")));
    }

    @Test
    void updateGift_ShouldUpdateGift() throws Exception {
        UpdateGiftCommand cmd = new UpdateGiftCommand()
                .setName("Scooter")
                .setPrice(299.99);

        mockMvc.perform(put("/api/v1/kids/{kidId}/gifts/{giftId}", kid.getId(), gift.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(gift.getId()))
                .andExpect(jsonPath("$.name").value("Scooter"))
                .andExpect(jsonPath("$.price").value(299.99));
    }

    @Test
    void updateGift_WhenMissingAllFields_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/kids/{kidId}/gifts/{giftId}", kid.getId(), gift.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation error")));
    }

    @Test
    void deleteGift_ShouldRemoveGift() throws Exception {
        mockMvc.perform(delete("/api/v1/kids/{kidId}/gifts/{giftId}", kid.getId(), gift.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/kids/{kidId}/gifts/{giftId}", kid.getId(), gift.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldImportKidsFromCsvFileSuccessfully() throws Exception {
        String csvContent = """
                first_name,last_name,birth_date
                Jan,Kowalski,2010-05-15
                Anna,Nowak,2012-08-22
                Piotr,Wiśniewski,2009-12-03
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "kids.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        long initialCount = kidRepository.count();

        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long finalCount = kidRepository.count();
                    assertEquals(initialCount + 3, finalCount);
                });
    }

    @Test
    void shouldImportLargeNumberOfKidsFromCsvFile() throws Exception {
        StringBuilder csvContent = new StringBuilder("first_name,last_name,birth_date\n");
        int numberOfKids = 100;

        for (int i = 1; i <= numberOfKids; i++) {
            csvContent.append(String.format("Kid%d,Surname%d,2010-01-%02d\n", i, i, (i % 28) + 1));
        }

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "large_kids.csv",
                "text/csv",
                csvContent.toString().getBytes(StandardCharsets.UTF_8)
        );

        long initialCount = kidRepository.count();

        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        await().atMost(30, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long finalCount = kidRepository.count();
                    assertEquals(initialCount + numberOfKids, finalCount);
                });
    }

    @Test
    void shouldHandleEmptyFile() throws Exception {
        String csvContent = "first_name,last_name,birth_date\n";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty_kids.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        long initialCount = kidRepository.count();

        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long finalCount = kidRepository.count();
                    assertEquals(initialCount, finalCount);
                });
    }

    @Test
    void shouldHandleMissingFileParameter() throws Exception {
        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleFileWithDifferentExtension() throws Exception {
        String csvContent = """
                first_name,last_name,birth_date
                Test,User,2010-01-01
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "kids.txt",
                "text/plain",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        long initialCount = kidRepository.count();

        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long finalCount = kidRepository.count();
                    assertEquals(initialCount + 1, finalCount);
                });
    }

    @Test
    void shouldHandleFileWithNoExtension() throws Exception {
        String csvContent = """
                first_name,last_name,birth_date
                NoExt,User,2010-01-01
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "kids_no_ext",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        long initialCount = kidRepository.count();

        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long finalCount = kidRepository.count();
                    assertEquals(initialCount + 1, finalCount);
                });
    }

    @Test
    void shouldImportKidsWithSpecialCharacters() throws Exception {
        String csvContent = """
                first_name,last_name,birth_date
                Józef,Żółć,2010-05-15
                Małgorzata,Śląsk,2012-08-22
                Łukasz,Ćwiklińska,2009-12-03
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "kids_special.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        long initialCount = kidRepository.count();

        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long finalCount = kidRepository.count();
                    assertEquals(initialCount + 3, finalCount);
                });
    }

}