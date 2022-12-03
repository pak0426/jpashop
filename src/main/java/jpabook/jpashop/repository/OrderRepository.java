package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    @Autowired
    QOrder order;
    @Autowired
    QMember member;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * 동적쿼리 queryDsl로 처리
     * @param orderSearch
     * @return
     */
    public List<Order> findAll(OrderSearch orderSearch) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch),
                        nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderSearch orderSearch) {
        if(orderSearch == null) {
            return null;
        }
        return order.status.eq(orderSearch.getOrderStatus());
    }

    private BooleanExpression nameLike(String memberName) {
        if(StringUtils.isEmpty(memberName)) {
            return null;
        }

        return member.name.eq(memberName);
    }

}
