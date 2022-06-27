package com.chaeking.api.domain.value;

public record TermsValue(
        Long termsLogId,
        String title,
        String url,
        String effectiveOn
) {
}
