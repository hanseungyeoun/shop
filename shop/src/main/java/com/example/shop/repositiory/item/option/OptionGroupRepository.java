package com.example.shop.repositiory.item.option;

import com.example.shop.domain.item.option.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionGroupRepository extends JpaRepository<OptionGroup, Long> {

    Optional<OptionGroup> findByOptionGroupToken(String token);
}
