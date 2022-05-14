package com.moon.aza.service;

import com.moon.aza.dto.BoardForm;
import com.moon.aza.entity.Member;
import com.moon.aza.entity.TemporaryBoard;
import com.moon.aza.repository.TemporaryBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TemporaryBoardService {
    private final TemporaryBoardRepository temporaryBoardRepository;

    public TemporaryBoard register(BoardForm boardForm){
        TemporaryBoard temporaryBoard = TemporaryBoard.builder()
                .title(boardForm.getTitle())
                .contents(boardForm.getContents())
                .writer(boardForm.getWriter())
                .member(Member.builder().id(boardForm.getId()).build())
                .build();

        return temporaryBoardRepository.save(temporaryBoard);
    }

    public BoardForm temporaryBoardLoad(Long id){
        TemporaryBoard tBoard = temporaryBoardRepository.getById(id);
        BoardForm boardForm = BoardForm.builder()
                .id(tBoard.getId())
                .title(tBoard.getTitle())
                .contents(tBoard.getContents())
                .writer(tBoard.getWriter())
                .regDate(tBoard.getRegDate())
                .modDate(tBoard.getModDate())
                .build();
        return boardForm;
    }
    public Long getCount(Long memberId){
        Long result = temporaryBoardRepository.getTemporaryBoardCount(memberId);
        if(result==null) result = 0L;
        return result;
    }

    public List<TemporaryBoard> getList(Long memberId){
        List<TemporaryBoard> result = temporaryBoardRepository.findByMemberId(memberId);
        return result;
    }

    @Transactional
    public void remove(Long id){
        temporaryBoardRepository.deleteById(id);
    }

}
