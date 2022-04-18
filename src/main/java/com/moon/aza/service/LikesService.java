package com.moon.aza.service;

import com.moon.aza.dto.LikesDTO;
import com.moon.aza.entity.Board;
import com.moon.aza.entity.Likes;
import com.moon.aza.entity.Member;
import com.moon.aza.repository.BoardRepository;
import com.moon.aza.repository.LikesRepository;
import com.moon.aza.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikesService {
    private final LikesRepository likesRepository;


    // 추천버튼 눌렀을 때
    public Boolean pushLike(LikesDTO likesDTO){
        Optional<Likes> result = likesRepository.exist(likesDTO.getMemberId(), likesDTO.getBoardId());

        // 존재하지 않으면
        if(!result.isPresent()){
            likesRepository.save(dtoToEntity(likesDTO));
            return true;
        } else {
          likesRepository.deleteById(result.get().getLikeId());
        }
        return false;
    }
    // 사용자가 추천을 했는지 확인
    public Boolean findLike(Long memberId, Long boardId){
        Optional<Likes> result = likesRepository.exist(memberId, boardId);
        if(!result.isPresent()) return false;
        else return true;
    }

    // DTO -> Entity
    private Likes dtoToEntity(LikesDTO likesDTO){
        Likes likes = Likes.builder()
                .member(Member.builder().id(likesDTO.getMemberId()).build())
                .board(Board.builder().id(likesDTO.getBoardId()).build())
                .build();
        return likes;
    }

}
