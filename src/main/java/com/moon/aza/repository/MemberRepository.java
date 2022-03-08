package com.moon.aza.repository;

import com.moon.aza.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    /* 이메일 중복 여부 확인 */
    boolean existsByEmail(String email);

    /* 닉네임 중복 여부 확인 */
    boolean existsByNickname(String nickname);

    public Member findByEmail(String email);
}
