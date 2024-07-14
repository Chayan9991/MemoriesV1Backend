package com.project.service;

import com.project.entity.user.Posts;
import com.project.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public interface UserEntityService {
    public User createUser(User user);
    public Posts createPost(Long diaryId, Posts post);
}
