package com.sns.gobong.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "gobong_introduce")
@Getter
public class Introduce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //@Size(max = 30, message = "30자 이내로 작성해주세요.")
    @Column(length = 30)
    private String bio;

}
