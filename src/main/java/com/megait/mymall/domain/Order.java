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
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="orders")  // DB 내부에서는 order 대신 orders 사용
public class Order {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false) // asdasd
    private Member member; // 주문자

    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItems; // 주문한 상품들

    @OneToOne
    private Delivery delivery;  // 배송 정보

    @Enumerated
    @ColumnDefault("'ORDER'")
    private OrderStatus orderStatus; // 주문 상태 (주문함, 취소함)

    private LocalDateTime orderTime; // 주문 시간

    @PostLoad
    public void createList() {
        if (orderItems == null) orderItems = new ArrayList<>();
    }
}
