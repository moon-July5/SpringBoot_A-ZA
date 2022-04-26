package com.moon.aza.repository;

import com.moon.aza.entity.Likes;
import com.moon.aza.repository.likes.LikesCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface LikesRepository extends JpaRepository<Likes, Long>, LikesCustomRepository {
}
