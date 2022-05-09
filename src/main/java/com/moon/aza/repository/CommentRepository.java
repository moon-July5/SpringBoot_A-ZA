package com.moon.aza.repository;

import com.moon.aza.entity.Board;
import com.moon.aza.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Comment> findByBoard(Board board);

    @Query("select c.commentNum from Comment c where c.member.id = :memberId")
    List<Long> findByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query("delete from Comment c where c.commentNum in :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);

}
