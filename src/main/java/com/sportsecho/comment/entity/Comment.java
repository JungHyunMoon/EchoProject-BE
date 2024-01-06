package com.sportsecho.comment.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.game.entity.Game;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 전체 인자를 가지는 생성자
@Builder // 빌더 패턴 적용
public class Comment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "member_name")
    private String memberName; // 댓글 작성자 이름
}