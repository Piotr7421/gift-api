package pl.spring.giftapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.spring.giftapi.exceptions.LoadingIOException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class KidsFileAsyncService {
    private final KidsFileImportService kidsFileImportService;

    @Async("asyncTaskExecutor")
    public void asyncProcessKidsFile(Path tmpFile, String originalFilename) {
        try {
            kidsFileImportService.importKidsFromFile(tmpFile);
            Files.deleteIfExists(tmpFile);
        } catch (IOException ex) {
            throw new LoadingIOException("Could not process inserts from file: " + originalFilename);
        }
    }
}
