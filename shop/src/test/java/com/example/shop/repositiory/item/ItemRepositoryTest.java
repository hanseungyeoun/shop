package com.example.shop.repositiory.item;

import com.example.shop.config.TestJpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@Import({TestJpaConfig.class})
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void test(){
        //given
        PageRequest request = PageRequest.of(10, 10);
        //when
        itemRepository.findAllByCategoryToken("token", request);

        //then

    }

}