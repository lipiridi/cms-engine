package com.ecommerce.engine.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@UtilityClass
public class TranslationUtils {
    private static final ResourceBundleMessageSource MESSAGE_SOURCE;

    static {
        MESSAGE_SOURCE = new ResourceBundleMessageSource();
        MESSAGE_SOURCE.setBasenames("translation/messages");
        MESSAGE_SOURCE.setDefaultLocale(Locale.ENGLISH);
    }

    public String getMessage(String key, Object... args) {
        return MESSAGE_SOURCE.getMessage(key, args, Locale.ENGLISH);
    }
}
