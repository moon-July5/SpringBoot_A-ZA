package com.moon.aza.repository;

import com.moon.aza.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 댓글의 commentNum 으로 조회하면서 자신의 부모 댓글과 함께 join된 결과 출력
    @Query("select c " +
            "from Comment c left join fetch c.parent " +
            "where c.commentNum = :commentNum")
    Optional<Comment> findWithParentById(Long commentNum);

    // 부모 댓글을 오름차순으로 정렬하되 NULL을 우선적으로
    // 자신 댓글을 오름차순 정렬하여 조회
    @Query("select c " +
            "from Comment c join fetch c.member left join fetch c.parent " +
            "where c.board.id = :boardId order by c.parent.commentNum asc nulls first, c.commentNum asc")
    List<Comment> findAllWithComment(Long boardId);

}
