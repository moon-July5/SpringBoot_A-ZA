package com.moon.aza.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardForm {
    private Long id;

    private String title;

    private String writer;

    private String contents;

}
