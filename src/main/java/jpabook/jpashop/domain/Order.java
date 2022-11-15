package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Lazy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") //FK 설정
    private Member member;

    //casecade ALL을 하는 이유는 order 1개당 orderItems가 3개라고 해보자. orderItems의 데이터가 DB에 저장될때 em.persist(); 이런식으로 하는데
    //cascade가 없으면 orderItems 각각 3개 persist하고 order까지도 persist를 해야한다. 즉 엔티티가 다르다면 각각 persist를 해야한다는 것이다.
    //cascade가 ALL이면 저장, 삭제할때 order만 persist하면 orderItems의 persis까지 같이 해준다고 생각하면 된다.
    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //EnumType의 종류 ORDINAL가 있는데 0, 1 이런식으로 순차적으로 배정되기 때문에 중간에 값이 추가되면 문제가 발생 따라서 무조건 String을 사용
    private OrderStatus status; // 주문상태 [ORDER, CANCLE]

    //==연관관계 편의 메소드==//
}
