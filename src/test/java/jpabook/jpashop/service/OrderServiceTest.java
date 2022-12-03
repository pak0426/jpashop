package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember("회원1", "서울시", "강가","123-123");

        //추상 클래스가 뭔지 파악하자
        Item book = createBook("숫자놀이", 10000, 10);

        //when
        //ctrl + shift + enter : 종료문장으로 완성
        //ctrl + alt + v : 숫자에서 단축키 누르면 변수로 자동 생성해줌
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        // ctrl + p : 파라미터 정보
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수는 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 10 - orderCount, book.getStockQuantity());
    }



    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember("현민", "서울시", "양천구", "122-122");

        Item book = createBook("사건의 지평선", 1000, 5);

        //when
        //then // ctrl + shit + p dataType 볼 수 있음

        int orderCount = 7;
        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail("재고수량 예외가 발생해야합니다.");

    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember("현민", "서울시", "양천구", "122-122");
        Item book = createBook("사건의 지평선", 1000, 5);

        //when
        int orderCount = 3;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문샅내는 취소여야 한다.", OrderStatus.CANCLE, getOrder.getStatus());
        assertEquals("재고수량은 주문 취소 전 수량이어야한다.", 5, book.getStockQuantity());

    }
    private Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
        em.persist(member);
        return member;
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }



}