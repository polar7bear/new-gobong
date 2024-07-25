package com.sns.gobong.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "gobong_follow")
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower; // 팔로우 하는 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id")
    private User followee; // 팔로우 당하는 사람
}
