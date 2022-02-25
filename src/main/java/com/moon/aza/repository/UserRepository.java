package com.moon.aza.repository;

import com.moon.aza.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    User findById(String id);

    User findByUid(Long uid);

    // DB에 존재하는지 여부 확인(중복확인)
    boolean existsById(String id);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

}
