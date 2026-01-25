package com.michelfilho.cookly.common.controller;

import com.michelfilho.cookly.common.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImagesController {

    @Autowired
    private ImageService imageService;
    @Value("${api.storage.pictures.profile.path}")
    private String profilesPicturePath;
    @Value("${api.storage.pictures.post.path}")
    private String postPicturePath;

    @GetMapping(value = "/profile/{fileName}",
    produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] profilePicure(@PathVariable String fileName) {
        return imageService.getImage(profilesPicturePath + "/" + fileName);
    }

    @GetMapping(value = "/post/{fileName}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] postPicture(@PathVariable String fileName) {
        return imageService.getImage(postPicturePath + "/" + fileName);
    }

}
