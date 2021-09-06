package com.megait.mymall.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Column(length = 31)
    private String postcode; // 우편번호

    private String baseAddress; // 기본주소

    private String detailAddress; // 상세주소
}
