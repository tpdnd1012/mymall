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
public class Delivery {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false) //asdasd
    private Order order; // 주문 정보

    @Embedded
    @Column(nullable = false)
    private Address address; // 배송지 주소

    @Enumerated
    private DeliveryStatus deliveryStatus; // 배송 상태 (상품 준비중, 배송중, 배송 완료)
}
