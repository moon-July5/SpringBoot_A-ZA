package com.moon.aza.repository;

import com.moon.aza.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b, count(distinct c) from Board b " +
            "left outer join Comment c on c.board = b " +
            "group by b")
    Page<Object[]> getListPage(Pageable pageable);

    @Query("select b, count(distinct c) from Board b " +
            "left outer join Comment c on c.board = b " +
            "group by b")
    List<Object[]> getBoardWithAll(Long boardId);
}
