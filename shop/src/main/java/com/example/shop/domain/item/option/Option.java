package com.example.shop.domain.item.option;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import com.example.shop.util.TokenGenerator;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "option")
public class Option extends AbstractEntity {
    private static final String OPTION_PREFIX = "opt_";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String optionToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    @Setter
    private String optionName;
    @Setter
    private String optionCode;

    private void changeOptionGroup(OptionGroup optionGroup) {
        if(this.optionGroup != null) {
            this.optionGroup.getOptionList().remove(this);
        }

        this.optionGroup = optionGroup;
        optionGroup.getOptionList().add(this);
    }

    @Builder
    private Option(
            OptionGroup optionGroup,
            String optionName,
            String optionCode
    ) {
        if (optionGroup == null) throw new InvalidParamException("Option.optionGroup");
        if (!StringUtils.hasText(optionName)) throw new InvalidParamException("Option.optionName");
        if (!StringUtils.hasText(optionCode)) throw new InvalidParamException("Option.optionCode");

        this.optionToken = TokenGenerator.randomCharacterWithPrefix(OPTION_PREFIX);

        this.optionName = optionName;
        this.optionCode = optionCode;

        changeOptionGroup(optionGroup);
    }
}
