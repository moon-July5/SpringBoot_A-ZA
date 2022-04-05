package com.moon.aza.repository.search;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {
    Page<Object[]> searchBoard(BooleanBuilder booleanBuilder, Pageable pageable);
}
