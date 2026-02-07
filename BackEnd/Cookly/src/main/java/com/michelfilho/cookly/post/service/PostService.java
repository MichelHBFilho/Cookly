package com.michelfilho.cookly.post.service;

import com.michelfilho.cookly.common.exception.NotFoundException;
import com.michelfilho.cookly.common.exception.UnauthorizedException;
import com.michelfilho.cookly.common.service.ImageService;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.post.dto.ReadCommentDTO;
import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.dto.ReadPostDTO;
import com.michelfilho.cookly.post.dto.ReadRecipeDTO;
import com.michelfilho.cookly.post.model.Comment;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.model.Recipe;
import com.michelfilho.cookly.post.model.StepToPrepare;
import com.michelfilho.cookly.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PersonRepository personRepository;
    @Value("${api.storage.pictures.post.path}")
    private String postPath;
    @Autowired
    private ImageService imageService;

    public void publishPost(
            NewPostDTO data,
            List<MultipartFile> images,
            UserDetails user
    ) {
        Person person = personRepository.findByUserUsername(user.getUsername());

        Recipe newRecipe = new Recipe(
                new ArrayList<StepToPrepare>(),
                data.prepareTime(),
                data.recipeName()
        );

        newRecipe.addListOfStringSteps(data.stepsToPrepare());

        List<String> imagesPaths = new ArrayList<>();
        if(images != null) {
            images.forEach((MultipartFile file) ->
                    imagesPaths.add(
                        imageService.saveImage(
                                postPath,
                                file
                        )
                    )
            );
        }

        Post newPost = new Post(
                newRecipe,
                person,
                data.description(),
                imagesPaths
        );

        postRepository.save(newPost);
    }

    public void removePost(
            String id,
            UserDetails user
    ) {
        if(!postRepository.existsById(id))
            throw new NotFoundException(Post.class);

        Person person = personRepository.findByUserUsername(user.getUsername());

        if(!postRepository.existsByIdAndPerson(id, person))
            throw new UnauthorizedException("You can't delete this post");

        postRepository.deleteById(id);
    }

    public List<ReadPostDTO> findPostsByUsername(
            String username,
            Integer page
    ) {
        if(page <= 0) throw new IllegalArgumentException();

        return postRepository
                .findAllByPersonUserUsername(
                        username,
                        10,
                        (page-1)*10
                )
                .stream()
                .map(this::postToReadDTO)
                .toList();
    }

    public ReadPostDTO findPostById(String id) {
        return postToReadDTO(
                postRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException(Post.class))
                        );
    }

    public List<ReadPostDTO> getAllPosts(Integer page) {
        if(page <= 0) throw new IllegalArgumentException();

        return postRepository.findAllPaginated(
                        10,
                        (page-1)*10
                )
                .stream()
                .map(this::postToReadDTO)
                .toList();
    }

    protected ReadPostDTO postToReadDTO(Post post) {
        Recipe recipe = post.getRecipe();
        ReadRecipeDTO recipeDTO = new ReadRecipeDTO(
                recipe.getName(),
                recipe.getPrepareTime(),
                recipe.getStepByStep()
                        .stream()
                        .map(StepToPrepare::getDescription)
                        .toList()
        );

        List<Comment> comments = post.getComments();
        List<ReadCommentDTO> readCommentDTO = comments
                .stream()
                .map((Comment comment) -> {
                    return new ReadCommentDTO(
                            comment.getId(),
                            comment.getPerson().getFullName(),
                            comment.getContent(),
                            comment.getCreatedAt()
                    );
                })
                .toList();

        Integer likesCount = post.getPostLikes().size();

        return new ReadPostDTO(
                post.getId(),
                recipeDTO,
                readCommentDTO,
                likesCount,
                post.getPerson().getUser().getUsername(),
                post.getDescription(),
                post.getCreatedAt(),
                post.getImagesPaths()
        );
    }
}
