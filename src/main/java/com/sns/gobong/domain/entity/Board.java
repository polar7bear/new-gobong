package com.sns.gobong.domain.entity;

import com.sns.gobong.util.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gobong_board")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(value = 200, message = "200자이내로 작성해주세요.")
    private String content;

    private Integer Views; // 조회수

    private String img1; // TODO: AWS S3
    private String img2;
    private String img3;

    @ManyToMany
    @JoinTable(
            name = "gobong_board_hashtag",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<HashTag> hashTags = new HashSet<>();
}
