package com.sns.gobong.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gobong_hashtag")
@Getter
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Size(max = 5, message = "최대 5글자까지 입력 가능합니다.")
    @Column(length = 5)
    private String tag;

    @ManyToMany(mappedBy = "hashTags")
    private Set<Board> boards = new HashSet<>();
}
