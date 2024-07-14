package com.project.service;

import com.project.entity.user.Posts;
import com.project.entity.user.User;
import com.project.entity.user.UserDiary;
import com.project.repository.PostRepository;
import com.project.repository.UserDiaryRepo;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserEntityServiceImpl implements UserEntityService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDiaryRepo userDiaryRepo;

    @Autowired
    private PostRepository postRepository;

    @Override
    public User createUser(User user) {
        User savedUser = userRepository.save(user);

        //create a Diary for the User
         UserDiary userDiary = new UserDiary();
         userDiary.setUser(savedUser);
         userDiaryRepo.save(userDiary);
         return savedUser;
    }

    @Override
    public Posts createPost(Long diaryId, Posts post) {
        Optional<UserDiary> diary = userDiaryRepo.findById(diaryId);
       if(diary.isEmpty()){
           throw new RuntimeException("Diary not found for id: " + diaryId);
       }else{
           post.setDiary(diary.get());
       }

        return postRepository.save(post);
    }


}
