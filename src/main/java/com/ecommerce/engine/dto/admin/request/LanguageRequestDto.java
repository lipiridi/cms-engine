package com.ecommerce.engine.dto.admin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Locale;

public record LanguageRequestDto(
        @NotBlank @Size(max = 255) String name,
        @NotNull Locale hreflang,
        @Pattern(regexp = "[a-z]{2}([_-][a-z]{2})?") String subFolder,
        @Size(max = 5) String suffixUrl,
        @JsonProperty("default") boolean defaultLang,
        boolean enabled) {}
