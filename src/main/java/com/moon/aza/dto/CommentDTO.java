package com.moon.aza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDTO {
    private Long commentNum;
    private Long boardId;
    private Long memberId;
    private String text;
    private String nickname;
    private LocalDateTime regDate, modDate;
}
