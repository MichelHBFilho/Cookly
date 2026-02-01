package com.michelfilho.cookly.post.controller;

import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.dto.ReadPostDTO;
import com.michelfilho.cookly.post.service.InteractPostService;
import com.michelfilho.cookly.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private InteractPostService interactPostService;

    @Operation(
            summary = "Publish a post",
            description = "Given all post information and images it publishes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post successfully published"),
            @ApiResponse(responseCode = "400", description = "Can't publish the post")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = {
                            @Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE),
                            @Encoding(name = "images", contentType = "image/png, image/jpeg")
                    }
            )
    )
    @PostMapping(
            value = "/new",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity publish(
            @RequestPart("data") @Validated NewPostDTO data,
            @RequestPart(value = "images", required = false)
            @Schema(type = "array", format = "binary")
            List<MultipartFile> images,
            @AuthenticationPrincipal UserDetails user
    ) {
        postService.publishPost(data, images, user);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Given an ID, it try to delete a post."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post successfully deleted."),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "403", description = "This user can't delete this post.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable @Parameter(example = "UUID") String id,
            @AuthenticationPrincipal UserDetails user
    ) {
        postService.removePost(id, user);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Given an username, return all posts of this person paginated."
    )
    @GetMapping("/{username}")
    public List<ReadPostDTO> getAllByUsername(
            @PathVariable String username,
            @RequestParam(value = "page", required = false,
                    defaultValue = "1") Integer page
    ) {
        return postService.findPostsByUsername(username, page);
    }

    @Operation(
            summary = "Return all posts paginated, for homepage."
    )
    @GetMapping
    public List<ReadPostDTO> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1")
            Integer page
    ) {
        return postService.getAllPosts(page);
    }

    @Operation(
            summary = "Given an post ID try to like the post with the logged user."
    )
    @ApiResponses(value =  {
            @ApiResponse(responseCode = "200", description = "Successfully liked"),
            @ApiResponse(responseCode = "400", description = "Post already liked by the user.")
    })
    @PostMapping("/{id}/like")
    public ResponseEntity likePost(
            @PathVariable @Parameter(example = "UUID") String id,
            @AuthenticationPrincipal UserDetails user
    ) {
        interactPostService.like(user, id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Given an post ID try to dislike the post with the logged user."
    )
    @ApiResponses(value =  {
            @ApiResponse(responseCode = "200", description = "Successfully disliked"),
            @ApiResponse(responseCode = "400", description = "Post isn't liked by the user.")
    })
    @DeleteMapping("/{id}/like")
    public ResponseEntity dislikePost(@PathVariable String id, @AuthenticationPrincipal UserDetails user) {
        interactPostService.dislike(user, id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Given an post ID comment on it with the logged user."
    )
    @PostMapping("/{id}/comment")
    public ResponseEntity comment(
            @PathVariable @Parameter(example = "UUID") String id,
            @AuthenticationPrincipal UserDetails user,
            @RequestBody @Parameter(example = "Very good recipe!") String content
    ) {
        interactPostService.comment(user, id, content);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Given an comment id try to delete it."
    )
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity removeComment(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String commentId
    ) {
        interactPostService.removeComment(user, commentId);
        return ResponseEntity.ok().build();
    }

}
