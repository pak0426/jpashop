package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        //item값엔 기본적으로 id가 없을 것이다 그러면 신규건이니 item을 저장한다.
        //id값이 있다면 새로 생성된 item객체가 아니라는 뜻이니 else문을 타게 한다.
        //merge란 어디선가 값이 있는 item객체 이므로 병합을 해준다는 의미 쉽게 update를 한다고 생각하면 된다.
        if(item.getId() == null) {
            em.persist(item);
        }
        else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
