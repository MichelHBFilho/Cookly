package com.michelfilho.cookly.post.controller;

import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.repository.PostRepository;
import com.michelfilho.cookly.post.service.PostService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/new")
    public ResponseEntity publish(@RequestBody @Valid NewPostDTO data, @AuthenticationPrincipal UserDetails user) {
        postService.publishAPost(data, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity all() {
        return ResponseEntity.ok().body("Ok");
    }

}
