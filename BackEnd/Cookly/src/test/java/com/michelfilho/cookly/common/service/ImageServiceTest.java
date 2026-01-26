package com.michelfilho.cookly.common.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    private ImageService imageService = new ImageService();
    private static String dir = "src/main/test/resources/";

    @AfterAll
    public static void deleteTestFolder() throws IOException {
        FileUtils.deleteDirectory(new File(dir));
    }

    @BeforeAll
    public static void setup() throws IOException {
        Files.createDirectories(Path.of(dir));
    }

    @Test
    public void shouldSaveImage() throws IOException {
        byte[] inputArray = "Test String".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", inputArray);

        String fileName = imageService.saveImage(dir, mockMultipartFile);

        Path path = Path.of(dir, fileName);
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertArrayEquals(inputArray, Files.readAllBytes(path));
    }

    @Test
    public void shouldGetImage() throws IOException {
        byte[] inputArray = "Test String".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", inputArray);
        Path path = Path.of(dir).resolve("fileName");
        Files.copy(mockMultipartFile.getInputStream(), path);

        byte[] returnedImage = imageService.getImage(path.toString());

        Assertions.assertArrayEquals(inputArray, returnedImage);
    }

    @Test
    public void shouldDeleteImage() throws IOException {
        byte[] inputArray = "Test String".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", inputArray);
        Path path = Path.of(dir).resolve("fileName");
        Files.copy(mockMultipartFile.getInputStream(), path);

        Assertions.assertTrue(Files.exists(path));

        imageService.deleteImage(path.toString());

        Assertions.assertFalse(Files.exists(path));
    }

}