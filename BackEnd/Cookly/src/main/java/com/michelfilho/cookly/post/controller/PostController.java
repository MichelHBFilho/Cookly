package com.michelfilho.cookly.post.controller;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        postService.publishPost(data, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id, @AuthenticationPrincipal UserDetails user) {
        postService.removePost(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public Page<Post> getAllByUsername(@PathVariable String username, @RequestParam("page") Integer page) {
        return postService.findPostsByUsername(username, page);
    }

}
