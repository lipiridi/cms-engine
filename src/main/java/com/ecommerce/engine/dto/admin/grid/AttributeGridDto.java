package com.ecommerce.engine.dto.admin.grid;

import com.ecommerce.engine.entity.Attribute;
import com.ecommerce.engine.enums.InputType;

public record AttributeGridDto(long id, String name, InputType inputType, boolean enabled) {
    public AttributeGridDto(Attribute attribute) {
        this(attribute.getId(), attribute.getLocaleName(), attribute.getInputType(), attribute.isEnabled());
    }
}
