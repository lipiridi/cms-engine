package com.ecommerce.engine.dto.admin.response;

import com.ecommerce.engine.dto.admin.common.NameDescriptionDto;
import com.ecommerce.engine.entity.PaymentMethod;
import java.util.Set;

public record PaymentMethodResponseDto(long id, boolean enabled, Set<NameDescriptionDto> descriptions) {

    public PaymentMethodResponseDto(PaymentMethod paymentMethod) {
        this(
                paymentMethod.getId(),
                paymentMethod.isEnabled(),
                NameDescriptionDto.createNameDescriptionDtoSet(paymentMethod.getDescriptions()));
    }
}
