package com.example.shop.domain.item.option;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import com.example.shop.util.TokenGenerator;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "option_group")
public class OptionGroup extends AbstractEntity {
    private static final String OPTION_GROUP_PREFIX = "optg_";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String optionGroupToken;
    private String optionGroupName;
    private String optionGroupCode;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "optionGroup", cascade = CascadeType.PERSIST)
    private List<Option> optionList = new ArrayList<>();

    @Builder
    public OptionGroup(String optionGroupName, String optionGroupCode) {
        if (!StringUtils.hasText(optionGroupName)) {
            throw new InvalidParamException("ItemOptionGroup.itemOptionGroupName");
        }

        if (!StringUtils.hasText(optionGroupCode)) {
            throw new InvalidParamException("OptionGroup.optionGroupCode");
        }
        this.optionGroupToken = TokenGenerator.randomCharacterWithPrefix(OPTION_GROUP_PREFIX);
        this.optionGroupName = optionGroupName;
        this.optionGroupCode = optionGroupCode;
    }
}
