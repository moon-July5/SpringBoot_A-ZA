package com.moon.aza.repository;

import com.moon.aza.entity.TemporaryBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryBoardRepository extends JpaRepository<TemporaryBoard, Long> {
    @Query("select t from TemporaryBoard t where t.member.id = :memberId")
    List<TemporaryBoard> findByMemberId(@Param("memberId") Long memberId);

    @Query("select count(t) from TemporaryBoard t where t.member.id = :memberId group by t.member.id")
    Long getTemporaryBoardCount(@Param("memberId") Long memberId);
}

