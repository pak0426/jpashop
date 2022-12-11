package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional //readonly false로 변환시켜줌
    public void save(Item item) {
        itemRepository.save(item);
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }


    public void updateItem(Long itemId, String name, int price, int stockQuantity) { //파라미터가 너무 많으면 updateItemDto라는걸 만들어서 넘겨라
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }
}
