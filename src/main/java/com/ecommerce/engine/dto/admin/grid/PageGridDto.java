package com.ecommerce.engine.dto.admin.grid;

import com.ecommerce.engine.entity.Page;
import java.time.Instant;

public record PageGridDto(
        long id, String title, boolean bottom, int sortOrder, Instant created, Instant updated, boolean enabled) {

    public PageGridDto(Page page) {
        this(
                page.getId(),
                page.getLocaleTitle(),
                page.isBottom(),
                page.getSortOrder(),
                page.getCreated(),
                page.getUpdated(),
                page.isEnabled());
    }
}
