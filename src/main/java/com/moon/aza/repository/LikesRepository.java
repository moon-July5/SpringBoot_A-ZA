package com.moon.aza.repository;

import com.moon.aza.entity.Likes;
import com.moon.aza.repository.likes.LikesCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface LikesRepository extends JpaRepository<Likes, Long>, LikesCustomRepository {
    @Query("select l.likeId from Likes l where l.member.id = :memberId")
    List<Long> findByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query("delete from Likes l where l.likeId in :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);
}
