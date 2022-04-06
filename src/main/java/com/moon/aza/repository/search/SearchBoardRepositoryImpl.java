package com.moon.aza.repository.search;

import com.moon.aza.entity.Board;
import com.moon.aza.entity.QBoard;
import com.moon.aza.entity.QComment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport
        implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {super(Board.class);}

    @Override
    public Page<Object[]> searchBoard(BooleanBuilder booleanBuilder, Pageable pageable) {
        QBoard qBoard = QBoard.board;
        QComment qComment = QComment.comment;

        JPQLQuery<Board> jpqlQuery = from(qBoard);
        jpqlQuery.leftJoin(qComment).on(qComment.board.eq(qBoard));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(qBoard, qComment.countDistinct());
        tuple.where(booleanBuilder);
        tuple.groupBy(qBoard);

        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC : Order.DESC;
            String property = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(property)));
        });

        long count = tuple.fetchCount();

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        Page<Object[]> page = new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable, count);
        return page;
    }
}
