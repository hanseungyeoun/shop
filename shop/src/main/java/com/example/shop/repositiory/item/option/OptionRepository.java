package com.example.shop.repositiory.item.option;


import com.example.shop.domain.item.option.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByOptionGroup_OptionGroupCode(String optionGroupName);
    void deleteByOptionToken(String optionGroupName);

    Optional<Option> findByOptionToken(String optionToken);
}
