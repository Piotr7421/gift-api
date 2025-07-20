package pl.spring.giftapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.spring.giftapi.exceptions.LoadingIOException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KidsFilePreparationService {

    private final KidsFileAsyncService kidsFileAsyncService;

    private static String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".tmp";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    public void processKidsFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = getExtension(originalFilename);
        try {
            Path tmp = Files.createTempFile("kids-" + UUID.randomUUID(), extension);
            multipartFile.transferTo(tmp);
            kidsFileAsyncService.asyncProcessKidsFile(tmp, multipartFile.getOriginalFilename());
        } catch (IOException ex) {
            throw new LoadingIOException("Could not process inserts from file: "
                    + multipartFile.getOriginalFilename());
        }
    }
}
