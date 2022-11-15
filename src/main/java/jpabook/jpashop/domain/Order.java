package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "member_id") //FK 설정
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //EnumType의 종류 ORDINAL가 있는데 0, 1 이런식으로 순차적으로 배정되기 때문에 중간에 값이 추가되면 문제가 발생 따라서 무조건 String을 사용
    private OrderStatus status; // 주문상태 [ORDER, CANCLE]

}
