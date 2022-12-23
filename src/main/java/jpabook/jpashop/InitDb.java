package jpabook.jpashop;


import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


/**
 * 총 주문 2개
 * userA
 *  * JPA1 Book
 *  * JPA2 Book
 *  * userB
 *  * SPRING1 Book
 *  * SPRING2 Book
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    //@PostConstruct : 스프링 빈이 다 올라오고 나면 호출해주는 것
    //종속성 주입이 완료된 후 실행되어야 하는 메서드에 사용된다. 이 어노테이션은 다른 리소스에서 호출되지 않아도 수행된다.
    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member); //em.persist()로 member를 영속성 콘텍스트로

            Book book1 = createBook("JPA1 Book", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 Book", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);

            //Order.java에서 파라미터 OrderItem... orderItems orderItems를 여러개 넘길 수 있다.
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "경기", "남양주", "12345");
            em.persist(member); //em.persist()로 member를 영속성 콘텍스트로

            Book book1 = createBook("SPRING Book", 20000, 300);
            em.persist(book1);

            Book book2 = createBook("SPRING Book", 30000, 400);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 2);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 30000, 3);

            Delivery delivery = createDelivery(member);

            //Order.java에서 파라미터 OrderItem... orderItems orderItems를 여러개 넘길 수 있다.
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private static Book createBook(String SPRING_Book, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(SPRING_Book);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
    }
}

