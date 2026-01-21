package com.michelfilho.cookly.post.controller;

import com.google.gson.Gson;
import com.michelfilho.cookly.CooklyApplication;
import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CooklyApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "api.security.token.secret=test-secret"
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
    public void shouldPostValidPost() throws Exception {
        NewPostDTO dto = new NewPostDTO(
                "RecipeName",
                "This is my recipe",
                15,
                List.of("Cook", "Uncook")
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/post/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldNotPostUnlogged() throws Exception {
        NewPostDTO dto = new NewPostDTO(
                "RecipeName",
                "This is my recipe",
                15,
                List.of("Cook", "Uncook")
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/post/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto))
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void shouldNotPostInvalidDTO_lessThan1Step() throws Exception {
        NewPostDTO dto = new NewPostDTO(
                "RecipeName",
                "This is my recipe",
                15,
                List.of()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/post/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void shouldNotPostInvalidDTO_moreThan15Step() throws Exception {
        ArrayList<String> steps = new ArrayList<>();
        for(int i = 0; i < 16; i++) {
            steps.add("step " + i);
        }

        NewPostDTO dto = new NewPostDTO(
                "RecipeName",
                "This is my recipe",
                15,
                steps
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/post/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void shouldRemovePostWhenAuthenticated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/post/delete/idtodelete")
        ).andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService).removePost(eq("idtodelete"), any());
    }



}