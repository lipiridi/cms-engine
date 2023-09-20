package com.ecommerce.engine.repository.entity;

import com.ecommerce.engine.dto.common.DescriptionDto;
import com.ecommerce.engine.util.TranslationUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Collection;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class DescriptionSuperclass {
    @Id
    @Column(length = 5)
    Locale locale;

    String title;

    @Column(columnDefinition = "text")
    String description;

    String metaTitle;
    String metaDescription;

    public DescriptionSuperclass(DescriptionDto descriptionDto) {
        locale = descriptionDto.locale();
        title = descriptionDto.title();
        description = descriptionDto.description();
        metaTitle = descriptionDto.metaTitle();
        metaDescription = descriptionDto.metaDescription();
    }

    public static String getLocaleTitle(Collection<? extends DescriptionSuperclass> descriptions) {
        return descriptions.stream()
                .filter(DescriptionSuperclass::isUserLocale)
                .findFirst()
                .map(DescriptionSuperclass::getTitle)
                .orElse(null);
    }

    public boolean isUserLocale() {
        return TranslationUtils.getUserLocale().equals(locale);
    }
}