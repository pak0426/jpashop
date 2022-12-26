package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * xToOne
 * Order
 * Order -> Member (ManyToOne)
 * Order -> Delivery (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        //Order -> Member -> Order -> Member 무한 루프 발생
        // 해결하려면 양방향 연관관계가 있는 곳에 @JsonIgnore 생성해야되는데 이렇게 해도 에러가 발생 해결하고 싶으면
        // Jackson5Moudle 라이브러리를 gradle에 등록 후 Application.java에서 빈으로 등록한다
        // 그리고 저렇게 해도 지연로딩이기 때문에 null로 나온다
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    //일반 Join
    //Fetch Join과 달리 연관 Entity에 Join을 걸어도 실제 쿼리에서 SELECT 하는 Entity는
    //오직 JPQL에서 조회하는 주체가 되는 Entity만 조회하여 영속화
    //조회의 주체가 되는 Entity만 SELECT 해서 영속화하기 때문에 데이터는 필요하지 않지만 연관 Entity가 검색조건에는 필요한 경우에 주로 사용됨
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }


    //Fetch Join
    //조회의 주체가 되는 Entity 이외에 Fetch Join이 걸린 연관 Entity도 함께 SELECT 하여 모두 영속화
    //Fetch Join이 걸린 Entity 모두 영속화하기 때문에 FetchType이 Lazy인 Entity를 참조하더라도
    //이미 영속성 컨텍스트에 들어있기 때문에 따로 쿼리가 실행되지 않은 채로 N+1문제가 해결됨
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() { return orderSimpleQueryRepository.findOrderDtos(); }

    @Data
    @AllArgsConstructor
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }


}
