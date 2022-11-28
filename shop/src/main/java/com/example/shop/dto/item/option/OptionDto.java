package com.example.shop.dto.item.option;

import com.example.shop.domain.item.option.Option;
import com.example.shop.domain.item.option.OptionGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class OptionDto {

    @Data
    public static class OptionGroupForm {
        @NotEmpty(message = "optionGroupName 는 필수값입니다")
        private String optionGroupName;
        @NotEmpty(message = "optionName 는 필수값입니다")
        private String optionGroupCode;

        List<OptionForm> optionList;

        OptionGroup toEntity(){
            return OptionGroup.builder()
                    .optionGroupName(optionGroupName)
                    .optionGroupCode(optionGroupCode)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    public static class OptionForm {
        @NotEmpty(message = "optionGroup 은 필수값입니다")
        private String optionGroupToken;

        @NotEmpty(message = "optionName 는 필수값입니다")
        private String optionName;
        @NotEmpty(message = "optionName 는 필수값입니다")
        private String optionCode;

        @Builder
        public OptionForm(String optionGroupToken, Option option) {
            this.optionGroupToken = optionGroupToken;
            this.optionName = option.getOptionName();
            this.optionCode = option.getOptionCode();
        }

        public Option toEntity(OptionGroup optionGroup){
            return Option.builder()
                    .optionGroup(optionGroup)
                    .optionName(optionName)
                    .optionCode(optionCode)
                    .build();
        }
    }

    @Data
    public static class OptionGroupResponseDto {
        private Long id;
        private String optionGroupToken;
        private String optionGroupName;
        private String optionGroupCode;

        private List<OptionResponseDto> optionList;

        public OptionGroupResponseDto(OptionGroup optionGroup, List<OptionResponseDto> optionRequestDtoList) {
            this.id = optionGroup.getId();
            this.optionGroupToken = optionGroup.getOptionGroupToken();
            this.optionGroupName = optionGroup.getOptionGroupName();
            this.optionGroupCode = optionGroup.getOptionGroupCode();
            this.optionList = optionRequestDtoList;
        }
    }

    @Data
    public static class OptionResponseDto {
        private Long id;
        private String optionToken;
        private String optionName;
        private String optionCode;

        public OptionResponseDto(Option option) {
            this.id = option.getId();
            this.optionToken = option.getOptionToken();
            this.optionName = option.getOptionName();
            this.optionCode = option.getOptionCode();
        }
    }
}
