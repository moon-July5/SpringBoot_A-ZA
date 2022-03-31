package com.moon.aza.service;

import com.moon.aza.dto.CommentDTO;
import com.moon.aza.entity.Board;
import com.moon.aza.entity.Comment;
import com.moon.aza.entity.Member;
import com.moon.aza.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    // 각 게시글 댓글 출력
    public List<CommentDTO> getListOfComment(Long boardId){
        Board board = Board.builder().id(boardId).build();
        List<Comment> result = commentRepository.findByBoard(board);
        return result.stream().map(comment -> entitiesToDTO(comment)).collect(Collectors.toList());
    }
    // 댓글 저장
    public Long save(CommentDTO commentDTO){
        Comment comment = dtoToEntity(commentDTO);
        commentRepository.save(comment);
        return comment.getCommentNum();
    }

    // DTO -> Entity
    private Comment dtoToEntity(CommentDTO commentDTO){
        Comment comment = Comment.builder()
                .text(commentDTO.getText())
                .member(Member.builder().id(commentDTO.getMemberId()).build())
                .board(Board.builder().id(commentDTO.getBoardId()).build())
                .build();

        return comment;
    }
    // Entity -> DTO
    public CommentDTO entitiesToDTO(Comment comment){
        CommentDTO commentDTO = CommentDTO.builder()
                .commentNum(comment.getCommentNum())
                .text(comment.getText())
                .boardId(comment.getBoard().getId())
                .memberId(comment.getMember().getId())
                .nickname(comment.getMember().getNickname())
                .regDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .build();

        return commentDTO;
    }
}
