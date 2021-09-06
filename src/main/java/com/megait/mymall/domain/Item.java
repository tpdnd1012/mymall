package com.megait.mymall.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Item {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ColumnDefault("0")
    private int price;

    @ColumnDefault("100")
    private int stackQuantity = 100;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Category category;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToMany(mappedBy = "likes", cascade = CascadeType.ALL)
    private List<Member> likers; // 이 상품을 '좋아요'한 사람들

    @ManyToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<Member> owners; // 이 상품을 장바구니에 넣은 사람들

    // ** ManyToMany 비추천..
    //    중간 엔티티(테이블)을 따로 만드는 것이 좋다.

    @PostLoad
    public void createList(){
        if(likers == null){
            likers = new ArrayList<>();
        }
        if(owners == null){
            owners = new ArrayList<>();
        }
    }
}
