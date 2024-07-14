package com.project.controller.other.user;

import com.project.entity.user.Posts;
import com.project.entity.user.User;
import com.project.repository.PostRepository;
import com.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    @GetMapping()
    public ResponseEntity<String> userHome(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hello From Demo page Only User can access: "+ authentication.getName());
    }

    @PostMapping("/update-user")
    public ResponseEntity<?> updateCurrentUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> currentUser = userRepository.findByUserEmail(userName);
        if(currentUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        currentUser.get().setUserFullName(user.getUserFullName());
        currentUser.get().setUserProfileImage(user.getUserProfileImage());
        try{
          User savedUser = userRepository.save(currentUser.get());
          return ResponseEntity.ok("User Updated Successfully: "+savedUser.getUserFullName());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> currentUser = userRepository.findByUserEmail(userName);
        if(currentUser.isPresent()){
            return  ResponseEntity.ok(currentUser.get().toString());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create-post")
    public ResponseEntity<?> savePost(@RequestBody Posts posts) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> currentUser = userRepository.findByUserEmail(userName);

        if(currentUser.isPresent()){
            posts.setDiary(currentUser.get().getDiary());
           Posts savedPost =  postRepository.save(posts);
            return  ResponseEntity.ok("Post Saved Successfully..."+savedPost);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/get-post/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> currentUser = userRepository.findByUserEmail(userName);

        if(currentUser.isPresent()){
            Optional<Posts> getPost = postRepository.findPostsByPostId(postId);
            return getPost.isPresent()
                    ? ResponseEntity.ok(postRepository.findPostsByPostId(postId))
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/get-all-posts")
    public ResponseEntity<?> getPost(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> currentUser = userRepository.findByUserEmail(userName);

        if(currentUser.isPresent()){
            Long diaryId = currentUser.get().getDiary().getDiaryId();
            try{
                return ResponseEntity.ok(postRepository.findByDiaryDiaryId(diaryId));
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/update-post")
    public ResponseEntity<?> updatePost(@RequestBody Posts posts) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> currentUser = userRepository.findByUserEmail(userName);

        Long postId = posts.getPostId();
        if (currentUser.isPresent()) {
            Optional<Posts> getPost = postRepository.findPostsByPostId(postId);
            if (getPost.isPresent()) {
                Posts postToUpdate = getPost.get();

                // Check if the current user is associated with the post's diary
                if (!postToUpdate.getDiary().getUser().getUserEmail().equals(userName)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this post");
                }

                postToUpdate.setTitle(posts.getTitle());
                postToUpdate.setImage(posts.getImage());
                postToUpdate.setContent(posts.getContent());

                Posts updatedPost = postRepository.save(postToUpdate);
                return ResponseEntity.ok(updatedPost);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> currentUser = userRepository.findByUserEmail(userName);

        if (currentUser.isPresent()) {
            Optional<Posts> getPost = postRepository.findPostsByPostId(postId);
            if (getPost.isPresent()) {
                Posts postToDelete = getPost.get();

                // Check if the current user is associated with the post's diary
                if (!postToDelete.getDiary().getUser().getUserEmail().equals(userName)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this post");
                }

                try {
                    postRepository.deleteById(postId);
                    return ResponseEntity.ok("Post Deleted Successfully...");
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong while deleting the post");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post Not Found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
    }


}
