package com.michelfilho.cookly.post.controller;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.dto.ReadPostDTO;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.service.InteractPostService;
import com.michelfilho.cookly.post.service.PostService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private InteractPostService interactPostService;

    @PostMapping("/new")
    public ResponseEntity publish(@RequestBody @Valid NewPostDTO data, @AuthenticationPrincipal UserDetails user) {
        postService.publishPost(data, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id, @AuthenticationPrincipal UserDetails user) {
        postService.removePost(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public List<ReadPostDTO> getAllByUsername(@PathVariable String username, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        return postService.findPostsByUsername(username, page);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity likePost(@PathVariable String id, @AuthenticationPrincipal UserDetails user) {
        interactPostService.like(user, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity dislikePost(@PathVariable String id, @AuthenticationPrincipal UserDetails user) {
        interactPostService.dislike(user, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity comment(@PathVariable String id, @AuthenticationPrincipal UserDetails user, @RequestBody String content) {
        interactPostService.comment(user, id, content);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity removeComment(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String commentId
    ) {
        interactPostService.removeComment(user, commentId);
        return ResponseEntity.ok().build();
    }

}
