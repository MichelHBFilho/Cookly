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

    @GetMapping(value = "/profile/{fileName}",
    produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable String fileName) {
        return imageService.getImage(profilesPicturePath + "/" + fileName);
    }

}
