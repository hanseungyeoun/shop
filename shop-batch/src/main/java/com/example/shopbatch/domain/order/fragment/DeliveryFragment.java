package com.example.shopbatch.domain.order.fragment;

import com.example.shopbatch.common.exception.InvalidParamException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
public class DeliveryFragment {
    private String receiverName;
    private String receiverPhone;
    private String receiverZipcode;
    private String receiverAddress1;
    private String receiverAddress2;
    private String etcMessage;

    @Builder
    public DeliveryFragment(
            String receiverName,
            String receiverPhone,
            String receiverZipcode,
            String receiverAddress1,
            String receiverAddress2,
            String etcMessage
    ) {
        if (!StringUtils.hasText(receiverName)) throw new InvalidParamException("DeliveryFragment receiverName");
        if (!StringUtils.hasText(receiverPhone)) throw new InvalidParamException("DeliveryFragment receiverPhone");
        if (!StringUtils.hasText(receiverZipcode)) throw new InvalidParamException("DeliveryFragment receiverZipcode");
        if (!StringUtils.hasText(receiverAddress1)) throw new InvalidParamException("DeliveryFragment receiverAddress1");
        if (!StringUtils.hasText(receiverAddress2)) throw new InvalidParamException("DeliveryFragment receiverAddress2");
        if (!StringUtils.hasText(etcMessage)) throw new InvalidParamException("DeliveryFragment etcMessage");

        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverZipcode = receiverZipcode;
        this.receiverAddress1 = receiverAddress1;
        this.receiverAddress2 = receiverAddress2;
        this.etcMessage = etcMessage;
    }
}