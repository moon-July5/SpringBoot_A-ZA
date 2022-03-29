package com.moon.aza.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    private Long commentNum;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean deleted;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    // 생성자
    public Comment(String text, Member member, Board board, Comment parent){
        this.text = text;
        this.member = member;
        this.board = board;
        this.parent = parent;
        this.deleted = false;
    }
    // 하위 댓글이 남아있어 실제로 제거할 수 없는 댓글인 경우, 삭제 표시
    public void delete(){
        this.deleted = true;
    }

    // 현재 댓글 기준으로 실제로 삭제할 수 있는 댓글 탐색
    public Optional<Comment> findDeletableComment(){
        // 하위 댓글이 존재하면? 즉시 제거하면 안됨
        // 하위 댓글이 없으면? 실제로 제거해도 되기 떄문에 상위 댓글 탐색
        return hasChildren() ? Optional.empty() : Optional.of(findDeletableCommentByParent());
    }

    // 하위 댓글(대댓글)이 있는지 확인
    private boolean hasChildren(){
        return getChildren().size() != 0;
    }

    // 상위 댓글로 올라가면서 실제로 제거해도 되는 댓글 탐색
    private Comment findDeletableCommentByParent(){
        if(isDeletedParent()){
            Comment deletableParent = getParent().findDeletableCommentByParent();
            if(getParent().getChildren().size() == 1) return deletableParent;
        }
        return this;
    }

    // 현재 댓글의 상위 댓글이 제거해도 되는 것인지 판별
    private boolean isDeletedParent(){
        // 부모가 존재, 이미 삭제 처리를 받았는지 확인
        return getParent() != null && getParent().isDeleted();
    }







}
