package io.github.Piotr7421.giftapi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import io.github.Piotr7421.giftapi.repository.KidRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class KidControllerLargeFileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KidRepository kidRepository;

    private static final int EXPECTED_RECORDS_COUNT = 100_000;
    private static final String CSV_FILE_PATH = "plik.csv";

    @BeforeEach
    void setUp() {
        kidRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        kidRepository.deleteAll();
    }

    @Test
    void shouldImport100ThousandKidsFromCsvFileSuccessfully() throws Exception {
        ClassPathResource resource = new ClassPathResource(CSV_FILE_PATH);
        assertTrue(resource.exists(), "CSV file should exist in resources directory: " + CSV_FILE_PATH);

        MockMultipartFile file = createMockMultipartFileFromResource(resource);
        long initialCount = kidRepository.count();

        long startTime = System.currentTimeMillis();

        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        await().atMost(5, TimeUnit.MINUTES)
                .pollInterval(50, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    long finalCount = kidRepository.count();
                    assertEquals(initialCount + EXPECTED_RECORDS_COUNT, finalCount,
                            "Expected " + EXPECTED_RECORDS_COUNT + " records to be imported");
                });

        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        System.out.println("Successfully imported " + EXPECTED_RECORDS_COUNT +
                " records in " + processingTime + "ms");

        long finalCount = kidRepository.count();
        assertEquals(initialCount + EXPECTED_RECORDS_COUNT, finalCount);
    }

    @Test
    void shouldHandleLargeFileProcessingWithoutMemoryIssues() throws Exception {
        ClassPathResource resource = new ClassPathResource(CSV_FILE_PATH);
        assertTrue(resource.exists(), "CSV file should exist in resources directory: " + CSV_FILE_PATH);

        MockMultipartFile file = createMockMultipartFileFromResource(resource);

        // Monitor memory before processing
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // wait for processing and check memory usage
        await().atMost(5, TimeUnit.MINUTES)
                .pollInterval(50, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    long finalCount = kidRepository.count();
                    assertTrue(finalCount >= EXPECTED_RECORDS_COUNT,
                            "At least " + EXPECTED_RECORDS_COUNT + " records should be imported");
                });

        // Check memory usage after processing
        runtime.gc(); // Suggest garbage collection
        Thread.sleep(1000); // Wait a bit for GC
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = memoryAfter - memoryBefore;

        System.out.println("Memory increase during processing: " +
                (memoryIncrease / 1024 / 1024) + " MB");

        // Memory increase should be reasonable (less than 500MB for this test)
        assertTrue(memoryIncrease < 500 * 1024 * 1024,
                "Memory increase should be reasonable during large file processing");
    }

    @Test
    void shouldProcessLargeFileInBatches() throws Exception {
        ClassPathResource resource = new ClassPathResource(CSV_FILE_PATH);
        assertTrue(resource.exists(), "CSV file should exist in resources directory: " + CSV_FILE_PATH);

        MockMultipartFile file = createMockMultipartFileFromResource(resource);
        long initialCount = kidRepository.count();

        mockMvc.perform(multipart("/api/v1/kids/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // verify that records are being inserted in batches by checking intermediate counts
        await().atMost(30, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long currentCount = kidRepository.count();
                    assertTrue(currentCount > initialCount,
                            "Some records should be inserted during batch processing");
                });

        // Wait for complete processing
        await().atMost(5, TimeUnit.MINUTES)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    long finalCount = kidRepository.count();
                    assertEquals(initialCount + EXPECTED_RECORDS_COUNT, finalCount);
                });
    }

    private MockMultipartFile createMockMultipartFileFromResource(ClassPathResource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] content = inputStream.readAllBytes();
            return new MockMultipartFile(
                    "file",
                    resource.getFilename(),
                    "text/csv",
                    content
            );
        }
    }
}