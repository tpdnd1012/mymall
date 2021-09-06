package com.megait.mymall.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 63)
    private String email; // username 역할

    @Column(nullable = false)
    private String password;

    @Embedded
    private Address address;

    private LocalDateTime joinedAt; // 가입일자

    @Column(nullable = false)
    @ColumnDefault("false") // default false
    private boolean emailVerified;  // 이메일 인증 여부

    private String emailCheckToken; // 이메일 인증에 사용할 임의의 문자열

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'ROLE_USER'") // default 'ROLE_USER'
    private MemberType memberType;  // 회원 유형

    private String name; // 사용자 이름

    private AuthType authType; // 인증 유형 (일반, 네이버, 구글)

    private String picture; // 프로필 사진 URL


    @ManyToMany(cascade = CascadeType.ALL)  // 다대다 양방향
    private List<Item> likes; // '좋아요'한 상품들

    @ManyToMany(cascade = CascadeType.ALL) // 다대다 양방향
    private List<Item> cart; // 장바구니

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // 일대다 양방향
    private List<Order> orders; // 주문내역

    @PostLoad
    public void createList() {
        if (likes == null) likes = new ArrayList<>();
        if (cart == null) cart = new ArrayList<>();
        if (orders == null) orders = new ArrayList<>();
    }

}
