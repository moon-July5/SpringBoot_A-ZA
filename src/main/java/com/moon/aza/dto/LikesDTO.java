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
public class LikesDTO {
    private Long likeId;
    private Long boardId;
    private Long memberId;
    private LocalDateTime regDate, modDate;
}
