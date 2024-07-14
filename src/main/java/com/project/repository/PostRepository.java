package com.project.repository;

import com.project.entity.user.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Posts, Long> {
    Optional<Posts> findPostsByPostId(Long id);
    List<Posts> findByDiaryDiaryId(Long diaryId);
}
