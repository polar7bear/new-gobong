package com.sns.gobong.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "gobong_board_like")
@Getter
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private boolean liked = false;
}
