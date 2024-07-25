package com.sns.gobong.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
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

    @Max(value = 5, message = "최대 5글자까지 입력 가능합니다.")
    @Column(length = 5)
    private String tag;

    @ManyToMany(mappedBy = "hash_tags")
    private Set<Board> boards = new HashSet<>();
}
