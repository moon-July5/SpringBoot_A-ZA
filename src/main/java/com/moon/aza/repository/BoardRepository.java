package com.moon.aza.repository;

import com.moon.aza.entity.Board;
import com.moon.aza.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
    @Query("select b, count(c) from Board b " +
            "left outer join Comment c on c.board = b " +
            "group by b")
    Page<Object[]> getListPage(Pageable pageable);

    @Query("select b, count(c), count(l) from Board b " +
            "left outer join Comment c on c.board = b " +
            "left outer join Likes l on l.board = b " +
            "where b.id = :boardId group by b")
    List<Object[]> getBoardWithAll(@Param("boardId") Long boardId);

    @Query("select b.id from Board b where b.member.id = :memberId")
    List<Long> findByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query("delete from Board b where b.id in :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);
}
