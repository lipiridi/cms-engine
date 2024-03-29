package com.ecommerce.engine.dto.admin.response;

import com.ecommerce.engine.dto.admin.common.MetaDescriptionDto;
import com.ecommerce.engine.entity.Page;
import java.time.Instant;
import java.util.Set;

public record PageResponseDto(
        long id,
        boolean bottom,
        int sortOrder,
        Instant created,
        Instant updated,
        boolean enabled,
        Set<MetaDescriptionDto> descriptions) {
    public PageResponseDto(Page page) {
        this(
                page.getId(),
                page.isBottom(),
                page.getSortOrder(),
                page.getCreated(),
                page.getUpdated(),
                page.isEnabled(),
                MetaDescriptionDto.createMetaDescriptionDtoSet(page.getDescriptions()));
    }
}
