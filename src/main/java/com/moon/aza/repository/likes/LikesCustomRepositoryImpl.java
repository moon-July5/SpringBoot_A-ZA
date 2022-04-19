package com.moon.aza.repository.likes;

import com.moon.aza.entity.Likes;
import com.moon.aza.entity.QBoard;
import com.moon.aza.entity.QLikes;
import com.moon.aza.entity.QMember;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.Optional;

public class LikesCustomRepositoryImpl extends QuerydslRepositorySupport
        implements LikesCustomRepository{

    JPAQueryFactory jpaQueryFactory;
    QLikes qLikes = QLikes.likes;

    public LikesCustomRepositoryImpl(EntityManager em) {
       super(Likes.class);
       this.jpaQueryFactory = new JPAQueryFactory(em);
   }

    @Override
    public Optional<Likes> exist(Long memberId, Long boardId) {
        Likes likes = jpaQueryFactory.selectFrom(qLikes)
                .where(qLikes.member.id.eq(memberId),
                qLikes.board.id.eq(boardId))
                .fetchFirst();

        return Optional.ofNullable(likes);
    }

}
