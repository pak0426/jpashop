package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded //내장타입이기 때문
    private Address address;

    @Enumerated(EnumType.STRING) //EnumType의 종류 ORDINAL가 있는데 0, 1 이런식으로 순차적으로 배정되기 때문에 중간에 값이 추가되면 문제가 발생 따라서 무조건 String을 사용
    private DeliveryStatus status; //READY, COMP
}
