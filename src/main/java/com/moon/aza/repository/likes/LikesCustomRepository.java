package com.moon.aza.repository.likes;

import com.moon.aza.entity.Likes;

import java.util.Optional;

public interface LikesCustomRepository {
    Optional<Likes> exist(Long memberId, Long boardId);
    long findBoardLikesNum(Long boardId);
}
