package com.example.shop.service;

import com.example.shop.common.exception.EntityNotFoundException;
import com.example.shop.domain.item.option.Option;
import com.example.shop.domain.item.option.OptionGroup;
import com.example.shop.repositiory.item.option.OptionGroupRepository;
import com.example.shop.repositiory.item.option.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.shop.dto.item.option.OptionDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionService {

    private final OptionRepository optionRepository;
    private final OptionGroupRepository optionGroupRepository;

    @Transactional
    public String createOption(OptionForm optionDto) {
        OptionGroup optionGroup = optionGroupRepository.findByOptionGroupToken(optionDto.getOptionGroupToken())
                .orElseThrow(() -> new EntityNotFoundException("옵션 정보를 찾을 수 없습니다. token : " + optionDto.getOptionGroupToken()));

        Option option = optionDto.toEntity(optionGroup);
        Option savedOption = optionRepository.save(option);
        return savedOption.getOptionToken();
    }

    public Map<String, Set<String>> getOptionInfo() {
        return optionGroupRepository.findAll().stream().
                map(OptionGroup::getOptionGroupCode)
                .collect(Collectors.toMap(Function.identity(), code -> {
                            return optionRepository.findByOptionGroup_OptionGroupCode(code).stream()
                                    .map(Option::getOptionName)
                                    .collect(Collectors.toSet());
                        })
                );
    }

    public List<OptionGroupResponseDto> getOptionGroups() {
        return optionGroupRepository.findAll().stream()
                .map(optionGroup -> {
                    List<OptionResponseDto> optionResponseDtoList = optionGroup.getOptionList().stream()
                            .map(OptionResponseDto::new)
                            .collect(Collectors.toList());

                    return new OptionGroupResponseDto(optionGroup, optionResponseDtoList);
                })
                .collect(Collectors.toList());
    }

    public OptionForm getOption(String optionToken) {
        return optionRepository.findByOptionToken(optionToken)
                .map(option -> {
                    return new OptionForm(option.getOptionGroup().getOptionGroupToken(), option);
                }).orElseThrow(() -> new EntityNotFoundException("옵션 정보를 찾을 수 없습니다. token : " + optionToken));
    }

    @Transactional
    public void modifyOption(String optionToken, OptionForm optionDto) {
        Option option = optionRepository.findByOptionToken(optionToken)
                .orElseThrow(EntityNotFoundException::new);

        option.setOptionCode(optionDto.getOptionCode());
        option.setOptionName(optionDto.getOptionCode());
    }

    @Transactional
    public void deleteOption(String optionToken) {
        optionRepository.deleteByOptionToken(optionToken);
    }


}
