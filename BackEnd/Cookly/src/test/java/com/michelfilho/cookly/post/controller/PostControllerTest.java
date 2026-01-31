package com.michelfilho.cookly.post.controller;

import com.google.gson.Gson;
import com.michelfilho.cookly.CooklyApplication;
import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.service.PostService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.List;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CooklyApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "api.security.token.secret=test-secret",
        "api.storage.pictures.profile.path=/src/main/resources/profile_pictures",
        "jwt.refreshExpirationMs=5000"
})
class PostControllerTest {

    @MockitoBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    @BeforeEach
    void setup() {
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void shouldPostValidPost_WithoutImages() throws Exception {
        NewPostDTO dto = Instancio.of(NewPostDTO.class).create();

        MockMultipartFile dataPart = new MockMultipartFile(
                "data",
                "data.json",
                MediaType.APPLICATION_JSON_VALUE,
                gson.toJson(dto).getBytes()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/post/new")
                        .file(dataPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void shouldPostValidPost_WithImages() throws Exception {
        NewPostDTO dto = Instancio.of(NewPostDTO.class).create();

        MockMultipartFile dataPart = new MockMultipartFile(
                "data",
                "data.json",
                MediaType.APPLICATION_JSON_VALUE,
                gson.toJson(dto).getBytes()
        );

        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                "image1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-1".getBytes()
        );

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                "image2.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image-2".getBytes()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/post/new")
                        .file(dataPart)
                        .file(image1)
                        .file(image2)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldNotPostUnlogged() throws Exception {
        NewPostDTO dto = Instancio.of(NewPostDTO.class).create();

        MockMultipartFile dataPart = new MockMultipartFile(
                "data",
                "data.json",
                MediaType.APPLICATION_JSON_VALUE,
                gson.toJson(dto).getBytes()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/post/new")
                        .file(dataPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void shouldNotPostInvalidDTO_lessThan1Step() throws Exception {
        NewPostDTO dto = Instancio.of(NewPostDTO.class)
                .set(field("stepsToPrepare"), new ArrayList<>())
                .create();

        MockMultipartFile dataPart = new MockMultipartFile(
                "data",
                "data.json",
                MediaType.APPLICATION_JSON_VALUE,
                gson.toJson(dto).getBytes()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/post/new")
                        .file(dataPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void shouldNotPostInvalidDTO_moreThan15Step() throws Exception {
        ArrayList<String> steps = new ArrayList<>();

        for(int i = 0; i < 16; i++) {
            steps.add("step " + i);
        }

        NewPostDTO dto = Instancio.of(NewPostDTO.class)
                .set(field("stepsToPrepare"), steps)
                .create();

        MockMultipartFile dataPart = new MockMultipartFile(
                "data",
                "data.json",
                MediaType.APPLICATION_JSON_VALUE,
                gson.toJson(dto).getBytes()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/post/new")
                        .file(dataPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}