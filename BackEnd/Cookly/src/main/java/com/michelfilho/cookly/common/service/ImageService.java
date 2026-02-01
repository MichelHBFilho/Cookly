package com.michelfilho.cookly.common.service;

import com.michelfilho.cookly.common.exception.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    // The return is the path to the saved image
    @SneakyThrows
    public String saveImage(String dir, MultipartFile image) {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        Path uploadPath = Paths.get(dir);
        Path filePath = uploadPath.resolve(fileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @SneakyThrows
    public byte[] getImage(String path) {
        Path imagePath = Path.of(path);

        if(!Files.exists(imagePath)) return null;

        return Files.readAllBytes(imagePath);
    }

    @SneakyThrows
    public void deleteImage(String path) {
        Path pathObject = Path.of(path);

        if(!Files.exists(pathObject))
            throw new NotFoundException(Files.class);

        Files.delete(pathObject);
    }
}
