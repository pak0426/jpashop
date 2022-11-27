package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    Item item = new Item() {
        @Override
        public Long getId() {
            return super.getId();
        }

        @Override
        public String getName() {
            return super.getName();
        }

        @Override
        public void setName(String name) {
            super.setName(name);
        }

        @Override
        public void setId(Long id) {
            super.setId(id);
        }
    };

    @Test
    @Rollback(value = false)
    public void 물품수량저장() {

        //given
        item.setId(0000000L);
        item.setName("apple");

        //when
        itemService.save(item);

        //then
        em.flush();

        assertEquals(item, itemRepository.findOne(item.getId()));
    }
}
