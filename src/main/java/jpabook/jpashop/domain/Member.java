package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name= "member_id")
    private Long id;
    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    //mappedBy는 order 테이블의 거울과 같은 존재임을 알려줌(읽기 전용)
    //따라서 member에서 order의 값을 변경해도 order 테이블의 값이 바뀌지 않음
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
