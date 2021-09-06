package com.megait.mymall.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Item item;  // 상품

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order; // 주문

    private int orderPrice; // 구매가

    private int count; // 수량
}
