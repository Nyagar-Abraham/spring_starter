package org.studyeasy.SpringStarter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studyeasy.SpringStarter.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
